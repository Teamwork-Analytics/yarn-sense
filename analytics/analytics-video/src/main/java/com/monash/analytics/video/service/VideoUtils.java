package com.monash.analytics.video.service;


import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.*;

import com.monash.analytics.video.controller.VideoController;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.FrameRecorder.Exception;
import org.bytedeco.opencv.opencv_core.IplImage;

/**
 * Video utils
 * @author Xinyu Li
 */

@Slf4j
public class VideoUtils {
    //threads
    private ScheduledThreadPoolExecutor screenTimer;
    private ScheduledThreadPoolExecutor screenTimer2;
    // FFmpegFrameRecorder
    private final FFmpegFrameRecorder recorder;
    // used to record video
    private ScheduledThreadPoolExecutor exec;
    private TargetDataLine line;
    private AudioFormat audioFormat;
    private DataLine.Info dataLineInfo;
    ///whether open audio device
    private boolean isHaveDevice = true;
    private long startTime = 0;
    private long videoTS = 0;
    private long pauseTimeStart = 0; //when pause start
    private long pauseTime = 0; //pause seconds
    private final double frameRate = 40;
    private final FrameGrabber grabber;
    private final CanvasFrame canvasFrame;
    private final OpenCVFrameConverter.ToIplImage converter;
    private final int width = 1280;
    private final int height = 720;

    private String state="stop"; // recording status: start - "start recording", pause - "pause recording", stop - "stop recording"
    public String getState() {
        return state;
    }

    /**
     * setup FFmpeg configuration
     * @param fileName
     * @param isHaveDevice
     * @throws FrameGrabber.Exception
     * @throws FFmpegFrameRecorder.Exception
     */
    public VideoUtils(int camera_id, String fileName, boolean isHaveDevice) throws FrameGrabber.Exception, FFmpegFrameRecorder.Exception {

        grabber = FrameGrabber.createDefault(camera_id); //local camera default device id is 0
        log.info("generate grabber");
        grabber.setImageHeight(720);
        grabber.setImageWidth(1280);
        grabber.setVideoBitrate(3000000);
        grabber.setFrameRate(frameRate);
        converter = new OpenCVFrameConverter.ToIplImage(); //converter

        recorder = new FFmpegFrameRecorder(fileName + ".mp4", width, height);
//		 recorder.setVideoCodec(avcodec.AV_CODEC_ID_H265); // 28
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4); // 13
        recorder.setFormat("mp4");
        // recorder.setFormat("mov,mp4,m4a,3gp,3g2,mj2,h264,ogg,MPEG4");
        recorder.setSampleRate(44100);
        recorder.setFrameRate(frameRate);
        recorder.setVideoQuality(0);
        recorder.setVideoOption("crf", "23");
        // 2000 kb/s, 720P
        recorder.setVideoBitrate(3000000);

        recorder.setVideoOption("preset", "faster");
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P); // yuv420p
        recorder.setAudioChannels(2);
        recorder.setAudioOption("crf", "0");
        // Highest quality
        recorder.setAudioQuality(0);
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        canvasFrame = new CanvasFrame("camera" + camera_id, CanvasFrame.getDefaultGamma() / grabber.getGamma());
        canvasFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setAlwaysOnTop(true);
        canvasFrame.setCanvasSize(1280, 720);
        canvasFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                log.info("window closing");
                if (!VideoController.controlVideo.equals("stop")) {
                    stop();

                } else {
                    log.info("already stopped");
                }
            }
        });


        if(isHaveDevice) {
            audioFormat = new AudioFormat(44100.0F, 16, 2, true, false);
            dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            try {
                line = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            } catch (LineUnavailableException e1) {

                log.error("cannot find audio device, "+e1);
            }
        }
        this.isHaveDevice = isHaveDevice;
    }

    /**
     * create new thread and get the video source
     */
    public void init() {
        try {
            grabber.start();
            recorder.start();
        } catch (Exception | FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        screenTimer2 = new ScheduledThreadPoolExecutor(1);
        screenTimer2.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    canvasFrame.showImage(grabber.grab());
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
        }, (int) (1000 / frameRate), (int) (1000 / frameRate), TimeUnit.MILLISECONDS);
    }

    /**
     * start recording
     */
    public void start() {
        state="start";
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        if(pauseTimeStart!=0) {
            //calculate pause time length
            pauseTime=System.currentTimeMillis()-pauseTimeStart;
            pauseTimeStart=0;//reset
        }
        else {
            pauseTime=0;
        }

        // if there exists audio device, start audio thread
        if (isHaveDevice) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SoundCapture();
                }
            }).start();

        }
        screenCapture();

    }

    /**
     * get screen images and save to video
     */
    private void screenCapture() {
        // 录屏
        screenTimer = new ScheduledThreadPoolExecutor(1);
        screenTimer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                BufferedImage screenCapture = null;
                try {
                    Java2DFrameConverter paintConverter = new Java2DFrameConverter();
                    Frame frame = converter.convert(converter.convert(grabber.grab()));
//                    System.out.println("width:" + grabber.getImageWidth() + "=====" + grabber.getImageHeight());
                    screenCapture = paintConverter.getBufferedImage(frame, 1);
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }

                BufferedImage videoImg = new BufferedImage(width, height,
                        BufferedImage.TYPE_3BYTE_BGR);

                Graphics2D videoGraphics = videoImg.createGraphics();

                videoGraphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
                videoGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                        RenderingHints.VALUE_COLOR_RENDER_SPEED);
                videoGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
                videoGraphics.drawImage(screenCapture, 0, 0, null);

                Java2DFrameConverter java2dConverter = new Java2DFrameConverter();

                Frame frame = java2dConverter.convert(videoImg);
                try {
                    videoTS = 1000L*(System.currentTimeMillis()-startTime-pauseTime);

                    if (videoTS > recorder.getTimestamp()) {
                        recorder.setTimestamp(videoTS);
                    }
                    recorder.record(frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                videoGraphics.dispose();
                videoGraphics = null;
                videoImg.flush();
                videoImg = null;
                java2dConverter = null;
                screenCapture.flush();
                screenCapture = null;


                canvasFrame.setTitle("recording time:" + format(System.currentTimeMillis() - startTime));
            }
        }, (int) (1000 / frameRate), (int) (1000 / frameRate), TimeUnit.MILLISECONDS);
    }

    /**
     * conver timestamp to readable time
     * @param elapsed
     * @return
     */
    private String format(long elapsed) {
        int hour, minute, second, milli;
        milli = (int) (elapsed % 1000);
        elapsed = elapsed / 1000;

        second = (int) (elapsed % 60);
        elapsed = elapsed / 60;

        minute = (int) (elapsed % 60);
        elapsed = elapsed / 60;

        hour = (int) (elapsed % 60);
        // %02d:%02d:%02d:%03d 00:00:00:000
        return String.format("%02d:%02d:%02d:%03d", hour, minute, second, milli);
    }

    /**
     * get sound and save to video
     */
    public void SoundCapture() {

        try {
            if(!line.isRunning()){
                line.open(audioFormat);
                line.start();
            }
        } catch (LineUnavailableException e1) {
            e1.printStackTrace();
        }


        final int sampleRate = (int) audioFormat.getSampleRate();
        final int numChannels = audioFormat.getChannels();

        int audioBufferSize = sampleRate * numChannels;
        final byte[] audioBytes = new byte[audioBufferSize];

        exec = new ScheduledThreadPoolExecutor(1);
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    int nBytesRead = line.read(audioBytes, 0, line.available());
                    int nSamplesRead = nBytesRead / 2;
                    short[] samples = new short[nSamplesRead];

                    // Let's wrap our short[] into a ShortBuffer and
                    // pass it to recordSamples
                    ByteBuffer.wrap(audioBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(samples);
                    ShortBuffer sBuff = ShortBuffer.wrap(samples, 0, nSamplesRead);

                    // recorder is instance of
                    // org.bytedeco.javacv.FFmpegFrameRecorder
                    recorder.recordSamples(sampleRate, numChannels, sBuff);
                    // System.gc();
                } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                    e.printStackTrace();
                }
            }
        }, (int) (1000 / frameRate), (int) (1000 / frameRate), TimeUnit.MILLISECONDS);
    }

    /**
     * pause recording
     */
    public void pause() {
        state="pause";
        screenTimer.shutdownNow();
        screenTimer = null;
        if (isHaveDevice) {
            exec.shutdownNow();
            exec = null;
        }
        pauseTimeStart = System.currentTimeMillis();

    }

    /**
     * stop recording
     */
    public void stop() {
        state="stop";
        if (null != screenTimer) {
            screenTimer.shutdownNow();
        }
        if (null != screenTimer2) {
            screenTimer2.shutdownNow();
        }
        try {
            if (isHaveDevice) {
                if (null != exec) {
                    exec.shutdownNow();
                }
                if (null != line) {
                    line.stop();
                    line.close();
                }
                dataLineInfo = null;
                audioFormat = null;
            }
            recorder.stop();
            recorder.release();
            recorder.close();
            grabber.close();
            screenTimer = null;
            // screenCapture = null;
            canvasFrame.dispose();

        } catch (Exception | FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }
}
