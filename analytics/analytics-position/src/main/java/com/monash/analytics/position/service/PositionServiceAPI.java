package com.monash.analytics.position.service;

/**
 * position data collection service APIs
 * @author Xinyu Li
 */

public interface PositionServiceAPI {
    void startRecordingPosition(String destPath, String sessionId) throws Exception;

    void stopRecordingPosition(String destPath) throws Exception;

    void testRecordingPosition() throws Exception;

}
