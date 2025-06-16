package com.example.demo.streams;

import com.example.demo.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;

@Configuration
@EnableKafkaStreams
public class KafkaStreamConfig {
	 private static final Logger log = LoggerFactory.getLogger(KafkaStreamConfig.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");

    

    @Bean
    public StreamsBuilderFactoryBeanConfigurer configurer() {
        return factoryBean -> {
            Map<Object, Object> props = factoryBean.getStreamsConfiguration();
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        };
    }
    public KafkaStreamConfig() {
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private boolean isValidDate(String dateStr) {
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    @Bean
    public KStream<String, Object> kStream(StreamsBuilder builder) {
        KStream<String, String> topicA = builder.stream("TOPIC_A");
        KStream<String, String> topicB = builder.stream("TOPIC_B");
        topicA.foreach((key, value) -> log.info("Received from TOPIC_A -> key={}, value={}", key, value));
        topicB.foreach((key, value) -> log.info("Received from TOPIC_B -> key={}, value={}", key, value));


        KStream<String, TopicA> parsedA = topicA.mapValues(value -> {
            try {
                return mapper.readValue(value, TopicA.class);
            } catch (Exception e) {
                return null;
            }
        }).filter((k, v) -> v != null &&
                "001".equals(v.getValue().getCountry()) &&
                v.getValue().getCatalog_number().length() == 5 &&
                isValidDate(v.getValue().getSelling_status_date()));

        KStream<String, TopicB> parsedB = topicB.mapValues(value -> {
            try {
                return mapper.readValue(value, TopicB.class);
            } catch (Exception e) {
                return null;
            }
        }).filter((k, v) -> v != null &&
                "001".equals(v.getValue().getCountry()) &&
                v.getValue().getCatalog_number().length() == 5 &&
                isValidDate(v.getValue().getSales_date()));

        KStream<String, Object> joined = parsedA.join(
                parsedB,
                (a, b) -> {
                    try {
                        return mapper.writeValueAsString(Map.of("topicA", a, "topicB", b));
                    } catch (Exception e) {
                        return null;
                    }
                },
                JoinWindows.ofTimeDifferenceWithNoGrace(java.time.Duration.ofMinutes(5)),
                StreamJoined.with(null, null, null)
        );
        log.info(joined.toString());

        joined.to("TOPIC_C");
        return joined;
    }
}