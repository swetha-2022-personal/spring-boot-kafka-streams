package com.example.demo.model;

public class TopicA {

	private TopicAKey key;
	private TopicAValue value;
	private Audit audit;
	
	
	public TopicA() {
		
	}
	
	public TopicA(TopicAKey key, TopicAValue value, Audit audit) {
		super();
		this.key = key;
		this.value = value;
		this.audit = audit;
	}
	public TopicAKey getKey() {
		return key;
	}
	public void setKey(TopicAKey key) {
		this.key = key;
	}
	public TopicAValue getValue() {
		return value;
	}
	public void setValue(TopicAValue value) {
		this.value = value;
	}
	public Audit getAudit() {
		return audit;
	}
	public void setAudit(Audit audit) {
		this.audit = audit;
	}
	
	

}
