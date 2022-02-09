package com.monash.analytics.position.service;

import com.monash.analytics.position.constant.ConstantValues;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

@Slf4j
public class KafkaConsumerUtils {
    public static void commitOffset() {
        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, ConstantValues.KAFKA_SERVER);
        props.setProperty("security.protocol", "SASL_SSL");
        props.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule   required username='"
                + ConstantValues.KAFKA_API_KEY + "'   password='" + ConstantValues.KAFKA_API_SECRET + "';");
        props.setProperty("sasl.mechanism", "PLAIN");
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "false");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", StringDeserializer.class.getName());
        props.setProperty("value.deserializer", StringDeserializer.class.getName());
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        TopicPartition topicPartition0 = new TopicPartition("empatica_data", 0);
        consumer.assign(Arrays.asList(topicPartition0));
        consumer.seek(topicPartition0, 0);
        StringBuilder sb = new StringBuilder();
        while (true) {
            log.info("into while");
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));

            log.info("get records");
            for (ConsumerRecord<String, String> record : records) {

                sb.append(String.format("partition = %d, offset = %d, key = %s, value = %s%n",
                        record.partition(), record.offset(), record.key(), record.value()));
                if (sb.length() > 10000) {
                    try {
                        FileUtils.writeStringToFile(new File("C:\\develop\\saved_data\\kafka_comsuer.txt"), sb.toString(), "UTF-8", true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sb.delete(0, sb.length());

                }
            }
            consumer.commitAsync();
            if (records.isEmpty()) {
                break;
            }
        }
        try {
            FileUtils.writeStringToFile(new File("C:\\develop\\saved_data\\kafka_comsuer.txt"), sb.toString(), "UTF-8", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("finished");

    }

    public static void controlOffset(String topicName) {
        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, ConstantValues.KAFKA_SERVER);
        props.setProperty("security.protocol", "SASL_SSL");
        props.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule   required username='"
                + ConstantValues.KAFKA_API_KEY + "'   password='" + ConstantValues.KAFKA_API_SECRET + "';"); //zBLiTuMlgEoPsv9/4uw39cQ0gvAoF/9FID33oytohydMJf99lYh9YuBHixKiFm7t
        props.setProperty("sasl.mechanism", "PLAIN");
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "false");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", StringDeserializer.class.getName());
        props.setProperty("value.deserializer", StringDeserializer.class.getName());
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        TopicPartition topicPartition0 = new TopicPartition(topicName, 0);

        consumer.assign(Arrays.asList(topicPartition0));

        consumer.seek(topicPartition0, 0);
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100000));
        for (TopicPartition partition : records.partitions()) {
            List<ConsumerRecord<String, String>> pRecords = records.records(partition);
            for (ConsumerRecord<String, String> record : pRecords) {

                System.err.printf("partition = %d, offset = %d, key = %s, value = %s%n",
                        record.partition(), record.offset(), record.key(), record.value());

            }
            long lastOffset = pRecords.get(pRecords.size() - 1).offset();
            Map<TopicPartition, OffsetAndMetadata> offsetAndMetadataMap = new HashMap<>();

            offsetAndMetadataMap.put(partition, new OffsetAndMetadata(lastOffset + 1));

            consumer.commitSync(offsetAndMetadataMap);
            log.info("==========partition - :" + partition + "==============");
        }
    }
}
