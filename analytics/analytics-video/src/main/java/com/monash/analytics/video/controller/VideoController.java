package com.monash.analytics.video.controller;

import com.monash.analytics.video.service.VideoServiceAPI;
import com.monash.analytics.video.service.VideoUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.io.*;
import java.util.Base64;

/**
 * controller handles the video data collection
 * @author Xinyu Li
 */

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class VideoController {
    private static String checkSessionId = "";
    public static String controlVideo = "";

    @Autowired
    private VideoServiceAPI videoServiceAPI;

    public static VideoUtils videoRecord = null;
    public final static String destPath = "C:\\develop\\saved_data\\"; //need to change TODO

    /**
     * init video which can shorten the video start time
     * @param sessionid
     * @return
     */
    @RequestMapping(value = "/video/init/{sessionid}")
    public String initVideo(@PathVariable("sessionid") String sessionid) {
        checkSessionId = sessionid;
        if (videoRecord != null) {
            videoRecord.stop();
            controlVideo = "stop";
            log.error("stop current video and init a new video");
//            return "stop current video and start a new video";
        }
        if (controlVideo.equals("init")) {
            log.warn("already stand by");
            return "already stand by";
        }
        if (controlVideo.equals("start")) {
            log.warn("video already started");
            return "video already started";
        }

        controlVideo = "init";
        File dir = new File(destPath + sessionid);
        if (!dir.exists()) {
            log.info("init video make dir result: " + dir.mkdir());
        } else {
            log.info("init video: session dir exist");
        }

        DateTime dt = new DateTime();
        String time = dt.toString("yyyy-MM-dd_HH-mm-ss-SSS");

        try {
            videoRecord = new VideoUtils(destPath + "\\videos\\" + sessionid + "_" + time, true);
            videoRecord.init();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("exception in video init");
            return "exception in video init";
        }

        log.info("video init success");
        return "video init success";
    }

    /**
     * start recording video
     * @param sessionid
     * @return
     */
    @RequestMapping(value = "/video/start/{sessionid}")
    public String start(@PathVariable("sessionid") String sessionid) {

        if (!checkSessionId.equals("") && !checkSessionId.equals(sessionid)) {
            log.error("please close the finished simulation webpage and reopen the current one");
            return "please close the finished simulation webpage and reopen the current one";
        }

        if (controlVideo.equals("start")) {
            return "video already started";
        }
        if (controlVideo.equals("stop") || videoRecord == null) {


            try {
                restartVideo(sessionid);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("exception in video re init");
                return "exception in video re init";
            }
            return "video already stopped, re-init video";
        }

        controlVideo = "start";
        File dir = new File(destPath + sessionid);
        if (!dir.exists()) {
            log.info("start video make dir result: " + dir.mkdir());
        } else {
            log.info("start video: session dir exist");
        }
        try {
            videoServiceAPI.recordingVideoUsingFFmpeg(destPath + sessionid + "\\");
        } catch (Exception e) {
            return "video start exception";
        }
        return "video start success";
    }

    /**
     * stop recording video
     * @return
     */
    @RequestMapping(value = "/video/stop")
    public String stop() {
        if (controlVideo.equals("stop")) {
            return "video already stopped";
        }
        controlVideo = "stop";
        return "video stop success";
    }

    /**
     * restart video recording service, incase error happens
     * @param sessionid
     * @throws Exception
     */
    private void restartVideo(String sessionid) throws Exception {
        Thread.sleep(5000);
        if (videoRecord != null) {
            videoRecord.stop();
        }
        File dir = new File(destPath + sessionid);
        if (!dir.exists()) {
            log.info("re init video make dir result: " + dir.mkdir());
        } else {
            log.info("re init video: session dir exist");
        }

        DateTime dt = new DateTime();
        String time = dt.toString("yyyy-MM-dd_HH-mm-ss-SSS");

        videoRecord = new VideoUtils(destPath + "\\videos\\" + sessionid + "_" + time, true);
        videoRecord.init();
        videoServiceAPI.recordingVideoUsingFFmpeg(destPath + sessionid + "\\");
        controlVideo = "start";
    }
}
