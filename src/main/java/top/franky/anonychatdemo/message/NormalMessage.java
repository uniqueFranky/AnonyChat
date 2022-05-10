package top.franky.anonychatdemo.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class NormalMessage extends AbstractMessage {

    public NormalMessage(String from, String to, String context) {
        map = new ConcurrentHashMap<>();
        addAttr("from", from);
        addAttr("to", to);
        addAttr("msgContext", context);
        addAttr("msgType", "Normal");
        Date now = new Date();
        addAttr("date", now.toLocaleString());
    }

    public NormalMessage(String msgString) {
        map = new ConcurrentHashMap<>();
        JSONObject obj = JSON.parseObject(msgString);
        addAttr("from", obj.getString("from"));
        addAttr("to", obj.getString("to"));
        addAttr("msgContent", obj.getString("msgContent"));
        addAttr("msgType", obj.getString("msgType"));
        addAttr("date", obj.getString("date"));
    }

    @Override
    public void addAttr(String key, Object value) {
        map.put(key, value);
    }

    @Override
    public void removeAttr(String key) {
        map.remove(key);
    }

    @Override
    public String getJsonString() {
        return JSON.toJSONString(map);
    }

    public String getTo() {
        return (String) map.get("to");
    }
    public String getFrom() {
        return (String) map.get("from");
    }
    @Override
    public String toString() {
        return getJsonString();
    }
}
