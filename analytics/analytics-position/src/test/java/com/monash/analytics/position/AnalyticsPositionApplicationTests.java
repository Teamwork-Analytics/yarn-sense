package com.monash.analytics.position;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.monash.analytics.position.service.PositionServiceAPI;
import com.monash.analytics.position.service.TestServiceAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AnalyticsPositionApplicationTests {

    @Autowired
    private PositionServiceAPI positionServiceAPI;

    @Autowired
    private TestServiceAPI testServiceAPI;
    @Test
    void contextLoads() {
        testServiceAPI.testCreateTopics();
    }

    @Test
    void testTopicList() {
        testServiceAPI.testGetTopicList();
    }

    @Test
    void testConsumer() {
        testServiceAPI.testConsumer();
    }

    @Test
    void testPositionData() throws Exception {
        positionServiceAPI.testRecordingPosition();
//        String message = "[{\"version\":\"1\",\"alive\":true,\"tagId\":\"27226\",\"success\":true,\"timestamp\":1626146663.105,\"data\":{\"tagData\":{},\"anchorData\":[],\"coordinates\":{\"x\":3551,\"y\":6139,\"z\":-189},\"orientation\":{\"yaw\":0.875,\"roll\":0.082,\"pitch\":0.25},\"metrics\":{\"latency\":42.9,\"rates\":{\"update\":4.112,\"success\":4.112}}}}]";
//
//        JSONArray jr = JSON.parseArray(message);
//        System.out.println(jr.getJSONObject(0).getString("tagId"));
    }

}
