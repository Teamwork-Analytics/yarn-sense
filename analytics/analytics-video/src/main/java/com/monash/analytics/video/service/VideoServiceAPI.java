package com.monash.analytics.video.service;

import java.io.File;
import java.io.InputStream;

/**
 * Video service APIs
 * @author Xinyu Li
 */

public interface VideoServiceAPI {
//    void informAllServiceStop() throws Exception;
//    void informAllServiceStart(String sessionId) throws Exception;
//    void receiveVideo(File destFile, InputStream inputStream) throws Exception;
//    void combineVideo(String destPath, String sessionId) throws Exception;
    void recordingVideoUsingFFmpeg(String destPath) throws Exception;
    void recordingVideoUsingFFmpeg2(String destPath) throws Exception;
    void recordingVideoUsingFFmpeg3(String destPath) throws Exception;
}
