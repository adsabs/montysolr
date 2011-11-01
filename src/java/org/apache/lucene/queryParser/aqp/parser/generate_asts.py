
import sys
import subprocess as sub
import os

def run(grammar_name, basedir='', cp='.:/dvt/antlr-142/lib/antlr-3.4-complete.jar:/x/dev/antlr-34/lib/antlr-3.4-complete.jar'):
    
    if not basedir:
        basedir = os.path.abspath('../../../../../../../../bin')

    old_dir = os.getcwd()        
    thisdir = os.path.dirname(os.path.abspath(__file__))
    os.chdir(thisdir)
    
    cp += os.pathsep + basedir
    
    grammar_file = os.path.join(thisdir, grammar_name + '.g')
    
    if not os.path.exists(grammar_file):
        raise Exception('Grammar %s does not exist in classpath: %s' % (grammar_file, cp))
    
    tmp_file = os.path.join(basedir, 'ast-tree.dot')
    index_file = os.path.join(basedir, '%s.html' % grammar_name)
    gunit_file = os.path.join(thisdir, grammar_name + '.gunit')
    generate_ast_command = 'java -cp %s org.apache.lucene.queryParser.aqp.parser.BuildAST %s "%%s"' % (cp, grammar_name)
    
    generate_grammar_command = '%s/demo.sh build-only %s' % (thisdir, grammar_name)
    
    generate_svg_command = 'dot -Tsvg %s' % tmp_file
    
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
    
    p = sub.Popen(generate_grammar_command.split(),stdout=sub.PIPE,stderr=sub.PIPE, cwd=thisdir)
    output, errors = p.communicate()
    if errors and 'Exception' in errors:
        raise Exception(errors)
    #os.system(generate_grammar_command)
    
    for section,values in test_cases.items():
        
        # generate AST tree
        for query in values:
            i += 1
            cmds[-1] = query
            
            #tmp_dot = os.path.join(basedir, 'tmp-%s.dot' % i)
            
            tmp_dot = tmp_file
            
            if os.path.exists(tmp_dot):
                os.remove(tmp_dot)
            
            toc.append('%s. <a href="#anchor%s"><pre>%s</pre></a><br/>' % (i, i, query))
            
            print '%s/%s :: %s' % (i, total, query)
            #print cmds
            p = sub.Popen(cmds,stdout=sub.PIPE,stderr=sub.PIPE)
            output, errors = p.communicate()
            if output:
                fo = open(tmp_dot, 'w')
                fo.write(output)
                fo.close()
            else:
                print 'Error generating AST for: ' + query
                print errors
                continue
            
            cmds_svg[-1] = tmp_dot
             
            p = sub.Popen(cmds_svg,stdout=sub.PIPE,stderr=sub.PIPE)
            
            output, errors = p.communicate()
            
            data.append(' <a name="anchor%s"/><h3>%s. <pre">%s</pre>&nbsp;&nbsp; <a href="#toc">^</a> </h3>' % (i, i, query))
            data.append(output)
            data.append('<br/>')
    
    index_fo.write('''
    <html>
<head>
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
        elif len(parts) > 1 and parts[1].lower() != 'fail':
            query = parts[0]
            query = query.replace('\\\"', '"')
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