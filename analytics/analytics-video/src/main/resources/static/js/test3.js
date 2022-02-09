'use strict';

const mediaStreamConstraints = {
    video: true,
    audio: true
};

const localVideo = document.getElementById('mainvideo');
const recvideo = document.getElementById('replay');
let localStream;

function gotLocalMediaStream(mediaStream) {
    localStream = mediaStream;
    localVideo.srcObject = mediaStream;

    console.log("helloworld")
    const videoTracks = localStream.getVideoTracks();
    console.log(`video name: ${videoTracks[0].label}`);

}

function handleLocalMediaStreamError(error) {
    console.log('navigator.getUserMedia error', error);
}
console.log("helloworld")

navigator.mediaDevices.getUserMedia(mediaStreamConstraints).then(gotLocalMediaStream).catch(handleLocalMediaStreamError);
var n= 0;
var timer=null;
var oTxt=document.getElementsByTagName("input")[0];

//补零
// function toDub(n){
//     return n<10?"0"+n:""+n;
// }


var buffer;

// 创建录制对象
var mediaRecorder;

var dateStarted;

// 当该函数被触发后，将数据压入到 blob 中
function handleDataAvailable(e){
    if(e && e.data && e.data.size > 0){
        buffer.push(e.data);
    }
}

function calculateTimeDuration(secs) {
    var hr = Math.floor(secs / 3600);
    var min = Math.floor((secs - (hr * 3600)) / 60);
    var sec = Math.floor(secs - (hr * 3600) - (min * 60));

    if (min < 10) {
        min = "0" + min;
    }

    if (sec < 10) {
        sec = "0" + sec;
    }

    if(hr <= 0) {
        return min + ':' + sec;
    }

    return hr + ':' + min + ':' + sec;
}

var totalTime = 7200;

function startRecord(){
    // 防止多次启动报错
    if(mediaRecorder && mediaRecorder.state === "recording"){
        return;
    }

    buffer = [];

    // 设置录制下来的多媒体格式
    var options = {
        mimeType: 'video/webm;codecs=vp8'
        // videoBitsPerSecond: 2500000,
        // mimeType: 'video/mp4'
    }

    // 判断浏览器是否支持录制
    if(!MediaRecorder.isTypeSupported(options.mimeType)){
        console.error(`${options.mimeType} is not supported!`);
        return;
    }

    try{
        mediaRecorder = new MediaRecorder(localVideo.srcObject, options);
    }catch(e){
        console.error('Failed to create MediaRecorder:', e);
        return;
    }

    console.log("before main start");
    // mainStart();
    console.log("after main start");

    // 当有音视频数据来了之后触发该事件
    mediaRecorder.ondataavailable = handleDataAvailable;
    // 开始录制
    mediaRecorder.start(100);
    dateStarted = new Date().getTime();
    var reader = new FileReader();
    var base64data;



    clearInterval(timer);
    // setInterval() 方法可按照指定的周期（以毫秒计）来调用函数或计算表达式。

    timer=setInterval(function ()
    {
        var seconds = (new Date().getTime() - dateStarted) / 1000;
        document.querySelector('h1').innerHTML = 'Recording Duration: ' + calculateTimeDuration(seconds);

        console.log(parseInt(seconds));
        if (parseInt(seconds) === totalTime) {
            download();
            stop();
        }

    },1000);
    console.log("recording start");

}

function startRecplay(){
    console.log(mediaRecorder)
    var blob = new Blob(buffer, {type: 'video/webm'});
    recvideo.src = window.URL.createObjectURL(blob);
    recvideo.srcObject = null;
    recvideo.controls = true;
    recvideo.play();
}

function download(name){
    var blob = new Blob(buffer, {type: 'video/webm'});
    var url = window.URL.createObjectURL(blob);
    var a = document.createElement('a');

    a.href = url;
    a.style.display = 'none';
    a.download = name + 'aaa.webm';
    a.click();
    a.remove();
    // buffer.clear();
    // buffer = [];

    // console.log(buffer);

}

function mainStart() {
    var oReqMainStart = new XMLHttpRequest();
    oReqMainStart.open("GET", "http://localhost:7101/main-start", false);
    oReqMainStart.send();

    oReqMainStart.onreadystatechange = function () {
        console.log("main start oReq readyState:" + oReqMainStart.readyState);
        console.log("main start oReq status:" + oReqMainStart.status);
        if (oReqMainStart.readyState === 4 && oReqMainStart.status === 200) {
            console.log(oReqMainStart.responseText);
        }
    };
}



function stop(){
    if(mediaRecorder && (mediaRecorder.state === "recording" || mediaRecorder.state === "paused")){
        mediaRecorder.stop();
        clearInterval(timer);
        console.log("recording stop");
    }
}