package top.franky.anonychatdemo.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

public class RejectConnectionMessage extends AbstractMessage {

    public RejectConnectionMessage(String to, String reason) {
        map = new ConcurrentHashMap<>();
        addAttr("to", to);
        addAttr("reason", reason);
        addAttr("msgType", "ConnectionRejected");
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

    @Override
    public String toString() {
        return getJsonString();
    }
}
