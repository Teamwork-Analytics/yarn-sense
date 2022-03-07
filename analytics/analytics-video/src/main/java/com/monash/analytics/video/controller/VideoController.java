package com.monash.analytics.video.controller;

import com.monash.analytics.video.service.VideoServiceAPI;
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
    private final static String destPath = "C:\\develop\\saved_data\\"; //TODO need to change
    public static String controlVideo = "";
    @Autowired
    private VideoServiceAPI videoServiceAPI;

    /**
     * start recording video
     * @param sessionid
     * @return
     */
    @RequestMapping(value = "/video/start/{sessionid}")
    public String start(@PathVariable("sessionid") String sessionid) {

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
        controlVideo = "stop";
        return "video stop success";
    }
}
