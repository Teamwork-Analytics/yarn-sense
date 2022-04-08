package com.monash.analytics.position.controller;

import com.monash.analytics.position.constant.ConstantValues;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

/**
 * extra controller for the coordination of audio laptop and the main laptop
 * All the service start and stop time will be written in to sync.txt file
 * @author Xinyu Li
 */

@RestController
@RequestMapping("/audio")
public class AudioMessageController {
    /**
     *
     * @param sessionid
     * @param message
     * @return
     */
    @RequestMapping(value = "/start-time/{sessionid}/{message}")
    public String recordAudioStartTime(@PathVariable("sessionid") String sessionid, @PathVariable("message") String message)  {
        DateTime dt = new DateTime();
        String time = dt.toString("yyyy-MM-dd_HH-mm-ss-SSS");
        try {
            FileUtils.writeStringToFile(new File(ConstantValues.FILE_SAVE_PATH + sessionid + "\\" + "sync.txt"), message + "_____" + time + "\n", "UTF-8", true);
        } catch (IOException e) {
            e.printStackTrace();
            return "error in " + message;
        }
        return "success in " + message;
    }

    @RequestMapping(value = "/stop-time/{sessionid}/{message}")
    public String recordAudioStopTime(@PathVariable("sessionid") String sessionid, @PathVariable("message") String message) {
        DateTime dt = new DateTime();
        String time = dt.toString("yyyy-MM-dd_HH-mm-ss-SSS");
        try {
            FileUtils.writeStringToFile(new File(ConstantValues.FILE_SAVE_PATH + sessionid + "\\" + "sync.txt"), message + "_____" + time + "\n", "UTF-8", true);
        } catch (IOException e) {
            e.printStackTrace();
            return "error in " + message;
        }
        return "success in " + message;
    }

}
