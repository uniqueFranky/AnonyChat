package top.franky.anonychatdemo.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class MessageBuilder {
    public static AbstractMessage build(String msgString) {
        JSONObject obj = JSON.parseObject(msgString);
        String type = obj.getString("msgType");
        if("Normal".equals(type)) {
            return new NormalMessage(msgString);
        }
        if("ConnectionEstablished".equals(type) || "ConnectionCut".equals(type)) {
            return new OnOffLineMessage(msgString);
        }
        return null;
    }
}
