package org.apache.solr.handler.batch;



public abstract class BatchProvider implements BatchProviderI {
	
	public String name;
	
	public BatchProvider() {
		this.name = "<not-set>";
	}
	
	public BatchProvider(String name) {
		this.name = name;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}