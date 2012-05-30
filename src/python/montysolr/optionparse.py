"""\
:Author: M. Simionato
:Date: April 2004
:Title: A much simplified interface to optparse.

You should use optionparse in your scripts as follows.
First, write a module level docstring containing something like this
(this is just an example):

'''usage: %prog files [options]
   -d, --delete: delete all files
   -e, --erase = ERASE: erase the given file'''

Then write a main program of this kind:

# sketch of a script to delete files
if __name__=='__main__':
    import optionparse
    option,args=optionparse.parse(__doc__)
    if not args and not option: optionparse.exit()
    elif option.delete: print "Delete all files"
    elif option.erase: print "Delete the given file"

Notice that ``optionparse`` parses the docstring by looking at the
characters ",", ":", "=", "\\n", so be careful in using them. If
the docstring is not correctly formatted you will get a SyntaxError
or worse, the script will not work as expected.
"""

import optparse, re, sys
import ConfigParser


#USAGE = re.compile(r'(?s)\s*usage: (.*?)(\n[ \t]*\n|$)')
USAGE = re.compile(r'(?s)(.*)\s*usage: (.*?)(\n[ \t]*\n|$)')

def nonzero(self): # will become the nonzero method of optparse.Values
    "True if options were given"
    for v in self.__dict__.itervalues():
        if v is not None: return True
    return False

optparse.Values.__nonzero__ = nonzero # dynamically fix optparse.Values

class ParsingError(Exception): pass

optionstring=""

def exit(msg=""):
    raise SystemExit(msg or optionstring.replace("%prog",sys.argv[0]))

def parse(docstring, arglist=None):
    global optionstring
    optionstring = docstring
    match = USAGE.search(optionstring)
    if not match: raise ParsingError("Cannot find the option string")
    #optlines = match.group(1).splitlines()
    optlines = match.group(2).splitlines()
    formatter = MyFormatter()
    try:
        p = optparse.OptionParser(optlines[0], epilog=str(match.group(1)).strip(), formatter=formatter)
        for line in optlines[1:]:
            if not line.strip():
                break
            opt, help=line.split(':', 1)[:2]
            short,long=opt.split(',', 1)[:2]
            default = None
            try:
                help, default = re.compile(r'\[|\]').split(help)[:2]
                default = default.strip()
            except:
                pass
            if '=' in opt:
                action='store'
                long=long.split('=')[0]
            else:
                action='store_true'
            p.add_option(short.strip(),long.strip(),
                         action = action, help = help.strip(), default=default)
    except (IndexError,ValueError):
        raise ParsingError("Cannot parse the option string correctly")
    
    options,args=p.parse_args(arglist)
    return (explode(options), args)

def explode(options):
    values = options.__dict__
    for key, value in values.iteritems():
        if value and isinstance(value, basestring) and value.find(',') > -1:
            values[key] = filter(len, [i.strip() for i in value.split(',')])
    return options

def add2options(cfgfo, options, secname='runtime'):
    """
    Takes values from the config file (section runtime)
    and adds them to the options (will overwrite existing keys only
    if their value is set to None)
    @var cfgfo: file-object opened for reading
    @var options: dictionary with parsed values (options)
    @keyword secname: name of the section from which to read values [runtime]
    @return: options 
    """
    cfg = ConfigParser.ConfigParser()
    cfg.readfp(cfgfo, filename=cfgfo)
    for k, v in cfg.items(secname):
        if "\n" in v:
            v = filter(len, v.splitlines())
        if k not in options or options[k] is None:
            options[k] = v
    return options

class MyFormatter(optparse.IndentedHelpFormatter):
  def format_epilog(self, epilog):
    return epilog
