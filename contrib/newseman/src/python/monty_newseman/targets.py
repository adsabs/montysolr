
from montysolr.initvm import JAVA as j
from montysolr.utils import make_targets

from newseman.sea.man import seaman_maker, Surface
from newseman.content.Document import Token, TokenCollection

JArray_object = j.JArray_object
JArray_string = j.JArray_string
tokenfeature_cleared = Surface.tokenfeature_cleared
tokenfeature_sem = Surface.tokenfeature_sem
tokenfeature_parsed_surface = Surface.tokenfeature_parsed_surface
tokenfeature_prefix = Surface.tokenfeature_prefix
tokenfeature_suffix = Surface.tokenfeature_suffix
tokenfeature_radix = Surface.tokenfeature_radix
tokenfeature_success = Surface.tokenfeature_success

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


def translate_token_collection(message):
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
        row_keys = first_row[0::2] # get the name of columns
        ret_keys = row_keys[:]
        ret_keys[0] = tokenfeature_cleared # instead of token, return other key
            
        
        for row in tokens:
            row = JArray_string.cast_(row)
            token = Token(row[1])
            i = 2
            while i < row_len:
                token.setFeature(row[i], row[i+1])
                i += 2
            tc.append(token)
    
        # get seman
        url = message.getParam("url")
        if url:
            seman = Cacher.get_seman(url)
        else:
            seman = Cacher.get_last()
            
        
        # translate
        language = str(message.getParam("language") or '')
        if language:
            seman.translateTokenCollection(tc, language=language)
        else:
            seman.translateTokenCollection(tc)
        
        
        # convert back to java array
        final_len = len(tc)
        final_results = JArray_object(final_len)
        i = 0
        while i < final_len:
            token = tc[i]
            t = []
            for ii in range(len(row_keys)): # take out the old values
                t.append(row_keys[ii])
                val = token.getFeature(ret_keys[ii])
                if isinstance(val, list):
                    t.append(val[0])  # merged values, eg id=[14,15]
                else:
                    t.append(val)
                
            sem = token.getFeature(tokenfeature_sem)
            if sem:
                t.append(tokenfeature_sem)
                t.append(' '.join(sem))
            final_results[i] = JArray_string(t)
            i += 1
        
        # clean up
        message.setParam("tokens", None)
        
        # save into message
        message.setResults(final_results)


def montysolr_targets():
    return make_targets(initialize_seman=initialize_seman,
                        translate_token_collection=translate_token_collection)
