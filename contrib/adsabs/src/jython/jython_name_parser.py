from org.jython.monty.interfaces import JythonNameParser

from nameparser import parser as nameparser
from nameparser import constants

HumanName = nameparser.HumanName


class Logger:
  def info(self, *args, **kwargs):
    pass
  def error(self, *args, **kwargs):
    pass
  def debug(self, *args, **kwargs):
    pass

for h in nameparser.log.handlers:
  log.removeHandler(h)
    
nameparser.log = Logger()
nameparser.logging = None    


class HumanParser(JythonNameParser):

    def __init__(self):
        prefixes = set()
        suffixes = set()

        prefixes.update(constants.PREFIXES)
        prefixes.add("'t")
        suffixes.update(constants.SUFFIXES)
        suffixes.remove('v')
        suffixes.remove('i') 

        self.prefixes = prefixes
        self.suffixes = suffixes 


    def parse_human_name(self, input):
        """
        This is the only public method - accessible from java
        """

        name = HumanName(self.fix_name(input), prefixes_c=self.prefixes, suffixes_c=self.suffixes)
        out = {}
        for k in ('first', 'middle', 'last', 'suffix', 'title'):
            if getattr(name, k):
                out[k[0].upper() + k[1:]] = getattr(name, k)
        name = None
        return out 


    def fix_name(self, input):
        """
        Very small changes to the name before we send it to the parser
        i am trying to not change things; but it seemed excessive to 
        put these tidbits into another java filter before the
        Pythonic filter, so i put them here
        
         #362 - smartly handle o' sullivan (there is a space after ')
         
        """
        if "' " in input:
            while "' " in input:
                start = input.find("' ")
                end = start + 2
                while end+1 < len(input) and input[end+1] == ' ':
                    end =+ 1 
                input = input.replace(input[start:end], "'")
        return input