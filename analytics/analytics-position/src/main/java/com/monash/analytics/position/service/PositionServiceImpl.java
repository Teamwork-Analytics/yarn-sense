package com.monash.analytics.position.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.ImmutableMap;
import com.monash.analytics.position.constant.ConstantValues;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * position data collection service APIs implementation
 * @author Xinyu Li
 */

@Slf4j
@Service
public class PositionServiceImpl implements PositionServiceAPI{
    private MqttClient sampleClient = null;
    private List<String> messageList = null;

//    private Producer<String, String> producer = null; // not use kafka anymore

    /**
     * start get position data from the pozyx server
     * @param destPath
     * @param sessionId
     * @throws Exception
     */
    @Override
    public void startRecordingPosition(String destPath, String sessionId) throws Exception {
        messageList = new ArrayList<>();
        String topic        = "tags";
        String content      = "Message from MqttPublishSample";
        int qos             = 2;
        String broker       = "tcp://" + ConstantValues.LOCAL_SERVER_NAME + ":1883";
        String clientId     = "JavaSample"; //TODO 需测试
        MemoryPersistence persistence = new MemoryPersistence();
//        producer = KafkaProducerUtils.createProducer(); // not use kafka anymore

        sampleClient = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        log.info("Connecting to broker: "+broker);
        sampleClient.connect(connOpts);
        log.info("Connected");
        DateTime dt = new DateTime();
        String time = dt.toString("yyyy-MM-dd_HH-mm-ss-SSS");
        FileUtils.writeStringToFile(new File(destPath + "sync.txt"), "start receive position data _____" + time + "\n", "UTF-8", true);

        sampleClient.setCallback(new MqttCallback() {
            public void connectionLost(Throwable cause) {
                log.info("connection lost for position server" + cause.toString());
                try {
                    sampleClient.reconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }


            //[{"version":"1","alive":true,"tagId":"27226","success":true,"timestamp":1624425365.374,"data":{"tagData":{},"anchorData":[],"coordinates":{"x":1201,"y":954,"z":375},"orientation":{"yaw":1.775,"roll":0.044,"pitch":0.017},"metrics":{"latency":46.9,"rates":{"update":9.545,"success":9.095}}}}]
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                messageList.add(message.toString());
//                KafkaProducerUtils.producerSend(producer, message.toString(), sessionId);// not use kafka anymore
                if (messageList.size() >= 1000) {
                    saveDataToFile(destPath);
                }

            }

            public void deliveryComplete(IMqttDeliveryToken token) {
                log.info("deliveryComplete-------" + token.isComplete());
            }
        });

        sampleClient.subscribe(topic);
    }

    /**
     * stop recording position data
     * @param destPath
     * @throws Exception
     */
    @Override
    public void stopRecordingPosition(String destPath) throws Exception {
        if (sampleClient != null) {
            DateTime dt = new DateTime();
            String time = dt.toString("yyyy-MM-dd_HH-mm-ss-SSS");
            FileUtils.writeStringToFile(new File(destPath + "sync.txt"), "stop receive position data _____" + time + "\n", "UTF-8", true);

            saveDataToFile(destPath);
            sampleClient.disconnectForcibly();
            sampleClient.close();
            log.info("force stop successfully");
//            KafkaProducerUtils.producerClose(producer);// not use kafka anymore

            String sessionId = destPath.substring(destPath.length() - 4, destPath.length() - 1);
            File copied = new File(
                    ConstantValues.FILE_SAVE_PATH + sessionId + ".json");  //for Gloria program, not used
            FileUtils.copyFile(new File(destPath + sessionId + ".json"), copied);
        }
    }

    /**
     * test method
     * @throws Exception
     */
    @Override
    public void testRecordingPosition() throws Exception {
        String topic        = "tags";
        String broker       = "tcp://localhost:1883";
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();
//        producer = KafkaProducerUtils.createProducer();
//        try {
        sampleClient = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        log.info("Connecting to broker: "+broker);
        sampleClient.connect(connOpts);

        log.info("Connected");
//            System.out.println("Publishing message: "+content);
        sampleClient.setCallback(new MqttCallback() {
            public void connectionLost(Throwable cause) {
                log.info("connection lost");
            }


            //[{"version":"1","alive":true,"tagId":"27226","success":true,"timestamp":1624425365.374,"data":{"tagData":{},"anchorData":[],"coordinates":{"x":1201,"y":954,"z":375},"orientation":{"yaw":1.775,"roll":0.044,"pitch":0.017},"metrics":{"latency":46.9,"rates":{"update":9.545,"success":9.095}}}}]
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                log.info("Message: " + message.toString());

                log.info(JSON.parseArray(message.toString()).getJSONObject(0).getString("tagId"));
                log.info(ConstantValues.kafkaNameMap.get(JSON.parseArray(message.toString()).getJSONObject(0).getString("tagId")));
            }

            public void deliveryComplete(IMqttDeliveryToken token) {
                log.info("deliveryComplete-------" + token.isComplete());
            }
        });

        log.info("before subscribe");
        sampleClient.subscribe(topic);
        while(true){}
    }

    /**
     * save position data to local files
     * @param destPath
     * @throws IOException
     */
    private void saveDataToFile(String destPath) throws IOException {
        if (messageList != null && !messageList.isEmpty()) {
            String sessionId = destPath.substring(destPath.length() - 4, destPath.length() - 1);
            log.error(sessionId);
            FileUtils.writeLines(new File(destPath + sessionId + ".json"), messageList, true);
            log.info("save position data to file:" + destPath + sessionId + ".json");

            messageList.clear();
        }
    }



}
