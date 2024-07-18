package com.monash.analytics.video.utils;

import com.monash.analytics.utils.common.vo.BasePageVO;
import com.monash.analytics.utils.exception.CommonServiceException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CommonUtilsTest {
    @Test
    public void test() throws CommonServiceException {
//        BaseResponseVO responseVO = new BaseResponseVO();
//        System.out.println(responseVO.run("hello"));
//        BasePageVO basePageVO = new BasePageVO();
//        basePageVO.checkParam();
    }

    @Test
    public void testFileContent() throws IOException {
        String destPath = "C:\\Users\\colam\\Documents\\saved_data\\";
        File file1 = new File(destPath + "undefinedaaa.webm");
        File file2 = new File(destPath + "myVideo.webm");
        byte[] b1 = FileUtils.readFileToByteArray(file1);
        byte[] b2 = FileUtils.readFileToByteArray(file2);
        System.out.println(b1.length);
        System.out.println(b2.length);


        System.out.println("b1[0]:" + b1[0]);
        System.out.println("b1[1]:" + b1[1]);
        System.out.println("b1[2]:" + b1[2]);
        System.out.println("b1[3]:" + b1[3]);

        for (int i = 0; i < b1.length; i++) {
            if (b1[i] != b2[i]) {
                System.out.println(i);
                System.out.println(b1[i] + "=------=" + b2[i]);
                break;
            }
        }
    }

    @Test
    public void testFileContent2() throws IOException {
        String destPath = "C:\\Users\\colam\\Documents\\saved_data\\";
        File file1 = new File(destPath + "undefined.txt");
        File file2 = new File(destPath + "myVideo.txt");
        String s1 = FileUtils.readFileToString(file1, "UTF-8");
        String s2 = FileUtils.readFileToString(file2, "UTF-8");
        System.out.println(s1.length());
        System.out.println(s2.length());

       for (int i = 0; i < s1.length(); i++) {
           if (s1.charAt(i) != s2.charAt(i)) {
               System.out.println(i);
               System.out.println(s1.charAt(i-2) + "******" + s2.charAt(i-2));
               System.out.println(s1.charAt(i-1) + "******" + s2.charAt(i-1));
               System.out.println(s1.charAt(i) + "******" + s2.charAt(i));
               System.out.println(s1.charAt(i+1) + "******" + s2.charAt(i+1));
               System.out.println(s1.charAt(i+2) + "******" + s2.charAt(i+2));
               break;
           }
       }
    }

    @Test
    public void testFileContent3() throws IOException {
        String destPath = "C:\\Users\\colam\\Documents\\saved_data\\";
        File file1 = new File(destPath + "undefined.txt");
        File file2 = new File(destPath + "myVideo.txt");
        List<String> s1 = FileUtils.readLines(file1);
        List<String> s2 = FileUtils.readLines(file2);
        System.out.println(s1.size());
        System.out.println(s2.size());

        System.out.println(s1.get(0) + "********" + s2.get(0));
        System.out.println(s1.get(1) + "********" + s2.get(1));
        System.out.println(s1.get(2) + "********" + s2.get(2));
        System.out.println(s1.get(3) + "********" + s2.get(3));
    }

}
