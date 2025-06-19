package com.example.demo.model;

public class TopicBValue {
    private String catalog_number;
    private String order_number;
    private String quantity;
    private String sales_date;
    private String country;
    
    public TopicBValue() {
    	
    }

    public TopicBValue(String catalog_number, String order_number, String quantity, String sales_date, String country) {
		super();
		this.catalog_number = catalog_number;
		this.order_number = order_number;
		this.quantity = quantity;
		this.sales_date = sales_date;
		this.country = country;
	}
	public String getCatalog_number() {
    	return catalog_number;
    	}
    public void setCatalog_number(String catalog_number) { 
    	this.catalog_number = catalog_number; 
    	}
    public String getOrder_number() {
    	return order_number; 
    	}
    public void setOrder_number(String order_number) {
    	this.order_number = order_number;
    	}
    public String getQuantity() {
    	return quantity; 
    	}
    public void setQuantity(String quantity) { 
    	this.quantity = quantity; 
    	}
    public String getSales_date() { 
    	return sales_date;
    	}
    public void setSales_date(String sales_date) { 
    	this.sales_date = sales_date; 
    	}
    public String getCountry() {
    	return country;
    	}
    public void setCountry(String country) { 
    	this.country = country;
    	}
}