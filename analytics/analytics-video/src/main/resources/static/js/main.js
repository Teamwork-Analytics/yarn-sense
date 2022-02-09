'use strict';

const mediaStreamConstraints = {
    video:true,
    width: 1920,
    height: 1080
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
var reader = new FileReader();

// 当该函数被触发后，将数据压入到 blob 中
function handleDataAvailable(e){
    if(e && e.data && e.data.size > 0){
        buffer.push(e.data);
        console.log(e.data);
        reader.readAsDataURL(e.data);
        reader.onloadend = function() {
            console.log(reader.result.length);

            // console.log(base64data);
            sendMessage(reader.result);
        };
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

var stompClient = null;
function connect() {
    var socket = new SockJS('/video-stream');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        // setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function(messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });
}

function disconnect() {
    if(stompClient != null) {
        stompClient.disconnect();
    }
    // setConnected(false);
    console.log("Disconnected");
}

function sendMessage(message) {
    // var from = document.getElementById('from').value;
    // var text = document.getElementById('text').value;
    console.log('sendmessage:');
    stompClient.send("/server/video-stream", {}, message);
}

function showMessageOutput(messageOutput) {
    // var response = document.getElementById('response');
    // var p = document.createElement('p');
    // p.style.wordWrap = 'break-word';
    // p.appendChild(document.createTextNode(messageOutput.from + ": "
    //     + messageOutput.text + " (" + messageOutput.time + ")"));
    // response.appendChild(p);
    console.log('messageOutput:');
    console.log(messageOutput);
}


var totalTime = 60;
var segmentTime = 5;
var sendTimes = 0;
var copyBufferStart = 0;

function startRecord(){
    connect();
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
    // var reader = new FileReader();
    // var base64data;



    clearInterval(timer);
    // setInterval() 方法可按照指定的周期（以毫秒计）来调用函数或计算表达式。

    timer=setInterval(function ()
    {
        var seconds = (new Date().getTime() - dateStarted) / 1000;
        document.querySelector('h1').innerHTML = 'Recording Duration: ' + calculateTimeDuration(seconds);

        console.log(parseInt(seconds));
        if (parseInt(seconds) === totalTime) {
            stop();
        }

        if (parseInt(seconds) % segmentTime === 0) {
            console.log(buffer);
            sendTimes = (parseInt(seconds) / segmentTime);
            console.log("send times :" + sendTimes);
            copyBufferStart = (sendTimes - 1) * 40;

            console.log("start:" + copyBufferStart);
            console.log("end:" + (copyBufferStart + 40));
            var tempbuffer = buffer.slice(copyBufferStart, copyBufferStart + 40);
            var tempBlob = new Blob(tempbuffer, {type: 'video/webm'});
            // reader.readAsDataURL(tempBlob);
            // reader.onloadend = function() {
            //   base64data = reader.result;
            //   // console.log(base64data);
            //   //   sendMessage(base64data);
            // };
            /**
            var oReq = new XMLHttpRequest();
            oReq.open("POST", "http://localhost:7101/video/receive/11111test/" + (parseInt(seconds) / segmentTime), true);
            var blob = new Blob(tempbuffer, {type: 'video/webm'});
            // const text = (new Response(blob)).text();
            // console.log(text);
            oReq.send(blob);
            // oReq.send(tempbuffer);
            oReq.onreadystatechange = function () {
                // console.log("oReq readyState:" + oReq.readyState);
                // console.log("oReq status:" + oReq.status);
                if (oReq.readyState === 4 && oReq.status === 200) {
                    console.log(oReq.responseText);
                }
            };
*/

            // buffer = [];
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

// function sendGet(message) {
//     console.log("into sendGet")
//     var oReq = new XMLHttpRequest();
//     oReq.open("GET", "http://localhost:7101/test?base64Video=" + message, true);
//
//     oReq.send();
//
//     oReq.onreadystatechange = function () {
//         if (oReq.readyState === 4 && oReq.status === 200) {
//             console.log(oReq.responseText);
//         }
//     };
// }

function stop(){
    if(mediaRecorder && (mediaRecorder.state === "recording" || mediaRecorder.state === "paused")){
        mediaRecorder.stop();
        clearInterval(timer);
        console.log("recording stop");

        /**
        var tempbuffer = buffer.slice(copyBufferStart);

        var oReqLastData = new XMLHttpRequest();
        oReqLastData.open("POST", "http://localhost:7101/video/receive/11111test/last", true);
        var blob = new Blob(tempbuffer, {type: 'video/webm'});
        oReqLastData.send(blob);
        // oReq.send(tempbuffer);
        oReqLastData.onreadystatechange = function () {
            if (oReqLastData.readyState === 4 && oReqLastData.status === 200) {
                console.log(oReqLastData.responseText);

                var oReq = new XMLHttpRequest();
                oReq.open("GET", "http://localhost:7101/video/stop", true);

                oReq.send();
                oReq.onreadystatechange = function () {
                    console.log("oReq readyState:" + oReq.readyState);
                    console.log("oReq status:" + oReq.status);
                    if (oReq.readyState === 4 && oReq.status === 200) {
                        console.log(oReq.responseText);
                    }
                };
            }
        };*/
    }
}