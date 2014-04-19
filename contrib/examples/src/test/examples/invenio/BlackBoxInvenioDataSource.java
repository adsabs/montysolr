package examples.invenio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URLEncoder;

import monty.solr.util.MontySolrSetup;

import org.apache.solr.handler.dataimport.InvenioDataSource;
import org.apache.solr.update.InvenioDB;
import org.junit.BeforeClass;

import examples.BlackAbstractLuceneTestCase;

public class BlackBoxInvenioDataSource extends BlackAbstractLuceneTestCase {
	
	public void tearDown() throws Exception {
	  InvenioDB.INSTANCE.close();
	  super.tearDown();
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
		
		// the query must be URLEncoded (double, if you look at it from the web)
		r = handler.getData("python://whatever.here/there?p=" + java.net.URLEncoder.encode("recid:1744593->1745994", "UTF-8"));
		result = convertToString(r);
		assert !result.contains("<controlfield tag=\"001\">");
		assert result.contains("</collection>");
		
		r = handler.getData("python://whatever.here/there?p=" + java.net.URLEncoder.encode("recid:1744593->1745994 OR recid:56->58", "UTF-8"));
    result = convertToString(r);
    assert result.contains("<controlfield tag=\"001\">56</controlfield>");
    
    //System.out.println(URLEncoder.encode("python://whatever.here/there?p=" + java.net.URLEncoder.encode("recid:1744593->1745994", "UTF-8")));
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
