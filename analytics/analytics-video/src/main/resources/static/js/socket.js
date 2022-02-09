// var xxx = {};
// xxx.init = function (){
//     xxx.lastHeartBeat = new Date().getTime();
//     xxx.overTime = 33000;
//     xxx.websocket = null;
//     if ('WebSocket' in window) {
//         xxx.websocket = new WebSocket("ws://localhost:7101/video/websocket")
//         xxx.websocket.binaryType = "arraybuffer";
//     } else {
//         console.log("current browser doesnt support websocket")
//     }
//
//     xxx.websocket.onerror = function (event) {
//         console.log("websocket error")
//         console.log(event);
//     };
//
//     xxx.websocket.onopen = function() {
//         console.log("websocket open")
//     }
//
//     xxx.websocket.onmessage = function(event) {
//         console.log("websocket message:" + event.data);
//         console.log(event.data);
//     }
//
//     xxx.websocket.onclose = function () {
//         console.log("websocket close");
//     }
//     setInterval(checkConnect, 6000);
// };
// function checkConnect() {
//     setMessage("ping");
//     if (xxx.websocket.readyState === 3) {
//         reConnect();
//     }
// }
//
// function reConnect() {
//     console.log("socket reconnect");
//     xxx.init();
// }
//
// window.onbeforeunload = function () {
//     closeWebSocket();
// }
// function setMessage(message) {
//     console.log(message);
//     if (xxx.websocket.readyState === 1) {
//         xxx.websocket.send(message);
//     }
// }
// function closeWebSocket() {
//     xxx.websocket.close();
//     reConnect();
// }

