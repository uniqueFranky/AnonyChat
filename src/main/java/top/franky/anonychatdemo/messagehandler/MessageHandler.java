package top.franky.anonychatdemo.messagehandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import top.franky.anonychatdemo.message.AbstractMessage;
import top.franky.anonychatdemo.message.NormalMessage;
import top.franky.anonychatdemo.websocket.WebSocket;

public  class MessageHandler {

    public static JSONObject parseStringToJsonObject(String msgString) {
        JSONObject obj = JSON.parseObject(msgString);
        return obj;
    }

    public static String getMsgType(String msgString) {
        JSONObject obj = parseStringToJsonObject(msgString);
        return obj.getString("msgType");
    }

    public static void handleMessgae(AbstractMessage msg) {
        //Normal Message
        if(msg instanceof NormalMessage) {

            String to = ((NormalMessage) msg).getTo();
            String from = ((NormalMessage) msg).getFrom();
            if("All".equals(to)) {
                WebSocket.sendMsgToAll(msg);
            } else {
                WebSocket.sendMsgToSingleSocketByName(msg, to);
                if(to.equals(from) == false) {
                    WebSocket.sendMsgToSingleSocketByName(msg, from);
                }
            }
        }
    }

}
