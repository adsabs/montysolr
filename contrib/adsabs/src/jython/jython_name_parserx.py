from org.jython.monty.interfaces import JythonNameParser



class HumanParser(JythonNameParser):

    def __init__(self):
        self.suffixes = 1


    def parse_human_name(self, input):
        """
        This is the only public method - accessible from java
        """
        return {'First': 'Jimmi', 'Last': 'Hendrix'}


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