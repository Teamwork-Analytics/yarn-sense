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

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/pos")
public class PositionController {
    private static String sessionId = "";
    @Autowired
    private PositionServiceAPI positionServiceAPI;

    @RequestMapping(value = "/start/{sessionid}")
    public String startGetPosition(@PathVariable String sessionid) {
        sessionId = sessionid;
        File dir = new File(ConstantValues.FILE_SAVE_PATH + sessionid);
        if (!dir.exists()) {
            log.info("start position make dir result: " + dir.mkdir());
        } else {
            log.info("start position: session dir exist");
        }

        try {
            positionServiceAPI.startRecordingPosition(ConstantValues.FILE_SAVE_PATH + sessionid + "\\", sessionid);
        } catch (Exception e) {
            return "exception in startGetPosition";
        }
        return "position start success";
    }

    @RequestMapping(value = "/stop")
    public String stopGetPosition() {
        try {
            positionServiceAPI.stopRecordingPosition(ConstantValues.FILE_SAVE_PATH + sessionId + "\\");
        } catch (Exception e) {
            return "exception in stopRecordingPosition";
        }

        return "position stop success";
    }
}
