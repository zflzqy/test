import Stomp from 'stompjs';
import React, { useState, useEffect } from 'react';
var stompClient = null;
let username = "123";
let client = {};

client.connect =()=> {
    if(stompClient){
        return;
    }
    // 注册ws
    let host = window.location.host
    stompClient = Stomp.client('wss://'+host+'/ws/websocket');
    stompClient.heartbeat.outgoing = 3000;
    stompClient.heartbeat.incoming = 3000;
    
    // 获取会话信息
    username = document.cookie;
    stompClient.connect({
    }, function (succ) {
        console.log(Document.cookie)
        console.log('client connect success:', succ);
        console.log('连接成功');

        // 订阅广播
        // client.subscribe("/topic/accept", onMessage);
        // 订阅消息
        // client.subscribe("/user/topic/acceptUser", onMessage);
    }, function (error) {
        console.log('client connect error:', error);
        console.log('连接失败');
    });
    
    return stompClient;

}

// destination 如果是广播，页面就填/app/send，如果是个人页面就填：/user/sendUser/info
// 注意广播只能以app开头，但是后端的发送以topic开头，ws会自己把app缓存topic
// 单对单只能以user开头，且除去user前缀后的地址必须有2个斜线（傻逼ws）,且前端订阅地址必须是/user/topic开头
client.sendMsg = function (destination, headers, body) {
    stompClient.send(destination, headers, body)
};
// 断开连接
client.disConnect = function () {
    stompClient.disconnect();
    console.log('client connect break')
}
// 订阅
client.subscribe = function(url, fun){
    stompClient.subscribe(url,fun)
}
export default client;


