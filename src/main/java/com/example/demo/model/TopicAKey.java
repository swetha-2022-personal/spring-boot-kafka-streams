package com.example.demo.model;



public class TopicAKey {
    private String catalog_number;
    private String country;
    
    public TopicAKey() {
    }
    
    public TopicAKey(String catalog_number, String country) {
  		super();
  		this.catalog_number = catalog_number;
  		this.country = country;
  	}
    public String getCatalog_number() {
    	return catalog_number;
    	}
  
	public void setCatalog_number(String catalog_number) { 
    	this.catalog_number = catalog_number;
    	}
    public String getCountry() {
    	return country; 
    	}
    public void setCountry(String country) {
    	this.country = country;
    	}
}