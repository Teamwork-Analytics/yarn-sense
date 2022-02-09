package com.monash.analytics.video;

import com.monash.analytics.video.service.VideoServiceAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AnalyticsVideoApplicationTests {
    @Autowired
    private VideoServiceAPI videoServiceAPI;
    @Test
    void contextLoads() throws Exception {
//        videoServiceAPI.combineVideo("/Users/xinyu/Downloads/", "1");
    }

}
