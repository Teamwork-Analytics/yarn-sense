package com.monash.analytics.biodata.service;

import java.io.IOException;

/**
 * Service APIs
 * @author Xinyu Li
 */

public interface BioDataServiceAPI {
    void testConnectToEmpaticaDevice() throws Exception;
    void getBioData(String deviceId, String command, String destPath, String sessionId) throws Exception;
    void getBaselineBioData(String deviceId, String command, String destPath, String sessionId) throws Exception;
}
