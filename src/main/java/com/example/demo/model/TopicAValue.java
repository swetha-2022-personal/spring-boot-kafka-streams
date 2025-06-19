package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class TopicAValue {
    private String catalog_number;
    private boolean is_selling;
    private String model;
    private String product_id;
    private String registration_id;
    private String registration_number;
    private String selling_status_date;
    private String country;

    public TopicAValue() {
    	
    }
    

	public TopicAValue(String catalog_number, boolean is_selling, String model, String product_id,
			String registration_id, String registration_number, String selling_status_date, String country) {
		super();
		this.catalog_number = catalog_number;
		this.is_selling = is_selling;
		this.model = model;
		this.product_id = product_id;
		this.registration_id = registration_id;
		this.registration_number = registration_number;
		this.selling_status_date = selling_status_date;
		this.country = country;
	}
	public String getCatalog_number() { 
    	return catalog_number; }
    public void setCatalog_number(String catalog_number) {
    	this.catalog_number = catalog_number;
    	}
    public boolean isIs_selling() {
    	return is_selling; 
    	}
    public void setIs_selling(boolean is_selling) {
    	this.is_selling = is_selling;
    	}
    public String getModel() {
    	return model;
    	}
    public void setModel(String model) {
    	this.model = model; 
    	}
    public String getProduct_id() { 
    	return product_id;
    	}
    public void setProduct_id(String product_id) {
    	this.product_id = product_id; 
    	}
    public String getRegistration_id() { 
    	return registration_id;
    	}
    public void setRegistration_id(String registration_id) { 
    	this.registration_id = registration_id;
    	}
    public String getRegistration_number() {
    	return registration_number;
    	}
    public void setRegistration_number(String registration_number) { 
    	this.registration_number = registration_number; 
    	}
    public String getSelling_status_date() { 
    	return selling_status_date; 
    	}
    public void setSelling_status_date(String selling_status_date) { 
    	this.selling_status_date = selling_status_date;
    	}
    public String getCountry() { 
    	return country; 
    	}
    public void setCountry(String country) {
    	this.country = country;
    	}
}