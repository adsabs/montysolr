package examples.invenio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URLEncoder;

import org.apache.solr.handler.dataimport.InvenioDataSource;
import org.junit.BeforeClass;

import examples.BlackAbstractLuceneTestCase;
import invenio.montysolr.util.MontySolrSetup;

public class BlackBoxInvenioDataSource extends BlackAbstractLuceneTestCase {
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		setEName("invenio");
		exampleInit();
		MontySolrSetup.addTargetsToHandler("monty_invenio.targets");
	}
	
	public void testImport() throws IOException {
		InvenioDataSource handler = new InvenioDataSource();
		Reader r;
		String result;
		
		r = handler.getData("python://whatever.here/there?p=recid%3A94&of=xm");
		result = convertToString(r);
		assert result.contains("<controlfield tag=\"001\">94</controlfield>");
		
		r = handler.getData("python://whatever.here/there?p=" + 
				URLEncoder.encode("recid:94->95", "UTF-8") + "&of=xm");
		result = convertToString(r);
		assert result.contains("<controlfield tag=\"001\">94</controlfield>");
		assert result.contains("<controlfield tag=\"001\">95</controlfield>");
		
		r = handler.getData("python://whatever.here/there?p=" + 
				URLEncoder.encode("recid:0->95", "UTF-8") + "&of=xm&rg=200");
		result = convertToString(r);
		assert result.split("<record>").length > 50;
		assert result.contains("<controlfield tag=\"001\">94</controlfield>");
		assert result.contains("<controlfield tag=\"001\">95</controlfield>");
	}
	
	
	
	public String convertToString(Reader r) throws IOException {
		StringBuffer result = new StringBuffer();
		BufferedReader br = new BufferedReader(r);
		String s = br.readLine();
		
		while (s != null) {
			result.append(s);
			s = br.readLine();
		}
		return result.toString();
	}

	
}
