
from montysolr.initvm import JAVA as j
from montysolr.utils import make_targets

from newseman.sea.man import seaman_maker, Surface
from newseman.sea import callbacks
from newseman.content.Document import Token, TokenCollection, Document
from newseman.ci8.semes import filter_semantic_tokens
from newseman.sea.callbacks import tokenfeature_orig

JArray_object = j.JArray_object
JArray_string = j.JArray_string
tokenfeature_cleared = Surface.tokenfeature_cleared
tokenfeature_sem = Surface.tokenfeature_sem
tokenfeature_parsed_surface = Surface.tokenfeature_parsed_surface
tokenfeature_prefix = Surface.tokenfeature_prefix
tokenfeature_suffix = Surface.tokenfeature_suffix
tokenfeature_radix = Surface.tokenfeature_radix
tokenfeature_success = Surface.tokenfeature_success
tokenfeature_extrasem = callbacks.tokenfeature_extrasem
tokenfeature_extrasurface = callbacks.tokenfeature_extrasurface

class Cacher(object):
    def __init__(self):
        self.cache = {}
        self.last = None
    def get_seman(self, url):
        if url in self.cache:
            return self.cache[url]
        else:
            self.cache[url] = seaman_maker(url=url)
            self.last = url
            return self.cache[url]
    def set_seman(self, name, seman):
        self.cache[name] = seman
        self.last = name
    
    def get_last(self):
        return self.cache[self.last]
    
Cacher = Cacher()



def initialize_seman(message):
    """Initialize the seman translator
        @param url: (str) database connection url
        @param name: (str) name under which the initialized
            seman instance will be stored and accessible. If
            empty, it will be saved using url
        @param verbose: (boolean) whether to print queries
            that are executed, can slow down your program 
            considerably
        @param language: (str) code of the language of the
            texts we are processing
        
    """
    url = str(message.getParam("url"))
    name = str(message.getParam("name") or url)
    
    kwargs = {'url':url}
    kwargs['verbose'] = bool(message.getParam("verbose")) 
    seman = seaman_maker(surface_dictionary_config=kwargs)
    
    if message.getParam("language"):
        seman.setPersistentParameter("language", str(message.getParam("language")))
    
    Cacher.set_seman(name, seman)


def configure_seman(message):
    """Configures the existing instance of seman using the 
        callbacks
        @param url: url or name of the existing instance
        
        @group fuzzy matching:
            Activates the discovery of multi-token groups
            even if they were separated by several other
            tokens.
            
            NOTE: the dictionary must already contain the multi-token
            groups that we want to find! They will be loaded at function
            init.
            
            @param max_distance: (int) max number of tokens to allow
                as separators
            @param grp_action: rewrite|add|insert_before|insert_after
            @param grp_cleaning: purge|remove
    """
    seman = Cacher.get_seman(str(message.getParam('url')))
    
    if seman is None:
        raise Exception('Seman is not initialized!')
    
    if message.getParam('grp_action'):
        action = str(message.getParam('grp_action'))
        cleaning = None
        if message.getParam('grp_cleaning'):
            cleaning = str(message.getParam('grp_cleaning'))
        max_distance = 0
        if message.getParam('max_distance'):
            max_distance = int(str(message.getParam('max_distance')))
        seman.registerCallback('after_translation', 
                callbacks.cb_after_translation_proximity_getter(seman,
                                                      max_dist=max_distance,
                                                      grp_action=action,
                                                      grp_cleaning=cleaning))
        
        

def translate_tokens(message):
    """Translates the array of tokens into semantic codes
        @param tokens: two dimensional array of tokens to 
            translate
        @param url: url of the seman database, it is used
            to identify processes
    """
    
    tokens = message.getParam("tokens")
    
    
    if (tokens):
        # create tokencolleation from the array
        tc = TokenCollection()
        
        # the data
        tokens = JArray_object.cast_(tokens)
        
        # description of incoming data
        header = list(JArray_string.cast_(tokens[0]))
        header_len = len(header)
        
        # descr of outgoing data
        ret_keys = header[:]
        ret_keys.append("sem")
        ret_keys.append("synonyms") # not implemented yet
        ret_keys.append("multi-sem") # the sem of the group
        ret_keys.append("multi-synonyms") #when a group is identified, its canonical form
        
            
        j = 1 # skip header
        l = len(tokens)
        while j < l:
            row = JArray_string.cast_(tokens[j])
            token = Token(row[0]) # token is first
            for i in range(1, len(row)): # set the rest
                token.setFeature(header[i], row[i])
            tc.append(token)
            j += 1
    
        # get seman
        url = message.getParam("url")
        if url:
            seman = Cacher.get_seman(url)
        else:
            seman = Cacher.get_last()
            
        
        # translate
        doc = Document(tokens=tc)
        language = str(message.getParam("language") or '')
        if language:
            seman.translateTokenizedDocument(doc, language=language)
        else:
            seman.translateTokenizedDocument(doc)
        
        
        # convert back to java array
        tc = doc.tokens()
        final_len = len(tc)
        final_results = JArray_object(final_len + 1) # + header
        
        # insert header
        final_results[0] = JArray_string(ret_keys)
        
        # insert rows
        i = 0
        r = 1
        idx_sem = ret_keys.index("sem")
        idx_grp = ret_keys.index("multi-synonyms")
        idx_grp_sem = ret_keys.index("multi-sem")
        while i < final_len:
            token = tc[i]
            t = []
            t.append(token.getFeature(tokenfeature_orig))
            
            for ii in range(1, len(header)): # take out the old values
                val = token.getFeature(header[ii])
                if isinstance(val, list):
                    t.append(val[0])  # merged values, eg id=[14,15]
                else:
                    t.append(val)
                
            sem = token.getFeature(tokenfeature_sem)
            if sem:
                t.insert(idx_sem, isinstance(sem, list) and ' '.join(sem) or sem)
            
            esem = token.getFeature(tokenfeature_extrasem)
            if esem:
                etoken = token.getFeature(tokenfeature_extrasurface)
                t.insert(idx_grp, etoken)
                t.insert(idx_grp_sem, isinstance(esem, list) and ' '.join(esem) or esem)
            
            final_results[r] = JArray_string(t)
            i += 1
            r += 1
        
        # clean up
        message.setParam("tokens", None)
        
        # save into message
        message.setResults(final_results)


def montysolr_targets():
    return make_targets(initialize_seman=initialize_seman,
                        translate_tokens=translate_tokens,
                        configure_seman=configure_seman)
