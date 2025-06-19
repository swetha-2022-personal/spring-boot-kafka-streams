package com.example.demo.model;

public class JoinedValue {

	 private TopicAValue topicAData;
	  private TopicBValue topicBData;
	  
	  public JoinedValue() {
		  
	  }
	  
	public JoinedValue(TopicAValue topicAData, TopicBValue topicBData) {
		super();
		this.topicAData = topicAData;
		this.topicBData = topicBData;
	}
	public TopicAValue getTopicAData() {
		return topicAData;
	}
	public void setTopicAData(TopicAValue topicAData) {
		this.topicAData = topicAData;
	}
	public TopicBValue getTopicBData() {
		return topicBData;
	}
	public void setTopicBData(TopicBValue topicBData) {
		this.topicBData = topicBData;
	}
	  
	  
}
