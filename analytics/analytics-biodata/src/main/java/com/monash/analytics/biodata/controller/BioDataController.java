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
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for EDA data collection
 *
 * @author Xinyu Li
 */

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/bio")
public class BioDataController {
    @Autowired
    private BioDataServiceAPI bioDataServiceAPI;

    public static String dataCollectionSignal = "";

    public static String baselineSignal = "";

    private String[] getDeviceIdAccordingSN(String sn1, String sn2, String sn3, String sn4) {
        String[] deviceArray = new String[4];

        Map<String, String> deviceSNMap = new HashMap<>();
        deviceSNMap.put("A029AC", "3b9efd");
        deviceSNMap.put("A029F6", "a39bfd");
        deviceSNMap.put("A02555", "379efd");
        deviceSNMap.put("A03588", "59ce11");

        deviceSNMap.put("A03696", "67cc11");
        deviceSNMap.put("A010C7", "1901bc");
        deviceSNMap.put("A012EF", "6101bc");
        deviceSNMap.put("A013B2", "b51af2");
        deviceSNMap.put("A0217A", "e72b64");

        deviceArray[0] = deviceSNMap.get(sn1);
        deviceArray[1] = deviceSNMap.get(sn2);
        deviceArray[2] = deviceSNMap.get(sn3);
        deviceArray[3] = deviceSNMap.get(sn4);

        return deviceArray;
    }

    /**
     * DeviceId:    3b9efd, a39bfd, 379efd, 59ce11, 67cc11, 1901bc, 6101bc, b51af2, e72b64
     * S/N:         A029AC, A029F6, A02555, A03588, A03696, A010C7, A012EF, A013B2, A0217A
     * command:     acc, bvp, gsr, ibi, tmp
     * acc - 3-axis acceleration
     * bvp - Blood Volume Pulse
     * gsr - Galvanic Skin Response
     * ibi - Interbeat Interval and Heartbeat
     * tmp - Skin Temperature
     *
     * start collect eda data api
     *
     * @return
     */
    @RequestMapping("/start/{sessionid}")
    public String start(@PathVariable("sessionid") String sessionid) {


        if (dataCollectionSignal.equals("start")) {
            return "empatica already started";
        }

        String[] commandArray = {"acc", "bvp", "gsr", "ibi", "tmp"};
//        String[] deviceArray = {"3b9efd", "a39bfd", "379efd", "59ce11"};
        String[] deviceArray = getDeviceIdAccordingSN("A03696", "A0217A", "A03588", "A012EF");

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
        dataCollectionSignal = "start";
        return "bio data start success";
    }

    @RequestMapping("/start-baseline/{sessionid}")
    public String startBaseline(@PathVariable("sessionid") String sessionid) {
        if (baselineSignal.equals("start")) {
            return "baseline empatica already started";
        }

        baselineSignal = "start";

        String[] commandArray = {"acc", "bvp", "gsr", "ibi", "tmp"};
//        String[] deviceArray = {"3b9efd", "a39bfd", "379efd", "59ce11"};
        String[] deviceArray = getDeviceIdAccordingSN("A03696", "A0217A", "A03588", "A012EF");

        File dir = new File(ConstantValues.FILE_SAVE_PATH + sessionid);
        if (!dir.exists()) {
            log.info("start bio data make dir result: " + dir.mkdir());
        } else {
            log.info("start bio data: session dir exist");
        }

        for (String device : deviceArray) {
            for (String command : commandArray) {
                try {

                    bioDataServiceAPI.getBaselineBioData(device, command, ConstantValues.FILE_SAVE_PATH + sessionid + "\\", sessionid);
                    log.info("getBioData started:" + device);
                } catch (Exception e) {
                    log.error("error in device:{}, command:{}", device, command);
                    e.printStackTrace();
                    return "bio data start exception";
                }
            }
        }

        return "bio data start success";
    }

    /**
     * stop collect eda data api
     * @return
     */
    @RequestMapping("/stop")
    public String stop() {
        dataCollectionSignal = "stop";
        baselineSignal = "stop";
        return "bio data stop success";
    }

}
