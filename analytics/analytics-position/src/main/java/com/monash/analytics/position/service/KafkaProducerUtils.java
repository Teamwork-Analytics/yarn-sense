package com.monash.analytics.position.service;

import com.monash.analytics.position.constant.ConstantValues;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

public class KafkaProducerUtils {


    public static Producer<String, String> createProducer() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, ConstantValues.KAFKA_SERVER);
        properties.setProperty("security.protocol", "SASL_SSL");
//        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule   required username='6CINJP7JL3B3JPMG'   password='zBLiTuMlgEoPsv9/4uw39cQ0gvAoF/9FID33oytohydMJf99lYh9YuBHixKiFm7t';");
        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule   required username='"
                + ConstantValues.KAFKA_API_KEY + "'   password='" + ConstantValues.KAFKA_API_SECRET + "';");
        properties.setProperty("sasl.mechanism", "PLAIN");
//        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, "0");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");


        Producer<String, String> producer = new KafkaProducer<>(properties);
        return producer;
    }

    public static void producerSend(Producer<String, String> producer, String message, String sessionId) {
        //ProducerRecord
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(ConstantValues.POSITION_TOPIC_NAME, "key-"
                + sessionId + "-" + UUID.randomUUID(), "position-" + message);
        producer.send(record);
    }

    public static void producerClose(Producer<String, String> producer) {
        if (producer != null) {
            producer.close();
        }
    }
}
