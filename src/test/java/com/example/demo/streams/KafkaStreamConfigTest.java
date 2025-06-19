package com.example.demo.streams;

import com.example.demo.model.*;
import com.example.demo.streams.KafkaStreamConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.test.TestRecord;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KafkaStreamConfigTest {

    private TopologyTestDriver testDriver;
    private TestInputTopic<String, TopicA> inputTopicA;
    private TestInputTopic<String, TopicB> inputTopicB;
    private TestOutputTopic<String, TopicC> outputTopic;
    private KafkaStreamConfig kafkaStreamConfig = new KafkaStreamConfig();

    @BeforeEach
    void setup() {
        StreamsBuilder builder = new StreamsBuilder();
        kafkaStreamConfig.kStream1(builder);

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        testDriver = new TopologyTestDriver(builder.build(), props);

        JsonSerde<TopicA> topicASerde = new JsonSerde<>(TopicA.class);
        JsonSerde<TopicB> topicBSerde = new JsonSerde<>(TopicB.class);
        JsonSerde<TopicC> topicCSerde = new JsonSerde<>(TopicC.class);

        topicASerde.configure(Map.of("spring.json.trusted.packages", "*"), false);
        topicBSerde.configure(Map.of("spring.json.trusted.packages", "*"), false);
        topicCSerde.configure(Map.of("spring.json.trusted.packages", "*"), false);

        inputTopicA = testDriver.createInputTopic("TOPIC_A", Serdes.String().serializer(), topicASerde.serializer());
        inputTopicB = testDriver.createInputTopic("TOPIC_B", Serdes.String().serializer(), topicBSerde.serializer());
        outputTopic = testDriver.createOutputTopic("TOPIC_C", Serdes.String().deserializer(), topicCSerde.deserializer());
    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @Test
    void testValidJoinMessage() {
        String key = "12345|001";

        TopicA a = new TopicA();
        a.setKey(new TopicAKey("12345", "001"));
        a.setValue(new TopicAValue("12345", true, "ModelX", "P123", "R123", "REG001", "2025-06-18T10:00:00.0000001Z", "001"));

        TopicB b = new TopicB();
        b.setKey(new TopicAKey("12345", "001"));
        b.setValue(new TopicBValue("12345", "O1001", "10", "2025-06-18T10:01:00.0000001Z", "001"));

        inputTopicA.pipeInput(key, a, Instant.parse("2025-06-18T10:00:00Z"));
        inputTopicB.pipeInput(key, b, Instant.parse("2025-06-18T10:01:00Z"));

        List<TestRecord<String, TopicC>> results = outputTopic.readRecordsToList();
        assertEquals(1, results.size());

        TopicC joined = results.get(0).getValue();
        assertEquals("JOINED", joined.getAudit().getEvent_name());
        assertEquals("12345", joined.getKey().getCatalog_number());
    }

    @Test
    void testInvalidCatalogNumber() {
        String key = "12X45|001"; 

        TopicA a = new TopicA();
        a.setKey(new TopicAKey("12X45", "001"));
        a.setValue(new TopicAValue("12X45", true, "ModelY", "P999", "R999", "REG999", "2025-06-18T10:00:00.0000001Z", "001"));

        inputTopicA.pipeInput(key, a);
        assertTrue(outputTopic.isEmpty());
    }
}
