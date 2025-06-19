package com.example.demo.model;

public class TopicB {

	private TopicAKey key;
	private TopicBValue value;
	private Audit audit;
	
	public TopicB() {
		
	}
	
	public TopicAKey getKey() {
		return key;
	}
	public void setKey(TopicAKey key) {
		this.key = key;
	}
	public TopicBValue getValue() {
		return value;
	}
	public void setValue(TopicBValue value) {
		this.value = value;
	}
	public Audit getAudit() {
		return audit;
	}
	public void setAudit(Audit audit) {
		this.audit = audit;
	}
	public TopicB(TopicAKey key, TopicBValue value, Audit audit) {
		super();
		this.key = key;
		this.value = value;
		this.audit = audit;
	}
	
	
}

