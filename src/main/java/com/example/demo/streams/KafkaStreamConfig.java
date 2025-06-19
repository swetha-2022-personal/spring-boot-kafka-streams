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
import org.springframework.kafka.support.serializer.JsonSerde;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Map;
import java.util.TimeZone;

@Configuration
@EnableKafkaStreams
public class KafkaStreamConfig {
    private static final Logger log = LoggerFactory.getLogger(KafkaStreamConfig.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");

    public KafkaStreamConfig() {
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Bean
    public StreamsBuilderFactoryBeanConfigurer configurer() {
        return factoryBean -> {
            Map<Object, Object> props = factoryBean.getStreamsConfiguration();
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

            // ✅ Handle deserialization errors without shutting down
            props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                      org.apache.kafka.streams.errors.LogAndContinueExceptionHandler.class);

            // ✅ Optional: continue on production errors (output topic issues)
            props.put(StreamsConfig.DEFAULT_PRODUCTION_EXCEPTION_HANDLER_CLASS_CONFIG,
                      org.apache.kafka.streams.errors.DefaultProductionExceptionHandler.class);
        };
    }

    private boolean isValidMessageA(TopicA msg) {
        if (msg == null || msg.getKey() == null || msg.getValue() == null) {
            log.warn("Invalid TopicA structure: {}", msg);
            return false;
        }
        if (!"001".equals(msg.getKey().getCountry())) {
            log.warn("Invalid TopicA country: {}", msg.getKey().getCountry());
            return false;
        }
        if (!isValidDate(msg.getValue().getSelling_status_date())) {
            log.warn("Invalid TopicA date: {}", msg.getValue().getSelling_status_date());
            return false;
        }
        if (!msg.getKey().getCatalog_number().matches("\\d{5}")) {
            log.warn("Invalid TopicA catalog_number: {}", msg.getKey().getCatalog_number());
            return false;
        }
        return true;
    }

    private boolean isValidMessageB(TopicB msg) {
        if (msg == null || msg.getKey() == null || msg.getValue() == null) {
            log.warn("Invalid TopicB structure: {}", msg);
            return false;
        }
        if (!"001".equals(msg.getKey().getCountry())) {
            log.warn("Invalid TopicB country: {}", msg.getKey().getCountry());
            return false;
        }
        if (!isValidDate(msg.getValue().getSales_date())) {
            log.warn("Invalid TopicB date: {}", msg.getValue().getSales_date());
            return false;
        }
        if (!msg.getKey().getCatalog_number().matches("\\d{5}")) {
            log.warn("Invalid TopicA catalog_number: {}", msg.getKey().getCatalog_number());
            return false;
        }
        return true;
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
    public KStream<String, TopicC> kStream1(StreamsBuilder builder) {
        KStream<String, TopicA> streamA = builder
            .stream("TOPIC_A", Consumed.with(Serdes.String(), jsonSerde(TopicA.class)))
            .filter((k, v) -> isValidMessageA(v))
            .selectKey((k, v) -> v.getKey().getCatalog_number() + "|" + v.getKey().getCountry());

        KStream<String, TopicB> streamB = builder
            .stream("TOPIC_B", Consumed.with(Serdes.String(), jsonSerde(TopicB.class)))
            .filter((k, v) -> isValidMessageB(v))
            .selectKey((k, v) -> v.getKey().getCatalog_number() + "|" + v.getKey().getCountry());

        streamA.foreach((k, v) -> log.info(" Filtered A -> key={}, value={}", k, v));
        streamB.foreach((k, v) -> log.info(" Filtered B -> key={}, value={}", k, v));

        KStream<String, TopicC> joined = streamA.join(
            streamB,
            this::joinMessages,
            JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(5)),
            StreamJoined.with(
                Serdes.String(),
                jsonSerde(TopicA.class),
                jsonSerde(TopicB.class))
        );

        joined.foreach((key, value) -> log.info(" Joined -> key={}, value={}", key, value));
        joined.to("TOPIC_C", Produced.with(Serdes.String(), jsonSerde(TopicC.class)));

        return joined;
    }

    private TopicC joinMessages(TopicA a, TopicB b) {
        TopicC c = new TopicC();
        c.setKey(a.getKey());

        JoinedValue j = new JoinedValue();
        j.setTopicAData(a.getValue());
        j.setTopicBData(b.getValue());
        c.setValue(j);

        Audit audit = new Audit();
        audit.setEvent_name("JOINED");
        audit.setSource_system("stream-app");
        c.setAudit(audit);

        return c;
    }

    private <T> JsonSerde<T> jsonSerde(Class<T> clazz) {
        JsonSerde<T> serde = new JsonSerde<>(clazz);
        serde.configure(Map.of("spring.json.trusted.packages", "*"), false);
        return serde;
    }
}
