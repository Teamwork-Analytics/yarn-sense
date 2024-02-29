package com.monash.analytics.position.constant;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * common used constant values
 * @author Xinyu Li
 */

public class ConstantValues {
    public static final String POSITION_TOPIC_NAME = "position";

    public static final String KAFKA_API_KEY = "";
    public static final String KAFKA_API_SECRET = "";
    public static final String KAFKA_SERVER = "";
    public static final String FILE_SAVE_PATH = "C:\\develop\\saved_data\\";
    public static final String LOCAL_SERVER_NAME = "localhost"; //49.127.70.209
    public static final Map<String, String> kafkaNameMap = ImmutableMap.of(
            "27160","0x6a18",
            "27226","0x6a5a",
            "27261","0x6a7d",
            "27262","0x6a7e",
            "27263", "0x6a7f");
}
