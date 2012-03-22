
import sys
import subprocess as sub
import os

def run(grammar_name, basedir='', 
        cp='.:/dvt/antlr-142/lib/antlr-3.4-complete.jar:/x/dev/antlr-34/lib/antlr-3.4-complete.jar',
        grammardir='',
        java_executable='java',
        dot_executable='dot'
        ):
    
    
    if not basedir:
        basedir = os.path.abspath('../../../../../../../../../../bin')

    old_dir = os.getcwd()
    
    thisdir = grammardir
    if not thisdir:        
        thisdir = os.path.dirname(os.path.abspath(__file__))
    os.chdir(thisdir)
    
    cp += os.pathsep + basedir

    #print "We'll generate ANTLR graphs\ngramar: %s\nbasedir: %s\nclasspath: %s\nparserdir: %s" % (grammar_name, basedir, cp, thisdir)
    
    grammar_file = os.path.join(thisdir, grammar_name + '.g')
    
    if not os.path.exists(grammar_file):
        raise Exception('Grammar %s does not exist in classpath: %s' % (grammar_file, cp))
    
    tmp_file = os.path.join(basedir, 'ast-tree.dot')
    index_file = os.path.join(basedir, '%s.html' % grammar_name)
    gunit_file = os.path.join(thisdir, grammar_name + '.gunit')
    generate_ast_command = '%s -cp %s org.apache.lucene.queryParser.aqp.parser.BuildAST %s "%%s"' % (java_executable, cp, grammar_name)
    
    
    generate_svg_command = '%s -Tsvg %s' % (dot_executable, tmp_file)
    
    test_cases = load_gunit_file(gunit_file)
    
    index_fo = open(index_file, 'w')
    index_fo.write('<h1>Test cases generated from grammar %s</h1>\n' % grammar_name)
    
    out_lines = []
    i = 0
    cmds = generate_ast_command.split()
    cmds_svg = generate_svg_command.split()
    
    total = sum(map(lambda x: len(x), test_cases.values()))
    
    toc = []
    data = []
    
    toc.append('<a name="toc" />')
    
    for section,values in test_cases.items():
        output = tree = svg = ''
        
        toc.append('<a href="#anchor%s"><pre>%s</pre></a><br/>' % (section, section))
        
        # generate AST tree
        for query in values:
            i += 1
            cmds[-1] = query
            
            #tmp_dot = os.path.join(basedir, 'tmp-%s.dot' % i)
            
            tmp_dot = tmp_file
            
            if os.path.exists(tmp_dot):
                os.remove(tmp_dot)
            
            toc.append('%s. <a href="#anchor%s"><pre>%s</pre></a><br/>' % (i, i, query))
            
            print '//  %s/%s :: %s' % (i, total, query)
            
            
            #generate graph
            p = sub.Popen(cmds,stdout=sub.PIPE,stderr=sub.PIPE)
            
            output, errors = p.communicate()
            if output:
                fo = open(tmp_dot, 'w')
                fo.write(output)
                fo.close()
            else:
                print 'Error generating AST for: ' + query
                print errors
                if 'java.lang.ClassNotFoundException' in errors:
                    raise Exception('Please fix your classpath')
                continue

            #generate tree
            cmds.append(section)
            cmds.append("tree")
            p = sub.Popen(cmds,stdout=sub.PIPE,stderr=sub.PIPE)
            
            tree, errors = p.communicate()
            if tree:
                q = query.replace('\\', '\\\\').replace('"', '\\"').replace('\'', '\\\'')
                t = tree.strip().replace('\\', '\\\\').replace('"', '\\"').replace("'", "\\'")
                print "\"%s\" -> \"%s\"" % (q, t)
            else:
                print 'Error generating AST for: ' + query
                print errors
                tree = errors
                
            cmds.pop()
            cmds.pop()
                        
            cmds_svg[-1] = tmp_dot
            
            try:
                p = sub.Popen(cmds_svg,stdout=sub.PIPE,stderr=sub.PIPE)
            except Exception, e:
                print "The following command failed:"
                print ' '.join(cmds_svg)
                raise e
            
            output, errors = p.communicate()
            
            data.append(' <a name="anchor%s"/><h3>%s. <pre">%s</pre>&nbsp;&nbsp; <a href="#toc">^</a> </h3>' % (i, i, query))
            data.append(output)
            data.append('<br/><pre>' + tree + '</pre>')
            data.append('<br/>')
    
    index_fo.write('''
    <html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<style type="text/css">
    pre {display:inline;}
</style>
</head>
</body>    
    ''')
    index_fo.write('\n'.join(toc))
    index_fo.write('\n'.join(data))
    
    index_fo.write('''
</body>
</html>    
    ''')    
    index_fo.close()
        
    print 'HTML charts generated into:', index_fo.name        
    os.chdir(old_dir)
        

def load_gunit_file(gunit_file):
    fi = open(gunit_file, 'r')
    test_cases = {}
    section = None
    for line in fi:
        l = line.strip()
        if not l or l[:2] == '//':
            continue
        parts = split_line(l)
        if len(parts) == 1 and parts[0][-1] == ':':
            section = parts[0][:-1]
            test_cases.setdefault(section, [])
        elif len(parts) > 1 and parts[1].lower() != 'fails':
            query = parts[0]
            query = query.replace('\\\"', '"').replace('\\\'', '\'').replace('\\\\', '\\')
            test_cases[section].append(query)
    fi.close()
    return test_cases
            
        
def split_line(line):
    line = line.replace('->', '')
    start = 0
    last_pos = None
    parts = []
    while line.find('"', start) > -1:
        p = line.index('"', start)
        start = p+1
        if line[p-1] != '\\':
            if last_pos is None:
                last_pos = p
            else:
                parts.append(line[last_pos+1:p])
                parts.append(line[p+1:].strip())
                last_pos = None
                break
    if not parts:
        parts.append(line.strip())
    return parts


if __name__ == '__main__':
    if len(sys.argv) == 1:
        sys.argv.insert(1, "StandardLuceneGrammar")
    run(*sys.argv[1:])
