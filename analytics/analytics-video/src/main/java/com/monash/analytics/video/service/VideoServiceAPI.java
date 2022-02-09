package com.monash.analytics.video.service;

import java.io.File;
import java.io.InputStream;

public interface VideoServiceAPI {
//    void informAllServiceStop() throws Exception;
//    void informAllServiceStart(String sessionId) throws Exception;
//    void receiveVideo(File destFile, InputStream inputStream) throws Exception;
//    void combineVideo(String destPath, String sessionId) throws Exception;
    void recordingVideoUsingFFmpeg(String destPath) throws Exception;
}
