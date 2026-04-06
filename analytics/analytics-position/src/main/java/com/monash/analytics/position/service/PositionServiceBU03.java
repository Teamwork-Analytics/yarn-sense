package com.monash.analytics.position.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.monash.analytics.position.constant.ConstantValues;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BU03 UWB sensor data collection service implementation Records distance data
 * from BU03 UWB sensors via MQTT
 *
 * Subscribes to: - uwb/raw (Pi 5 direct USB connection) - uwb/+/raw (Pico 2 W
 * Wi-Fi zones)
 *
 * Data format from uwb/raw: { "tags": { "tag0": {"base0": 1.2, "base1": 3.4},
 * "tag1": {"base0": 2.1, "base1": 4.2} }, "timestamp": "2026-04-06T10:30:45",
 * "num_tags": 2 }
 *
 * Data format from uwb/zone/raw: { "zone": "zoneA", "distances": {"tag0": 1.2,
 * "tag1": 3.4} }
 *
 * @author Generated for BU03 UWB sensor integration
 */
@Slf4j
@Service
public class PositionServiceBU03 implements PositionServiceAPI {

    private MqttClient mqttClient = null;
    private List<String> messageList = null;

    // MQTT topics for BU03 UWB sensors
    private static final String TOPIC_UWB_RAW = "uwb/raw";           // Pi 5 direct
    private static final String TOPIC_UWB_ZONES = "uwb/+/raw";       // Pico zones
    private static final String BU03_SUFFIX = "-bu03.json";          // File suffix for BU03 data

    /**
     * Start recording BU03 UWB sensor data Subscribes to both uwb/raw and
     * uwb/+/raw topics
     *
     * @param destPath Destination path for saving data files
     * @param sessionId Session identifier for this recording session
     * @throws Exception If connection or subscription fails
     */
    @Override
    public void startRecordingPosition(String destPath, String sessionId) throws Exception {
        messageList = new ArrayList<>();

        int qos = 2;
        String broker = "tcp://" + ConstantValues.LOCAL_SERVER_NAME + ":1883";
        String clientId = "BU03-UWB-Recorder-" + sessionId;
        MemoryPersistence persistence = new MemoryPersistence();

        mqttClient = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setAutomaticReconnect(true);
        connOpts.setConnectionTimeout(30);
        connOpts.setKeepAliveInterval(60);

        log.info("BU03 UWB: Connecting to broker: " + broker);
        mqttClient.connect(connOpts);
        log.info("BU03 UWB: Connected");

        DateTime dt = new DateTime();
        String time = dt.toString("yyyy-MM-dd_HH-mm-ss-SSS");
        FileUtils.writeStringToFile(
                new File(destPath + "sync.txt"),
                "start receive BU03 UWB data _____" + time + "\n",
                "UTF-8",
                true
        );

        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                log.warn("BU03 UWB: Connection lost - " + cause.getMessage());
                try {
                    if (!mqttClient.isConnected()) {
                        log.info("BU03 UWB: Attempting to reconnect...");
                        mqttClient.reconnect();
                    }
                } catch (MqttException e) {
                    log.error("BU03 UWB: Reconnection failed", e);
                }
            }

            /**
             * Process incoming UWB distance messages Handles both Pi 5 direct
             * (uwb/raw) and Pico zone (uwb/zone/raw) formats
             */
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                try {
                    String payload = message.toString();

                    // Add metadata to the message
                    JSONObject enrichedMessage = new JSONObject();
                    enrichedMessage.put("topic", topic);
                    enrichedMessage.put("received_at", new DateTime().toString("yyyy-MM-dd'T'HH:mm:ss.SSS"));
                    enrichedMessage.put("session_id", sessionId);

                    // Parse and merge the original payload
                    JSONObject originalPayload = JSON.parseObject(payload);
                    enrichedMessage.put("data", originalPayload);

                    // Determine data source
                    String source = topic.equals(TOPIC_UWB_RAW) ? "pi5_usb" : "pico_wifi";
                    enrichedMessage.put("source", source);

                    messageList.add(enrichedMessage.toJSONString());

                    // Save to file every 1000 messages to prevent memory overflow
                    if (messageList.size() >= 1000) {
                        saveDataToFile(destPath, sessionId);
                    }

                    // Log progress periodically
                    if (messageList.size() % 100 == 0) {
                        log.debug("BU03 UWB: Buffered {} messages from topic: {}",
                                messageList.size(), topic);
                    }

                } catch (Exception e) {
                    log.error("BU03 UWB: Error processing message from topic " + topic, e);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                log.debug("BU03 UWB: Delivery complete - " + token.isComplete());
            }
        });

        // Subscribe to both topic patterns
        mqttClient.subscribe(TOPIC_UWB_RAW, qos);
        log.info("BU03 UWB: Subscribed to topic: " + TOPIC_UWB_RAW);

        mqttClient.subscribe(TOPIC_UWB_ZONES, qos);
        log.info("BU03 UWB: Subscribed to topic: " + TOPIC_UWB_ZONES);
    }

    /**
     * Stop recording BU03 UWB sensor data Saves any remaining buffered data and
     * closes MQTT connection
     *
     * @param destPath Destination path where data was being saved
     * @throws Exception If disconnection or file save fails
     */
    @Override
    public void stopRecordingPosition(String destPath) throws Exception {
        if (mqttClient != null && mqttClient.isConnected()) {
            DateTime dt = new DateTime();
            String time = dt.toString("yyyy-MM-dd_HH-mm-ss-SSS");
            FileUtils.writeStringToFile(
                    new File(destPath + "sync.txt"),
                    "stop receive BU03 UWB data _____" + time + "\n",
                    "UTF-8",
                    true
            );

            // Extract sessionId from path (last 3 characters before trailing slash)
            String sessionId = destPath.substring(destPath.length() - 4, destPath.length() - 1);

            // Save any remaining data
            saveDataToFile(destPath, sessionId);

            // Unsubscribe from topics
            mqttClient.unsubscribe(TOPIC_UWB_RAW);
            mqttClient.unsubscribe(TOPIC_UWB_ZONES);

            // Disconnect and close
            mqttClient.disconnectForcibly();
            mqttClient.close();

            log.info("BU03 UWB: Stopped successfully. Data saved to: "
                    + destPath + sessionId + BU03_SUFFIX);
        }
    }

    /**
     * Test method for BU03 UWB data recording Connects to localhost and logs
     * incoming messages
     *
     * @throws Exception If test connection fails
     */
    @Override
    public void testRecordingPosition() throws Exception {
        String broker = "tcp://localhost:1883";
        String clientId = "BU03-UWB-Test";
        MemoryPersistence persistence = new MemoryPersistence();

        mqttClient = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);

        log.info("BU03 UWB Test: Connecting to broker: " + broker);
        mqttClient.connect(connOpts);
        log.info("BU03 UWB Test: Connected");

        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                log.warn("BU03 UWB Test: Connection lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                log.info("BU03 UWB Test: Topic: " + topic);
                log.info("BU03 UWB Test: Message: " + message.toString());

                try {
                    JSONObject data = JSON.parseObject(message.toString());
                    log.info("BU03 UWB Test: Parsed JSON: " + data.toJSONString());

                    // Log specific fields based on message type
                    if (data.containsKey("tags")) {
                        // Pi 5 direct format
                        log.info("BU03 UWB Test: Source=Pi5, Num_tags="
                                + data.getInteger("num_tags"));
                    } else if (data.containsKey("zone")) {
                        // Pico zone format
                        log.info("BU03 UWB Test: Source=Pico, Zone="
                                + data.getString("zone"));
                    }
                } catch (Exception e) {
                    log.error("BU03 UWB Test: Error parsing message", e);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                log.debug("BU03 UWB Test: Delivery complete");
            }
        });

        log.info("BU03 UWB Test: Subscribing to topics...");
        mqttClient.subscribe(TOPIC_UWB_RAW);
        mqttClient.subscribe(TOPIC_UWB_ZONES);

        log.info("BU03 UWB Test: Listening for messages... (Press Ctrl+C to exit)");

        // Keep running
        while (true) {
            Thread.sleep(1000);
        }
    }

    /**
     * Save BU03 UWB data to file Data is saved with format: sessionId-bu03.json
     * Each line in the file is a JSON object containing: - topic: MQTT topic
     * the message came from - received_at: Timestamp when message was received
     * - session_id: Recording session identifier - source: Data source (pi5_usb
     * or pico_wifi) - data: Original UWB sensor data payload
     *
     * @param destPath Destination directory path
     * @param sessionId Session identifier
     * @throws IOException If file write fails
     */
    private void saveDataToFile(String destPath, String sessionId) throws IOException {
        if (messageList != null && !messageList.isEmpty()) {
            String filename = sessionId + BU03_SUFFIX;
            File outputFile = new File(destPath + filename);

            FileUtils.writeLines(outputFile, messageList, true);

            log.info("BU03 UWB: Saved {} messages to file: {}",
                    messageList.size(), outputFile.getAbsolutePath());

            messageList.clear();
        }
    }
}
