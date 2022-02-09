package com.monash.analytics.biodata.controller;

import com.monash.analytics.biodata.constant.ConstantValues;
import com.monash.analytics.biodata.service.BioDataServiceAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/bio")
public class BioDataController {
    @Autowired
    private BioDataServiceAPI bioDataServiceAPI;

    public static boolean dataCollectionSignal = false;

    /**
     * DeviceId:    3b9efd, a39bfd, 379efd, 59ce11, 67cc11
     * S/N:         A029AC, A029F6, A02555, A03588, A03696
     * command:     acc, bvp, gsr, ibi, tmp
     * @return
     */
    @RequestMapping("/start/{sessionid}")
    public String start(@PathVariable("sessionid") String sessionid) {

        String[] commandArray = {"acc", "bvp", "gsr", "ibi", "tmp"};
        String[] deviceArray = {"379efd", "a39bfd"};  //"3b9efd",


        File dir = new File(ConstantValues.FILE_SAVE_PATH + sessionid);
        if (!dir.exists()) {
            log.info("start bio data make dir result: " + dir.mkdir());
        } else {
            log.info("start bio data: session dir exist");
        }

        for (String device : deviceArray) {
            for (String command : commandArray) {
                try {

                    bioDataServiceAPI.getBioData(device, command, ConstantValues.FILE_SAVE_PATH + sessionid + "\\", sessionid);
                    log.info("getBioData started:" + device);
                } catch (Exception e) {
                    log.error("error in device:{}, command:{}", device, command);
                    e.printStackTrace();
                    return "bio data start exception";
                }
            }
        }
        dataCollectionSignal = true;
        return "bio data start success";
    }
    @RequestMapping("/stop")
    public String stop() {
        dataCollectionSignal = false;
        return "bio data stop success";
    }

}
