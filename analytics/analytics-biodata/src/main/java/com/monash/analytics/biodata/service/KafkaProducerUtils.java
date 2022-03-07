package com.monash.analytics.biodata.service;

import com.monash.analytics.biodata.constant.ConstantValues;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.UUID;


/**
 * Kafka operation utils
 * @author Xinyu Li
 */

public class KafkaProducerUtils {

    /**
     * create producer and setup all the configurations
     * @return
     */
    public static Producer<String, String> createProducer() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, ConstantValues.KAFKA_SERVER);
        properties.setProperty("security.protocol", "SASL_SSL");
//        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule   required username='6CINJP7JL3B3JPMG'   password='zBLiTuMlgEoPsv9/4uw39cQ0gvAoF/9FID33oytohydMJf99lYh9YuBHixKiFm7t';");
        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule   required username='" + ConstantValues.KAFKA_API_KEY + "'   password='" + ConstantValues.KAFKA_API_SECRET + "';");
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

    /**
     * use producer and send data to kafka
     * @param producer
     * @param message
     * @param deviceId
     * @param sessionId
     */
    public static void producerSend(Producer<String, String> producer, String message, String deviceId, String sessionId) {
        //ProducerRecord
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(ConstantValues.EMPATICA_TOPIC_NAME, "key-" + sessionId + "-" + UUID.randomUUID(), "empatica-" + deviceId + "-" + message);
        producer.send(record);
    }

    /**
     * close producer
     * @param producer
     */
    public static void producerClose(Producer<String, String> producer) {
        if (producer != null) {
            producer.close();
        }
    }
}
