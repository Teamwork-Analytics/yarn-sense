package com.monash.analytics.biodata.service;

import com.monash.analytics.biodata.constant.ConstantValues;
import com.monash.analytics.biodata.controller.BioDataController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;

@Slf4j
@Service
public class BioDataServiceImpl implements BioDataServiceAPI {

    private Producer<String, String> producer = null;
    /**
     * DeviceId:    3b9efd, a39bfd, 379efd
     * S/N:         A029AC, A029F6, A02555
     */
    @Override
    public void testConnectToEmpaticaDevice() throws Exception {
        Socket clientSocket = new Socket(ConstantValues.LOCAL_SERVER_NAME, 28000);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        out.println("device_list");
        log.info("resp:" + in.readLine());
        out.println("device_connect 3B9EFD");
        log.info("resp:" + in.readLine());
        out.println("device_subscribe acc ON");
        log.info("resp:" + in.readLine());

        int i = 0;
        String temp = null;
        while ((temp = in.readLine()) != null) {
            log.info("resp:" + temp);

            if (i == 1000) {
                out.println("device_disconnect");
//                System.out.println("---" + in.readLine() + "---");
            }
            i++;
        }

        in.close();
        out.close();
        clientSocket.close();
    }


    /**
     * DeviceId:
     * a39bfd,        3b9efd, 379efd
     * command: acc, bvp, gsr, ibi, tmp
     * @param deviceId
     * @param command
     * @throws Exception
     */
    @Async("getBioDataExecutor")
    @Override
    public void getBioData(String deviceId, String command, String destPath, String sessionId) throws Exception {

        Socket clientSocket = new Socket(ConstantValues.LOCAL_SERVER_NAME, 28000);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        out.println("device_list");
        log.info("resp:" + in.readLine());
        out.println("device_connect " + deviceId);
        log.info("resp:" + in.readLine());
        Thread.sleep(500);
        out.println("device_subscribe " + command + " ON");
        Thread.sleep(500);
        log.info("resp:" + in.readLine());
        producer = KafkaProducerUtils.createProducer();

//        int i = 0;
        StringBuilder sb = new StringBuilder();
        String temp;
        while ((temp = in.readLine()) != null) {
            sb.append("resp:").append(temp).append("\n");

            KafkaProducerUtils.producerSend(producer, temp, deviceId, sessionId);
            //use buffer
            if (sb.length() >= 10000) {

                FileUtils.writeStringToFile(new File(destPath + deviceId + "_" + command + ".txt"), sb.toString(), "UTF-8", true);
                log.info("append data to file" + deviceId + "_" + command + ".txt");

                sb.delete( 0, sb.length() );
            }

            if (!BioDataController.dataCollectionSignal) {
//                out.println("device_disconnect");
//                System.out.println("---" + in.readLine() + "---");
                break;
            }
//            i++;
        }

        in.close();
        out.close();
        clientSocket.close();
        log.info("finish save data to file" + deviceId + "_" + command + ".txt");
        FileUtils.writeStringToFile(new File(destPath + deviceId + "_" + command + ".txt"), sb.toString(), "UTF-8", true);

    }
}
