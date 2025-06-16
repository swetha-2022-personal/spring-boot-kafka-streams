package com.example.demo.streams.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.TopicA;
import com.example.demo.model.TopicB;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/produce")
public class KafkaStreamsController {
	
	@PostMapping(path="/topicA")
	String produceTopicAData(@RequestBody TopicA topicA) {
		return null;
		
	}
	
	@PostMapping(path="/topicB")
	String produceTopicBData(@RequestBody TopicB topicb) {
		return null;
		
	}
	
	

}
