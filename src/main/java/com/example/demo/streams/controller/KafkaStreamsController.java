package com.example.demo.streams.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.TopicA;
import com.example.demo.model.TopicB;
import com.example.demo.service.KafkaProducerService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/produce")
public class KafkaStreamsController {
	
	/*
	 * @Autowired KafkaProducerService producer;
	 * 
	 * @PostMapping(path="/topicA") String produceTopicAData(@RequestBody TopicA
	 * topicA) { producer.sendMessage("TOPIC_A", topicA.toString());
	 * 
	 * return "success";
	 * 
	 * }
	 * 
	 * @PostMapping(path="/topicB") String produceTopicBData(@RequestBody TopicB
	 * topicB) {
	 * 
	 * producer.sendMessage("TOPIC_B",topicB.toString());
	 * 
	 * return "succsess";
	 * 
	 * }
	 */
	
	

}
