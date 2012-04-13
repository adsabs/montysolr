
from montysolr.initvm import JAVA as j
from montysolr.utils import make_targets

from newseman.sea.man import seaman_maker, Surface
from newseman.sea import callbacks
from newseman.content.Document import Token, TokenCollection, Document
from newseman.ci8.semes import filter_semantic_tokens
from newseman.sea.callbacks import tokenfeature_orig
from newseman.content.Document import SemStr

JArray_object = j.JArray_object #@UndefinedVariable
JArray_string = j.JArray_string #@UndefinedVariable

tokenfeature_cleared = Surface.tokenfeature_cleared
tokenfeature_sem = Surface.tokenfeature_sem
tokenfeature_parsed_surface = Surface.tokenfeature_parsed_surface
tokenfeature_prefix = Surface.tokenfeature_prefix
tokenfeature_suffix = Surface.tokenfeature_suffix
tokenfeature_radix = Surface.tokenfeature_radix
tokenfeature_success = Surface.tokenfeature_success
tokenfeature_extrasem = callbacks.tokenfeature_extrasem
tokenfeature_extrasurface = callbacks.tokenfeature_extrasurface
tokenfeature_extracanonical = callbacks.tokenfeature_extracanonical
tokenfeature_multi = callbacks.tokenfeature_multi

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
        
        @param language: language to set 
        
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
    
    if message.getParam("language"):
        seman.setPersistentParameter("language", str(message.getParam("language")))
    
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
    
    val_separator = str(message.getParam('value_separator') or '|')
    def prepare_dict_value(x):
            s = SemStr(val_separator.join(x.value.split(' ')))
            s.set('t', x.type)
            s.set('l', x.language)
            s.set('g', x.group)
            return s
    seman.registerCallback('prepare_dict_value', prepare_dict_value)
        

def translate_tokens(message):
    """Translates the array of tokens into semantic codes
        @param tokens: two dimensional array of tokens to 
            translate
        @param url: url of the seman database, it is used
            to identify processes
    """
    
    tokens = message.getParam("tokens")
    join_char = str(message.getParam("joinChar") or '|')
    
    
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
        ret_keys.append("extra-sem") # the sem of the group
        ret_keys.append("extra-canonical") #when a group is identified, its canonical form
        ret_keys.append("extra-synonyms") #when a group is identified, its canonical form
        ret_keys.append(tokenfeature_multi) #indicator of the callback
        #ret_keys.append("synonyms")
        
        
        # descr of incoming features (related to outgoing data)
        ret_features = header[:]
        ret_features[0] = tokenfeature_orig
        ret_features.append(tokenfeature_sem)
        ret_features.append(tokenfeature_extrasem)
        ret_features.append(tokenfeature_extracanonical)
        ret_features.append(tokenfeature_extrasurface)
        ret_features.append(tokenfeature_multi)
        #ret_features.append(tokenfeature_synonyms)
        
        
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
        url = str(message.getParam("url") or '')
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
        ret_arr = [None] * len(ret_keys) # care must be taken to place vals into correct place
        while i < final_len:
            
            token = tc[i]
            t = ret_arr[:]
            
            max_col = 0
            ii = 0
            for feature in ret_features:
                val = token.getFeature(feature)
                if val:
                    max_col = ii
                    if isinstance(val, list):
                        t[ii] = join_char.join(val)
                    else:
                        t[ii] = str(val)
                ii += 1
            
            final_results[r] = JArray_string(t[:max_col+1]) # we want to limit amount of data sent
            i += 1
            r += 1
        
        # clean up
        message.setParam("tokens", None)
        
        # save into message
        message.setResults(final_results)


def montysolr_targets():
    return make_targets("newseman.SemanticTagger:translate_tokens", translate_tokens,
                        "newseman.SemanticTagger:initialize_seman", initialize_seman,
                        initialize_seman=initialize_seman,
                        translate_tokens=translate_tokens,
                        configure_seman=configure_seman)
