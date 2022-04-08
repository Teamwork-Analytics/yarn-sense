package com.monash.analytics.position.controller;

import com.monash.analytics.position.constant.ConstantValues;
import com.monash.analytics.position.service.PositionServiceAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Path;
import java.io.File;

/**
 * controller handles the position data collection
 * @author Xinyu Li
 */

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/pos")
public class PositionController {
    private static String sessionId = "";
    @Autowired
    private PositionServiceAPI positionServiceAPI;

    private String status = "";

    /**
     * start to get position data from pozyx server
     * @param sessionid
     * @return
     */
    @RequestMapping(value = "/start/{sessionid}")
    public String startGetPosition(@PathVariable String sessionid) {
        sessionId = sessionid;

        if (status.equals("start")) {
            return "position already started";
        }

        status = "start";
        File dir = new File(ConstantValues.FILE_SAVE_PATH + sessionid);
        if (!dir.exists()) {
            log.info("start position make dir result: " + dir.mkdir());
        } else {
            log.info("start position: session dir exist");
        }

        try {
            positionServiceAPI.startRecordingPosition(ConstantValues.FILE_SAVE_PATH + sessionid + "\\", sessionid);
        } catch (Exception e) {
            e.printStackTrace();
            status = "stop";
            log.error("exception in startGetPosition");
            return "exception in startGetPosition";
        }
        return "position start success";
    }

    /**
     * stop to get position data
     * @return
     */
    @RequestMapping(value = "/stop")
    public String stopGetPosition() {
        try {
            if (status.equals("stop")) {
                return "position already stopped";
            }
            status = "stop";
            positionServiceAPI.stopRecordingPosition(ConstantValues.FILE_SAVE_PATH + sessionId + "\\");
        } catch (Exception e) {
            return "exception in stopRecordingPosition";
        }

        return "position stop success";
    }
}
