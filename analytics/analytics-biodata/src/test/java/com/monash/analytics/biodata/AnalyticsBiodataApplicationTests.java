package com.monash.analytics.biodata;

import com.monash.analytics.biodata.service.BioDataServiceAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AnalyticsBiodataApplicationTests {

    @Autowired
    private BioDataServiceAPI bioDataServiceAPI;
    @Test
    void contextLoads() throws Exception {
        bioDataServiceAPI.testConnectToEmpaticaDevice();
    }

}
