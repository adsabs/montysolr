import sys 
from invenio import search_engine
from invenio import dbquery


def create_collection_bibrec(table_name, coll_name, step_size=10000, maxsize=None):
    if table_name[0] != '_':
        raise Exception("By convention, temporary tables must begin with '_'. I don't want to give you tools to screw st important")
    
    create_stmt = dbquery.run_sql("SHOW CREATE TABLE bibrec")[0][1].replace('bibrec', dbquery.real_escape_string(table_name))
    dbquery.run_sql("DROP TABLE IF EXISTS `%s`" % dbquery.real_escape_string(table_name))
    dbquery.run_sql(create_stmt)
    
    #now retrieve the collection
    c = search_engine.get_collection_reclist(coll_name)
    if len(c) < 0:
        sys.stderr.write("The collection %s is empty!\n" % coll_name)
    
    c = list(c)
    l = len(c)
    i = 0
    sys.stderr.write("Copying bibrec data\n")
    while i < l:
        dbquery.run_sql("INSERT INTO `%s` SELECT * FROM `bibrec` WHERE bibrec.id IN (%s)" % 
                             (dbquery.real_escape_string(table_name), ','.join(map(str, c[i:i+step_size]))))
        i = i + step_size
        sys.stderr.write("%s\n" % i)

        if (maxsize and i > maxsize):
        	break
        
    sys.stderr.write("Total number of records: %s\n" % l)



def create_astro_collection():
	create_collection_bibrec("_astro_bibrec", "Astronomy", step_size=1000)

def create_astro_collection_short():	
	create_collection_bibrec("_astro_bibrec", "Astronomy", step_size=1000, maxsize=100000)