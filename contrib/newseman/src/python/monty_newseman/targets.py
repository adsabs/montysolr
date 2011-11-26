
from montysolr.initvm import JAVA as j
from montysolr.utils import make_targets

from newseman.sea.man import seaman_maker, Surface, callbacks
from newseman.content.Document import Token, TokenCollection, Document
from newseman.ci8.semes import filter_semantic_tokens

JArray_object = j.JArray_object
JArray_string = j.JArray_string
tokenfeature_cleared = Surface.tokenfeature_cleared
tokenfeature_sem = Surface.tokenfeature_sem
tokenfeature_parsed_surface = Surface.tokenfeature_parsed_surface
tokenfeature_prefix = Surface.tokenfeature_prefix
tokenfeature_suffix = Surface.tokenfeature_suffix
tokenfeature_radix = Surface.tokenfeature_radix
tokenfeature_success = Surface.tokenfeature_success
tokenfeature_extrasem = Surface.tokenfeature_extrasem
tokenfeature_extrasurface = Surface.tokenfeature_extrasurface

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
            tokens
            
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
            cleaning = message.getParam('grp_cleaning')
        max_distance = 0
        if message.hasParam('max_distance'):
            max_distance = int(message.getParam('max_distance'))
        seman.registerCallback('after_translation', 
                callbacks.cb_after_translation_getter(max_distance=max_distance,
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
        tokens = JArray_object.cast_(tokens)
        first_row = list(JArray_string.cast_(tokens[0]))
        row_len = len(first_row)
        row_keys = first_row # get the name of columns
        ret_keys = row_keys[:]
        ret_keys[0] = tokenfeature_cleared # instead of token, return other key
            
        j = 1 # skip header
        l = len(tokens)
        while j < l:
            row = JArray_string.cast_(tokens[j])
            token = Token(row[0])
            row_len = len(row)
            i = 2
            while i < row_len:
                token.setFeature(row_keys[i], row[i])
                i += 1
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
        final_len = len(tc) + len(filter_semantic_tokens(tc, attr=tokenfeature_extrasem))
        final_results = JArray_object(final_len)
        i = 0
        while i < final_len:
            token = tc[i]
            t = []
            for ii in range(len(row_keys)): # take out the old values
                #t.append(row_keys[ii])
                val = token.getFeature(ret_keys[ii])
                if isinstance(val, list):
                    t.append(val[0])  # merged values, eg id=[14,15]
                else:
                    t.append(val)
                
            sem = token.getFeature(tokenfeature_sem)
            if sem:
                #t.append(tokenfeature_sem)
                t.append(' '.join(sem))
            
            esem = token.getFeature(tokenfeature_sem)
            if esem:
                t.append(esem)
            final_results[i] = JArray_string(t)
            i += 1
        
        # clean up
        message.setParam("tokens", None)
        
        # save into message
        message.setResults(final_results)


def montysolr_targets():
    return make_targets(initialize_seman=initialize_seman,
                        translate_token_collection=translate_token_collection)
