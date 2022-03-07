package com.monash.analytics.position.service;

import org.springframework.stereotype.Service;

/**
 * test Kafka services APIs implementation
 * @author Xinyu Li
 */

@Service
public class TestServiceImpl implements TestServiceAPI {
    public void testConsumer() {
        KafkaConsumerUtils.commitOffset();
    }

    public void testCreateTopics() {
        System.out.println("---------------------topic list-------------------------");
        try {
            KafkaAdminUtils.topicLists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("---------------------topic list end-------------------------");
        String positionTopicName = "position_data_{device_id}";
        String bioDataTopicName = "empatica_data_{deviceId}";
        String[] positionDeviceArray = {"", "", "", "", ""};
        String[] empaticaDeviceArray = {"3b9efd", "a39bfd", "379ef"};
        for (String deviceId : positionDeviceArray) {
            KafkaAdminUtils.createTopic("position_data_" + deviceId);
        }
        for (String deviceId : empaticaDeviceArray) {
            KafkaAdminUtils.createTopic("empatica_data_" + deviceId);
        }
//        KafkaAdminUtils.createTopic(positionTopicName);
//        KafkaAdminUtils.createTopic(bioDataTopicName);
        System.out.println("---------------------topic list-------------------------");
        try {
            KafkaAdminUtils.topicLists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("---------------------topic list end-------------------------");
    }

    public void testGetTopicList() {
        System.out.println("---------------------topic list-------------------------");
        try {
            KafkaAdminUtils.topicLists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("---------------------topic list end-------------------------");
    }
}
