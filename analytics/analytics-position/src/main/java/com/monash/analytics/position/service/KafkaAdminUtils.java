package com.monash.analytics.position.service;

import com.monash.analytics.position.constant.ConstantValues;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Kafka admin utils
 * @author Xinyu Li
 */

@Slf4j
public class KafkaAdminUtils {

    /**
     * create admin client and setup configurations
     * @return
     */
    public static AdminClient adminClient() {
        Properties properties = new Properties();
        properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, ConstantValues.KAFKA_SERVER);
        properties.setProperty(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
//        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule   required username='6CINJP7JL3B3JPMG'   password='zBLiTuMlgEoPsv9/4uw39cQ0gvAoF/9FID33oytohydMJf99lYh9YuBHixKiFm7t';");
        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule   required username='" + ConstantValues.KAFKA_API_KEY + "'   password='" + ConstantValues.KAFKA_API_SECRET + "';");

        properties.setProperty("sasl.mechanism", "PLAIN");

        AdminClient adminClient = AdminClient.create(properties);

        return adminClient;
    }

    /**
     * show all the topics
     * @throws Exception
     */
    public static void topicLists() throws Exception{
        AdminClient adminClient = adminClient();
        ListTopicsOptions options = new ListTopicsOptions();
        options.listInternal(true);
//        ListTopicsResult listTopicsResult = adminClient.listTopics();
        ListTopicsResult listTopicsResult = adminClient.listTopics(options);
        Set<String> names = listTopicsResult.names().get();
        Collection<TopicListing> topicListings = listTopicsResult.listings().get();

        names.forEach(System.out::println);

        topicListings.forEach(System.out::println);
    }

    /**
     * create topics
     * @param topicName
     */
    public static void createTopic(String topicName) {

        AdminClient adminClient = adminClient();
        NewTopic newTopic = new NewTopic(topicName, 1, (short)3);
        CreateTopicsResult topicsResult = adminClient.createTopics(Collections.singletonList(newTopic));

        try {
            topicsResult.all().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        log.info("Createtopic result:" + topicsResult);


    }
}
