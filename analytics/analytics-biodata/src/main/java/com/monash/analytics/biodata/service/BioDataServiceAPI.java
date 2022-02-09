package com.monash.analytics.biodata.service;

import java.io.IOException;

public interface BioDataServiceAPI {
    void testConnectToEmpaticaDevice() throws Exception;
    void getBioData(String deviceId, String command, String destPath, String sessionId) throws Exception;
}