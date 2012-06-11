package org.adsabs.solr.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.*;

import org.apache.solr.analysis.SynonymFilterFactory;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteableSynonymMap {
	
    public static final Logger log = LoggerFactory.getLogger(WriteableSynonymMap.class);
	
    private HashMap<String, List<String>> map;
	private int numUpdates = 0;
	private String outFile = null;
	
	public WriteableSynonymMap(String outFile) {
		this.map = new HashMap<String, List<String>>();
		this.outFile = outFile;
	}
	
	public void setOutput(String out) {
		this.outFile = out;
	}
	
	public List<String> put(String k, List<String> v) {
		log.trace("setting " + k + " to " + v);
		numUpdates++;
		return this.map.put(k, v);
	}
	
	public List<String> get(String k) {
		return this.map.get(k);
	}
	
	public List<String> get(Pattern p) {
		for (String k : this.map.keySet()) {
			Matcher m = p.matcher(k);
			if (m.matches()) {
				return this.map.get(k);
			}
		}
		return null;
	}
	
	public boolean containsKey(String key) {
		return this.map.containsKey(key);
	}
	
	public boolean isDirty() {
		if (numUpdates > 0) 
			return true;
		return false;
	}
	
	public void persist() throws IOException {
		persist(true);
	}
	
	public void persist(boolean append) throws IOException {
		Writer writer = getWriter(append);
		if (writer == null) {
			log.error("Cannot write synonyms, writer object is null.");
			return;
		}
		
		writeSynonyms(map, writer);
		numUpdates = 0;
		writer.close();
	}
	
	public Writer getWriter(boolean append) {
		if (outFile == null)
			return null;
		
		log.info("Creating new Writer for " + outFile);
		Writer w;
		
		Charset UTF_8 = Charset.forName("UTF-8");
		try {
			w = new OutputStreamWriter(new FileOutputStream(this.outFile, append), UTF_8);
			//w = new BufferedWriter(w);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} 
		
		return w;
	}
	
	
	public void writeSynonyms(Map<String, List<String>> map, Writer writer) {
		StringBuffer out = new StringBuffer();
		int max = 1000;
		int i = 0;
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			/*
			for (String s : entry.getValue()) { // escape the entry
				out.append(s.replace(",", "\\,").replace(" ", "\\ "));
				out.append(",");
			}
			out.append(entry.getKey().replace(",", "\\,").replace(" ", "\\ "));
			out.append("\n");
			*/
			
			// I don't like this format, but SynonymFilter is using it, so 
			// let's be compliant...
			
			out.append(entry.getKey().replace(",", "\\,").replace(" ", "\\ "));
			out.append("=>");
			for (String s : entry.getValue()) {
				out.append(s.replace(",", "\\,").replace(" ", "\\ "));
				out.append(",");
			}
			out.append("\n");
			
			i++;
			if (i > max) {
				i = 0;
				write(writer, out.toString());
				out = new StringBuffer();
			}
		}
		write(writer, out.toString());
	}
	
	private void write(Writer writer, String out) {
		try {
			synchronized(writer) {
				writer.write(out);
				writer.flush();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void finalize() {
		if (isDirty())
			try {
				this.persist();
			} catch (IOException e) {
				log.error(e.getLocalizedMessage());
			}
		
	}
	
	/*
	 * this is much simplified version of synonym rules that
	 * supports:
	 * 
	 * token=>token,token\\ tokenb,token
	 */
	public void parseRules(List<String> rules) {
		for (String rule : rules) {
			List<String> mapping = StrUtils.splitSmart(rule, "=>", false);
		    if (mapping.size() != 2) 
		    	log.error("Invalid Synonym Rule:" + rule);
		    String key = mapping.get(0).trim();
		    List<String> values = getSynList(mapping.get(1));
		    this.map.put(key.replace("\\,", ",").replace("\\ ", " "), values);
		}
	}
	
	private List<String> getSynList(String synonyms) {
		List<String> list = new ArrayList<String>();
		for (String s : StrUtils.splitSmart(synonyms, ",", false)) {
			list.add(s.trim().replace("\\,", ",").replace("\\ ", " "));
		}
		return list;
	}
	
	/**
	 * This is just a helper method, you should be using SolrResouceLoader#getLines()
	 * instead
	 * 
	 * @param inputFile
	 * @return
	 * @throws IOException
	 */
	public List<String> getLines(String inputFile) throws IOException {
		ArrayList<String> lines;
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(inputFile)),
					Charset.forName("UTF-8")));

			lines = new ArrayList<String>();
			for (String word = null; (word = input.readLine()) != null;) {
				// skip initial bom marker
				if (lines.isEmpty() && word.length() > 0
						&& word.charAt(0) == '\uFEFF')
					word = word.substring(1);
				// skip comments
				if (word.startsWith("#"))
					continue;
				word = word.trim();
				// skip blank lines
				if (word.length() == 0)
					continue;
				lines.add(word);
			}
		} catch (CharacterCodingException ex) {
			throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,
					"Error loading resource (wrong encoding?): " + inputFile,
					ex);
		} finally {
			if (input != null)
				input.close();
		}
		return lines;

	}
	
}
