package com.example.demo.model;

public class TopicC {
	
	  private TopicAKey key;
	  private JoinedValue value;
	  private Audit audit;
	  
	  
	  public TopicC() {
		  
	  }
	  
	public TopicC(TopicAKey key, JoinedValue value, Audit audit) {
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
	public JoinedValue getValue() {
		return value;
	}
	public void setValue(JoinedValue value) {
		this.value = value;
	}
	public Audit getAudit() {
		return audit;
	}
	public void setAudit(Audit audit) {
		this.audit = audit;
	}

	  
	  
}
