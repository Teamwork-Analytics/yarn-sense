package com.monash.analytics.video.service;

import com.alibaba.fastjson.JSONObject;
import com.monash.analytics.video.controller.VideoController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Video service APIs implementation
 * @author Xinyu Li
 */

@Slf4j
@Service
public class VideoServiceImpl implements VideoServiceAPI{

    //rest template for call services
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient eurekaClient;

    /**
     * Asynchronously use thread to start video recording
     * @param destPath
     * @throws Exception
     */
    @Async("videoRecordingExecutor")
    @Override
    public void recordingVideoUsingFFmpeg(String destPath) throws Exception {
        DateTime dt = new DateTime();
        String time = dt.toString("yyyy-MM-dd_HH-mm-ss");

        VideoUtils videoRecord = new VideoUtils(destPath + "_video_" + time, true);
        videoRecord.init();
        videoRecord.start();

        while (true) {

                if (VideoController.controlVideo.equalsIgnoreCase("stop")) {
                    videoRecord.stop();
                    log.info("****stop recording****");
                    break;
                }
        }
        log.info("video recording finish");
    }
}
