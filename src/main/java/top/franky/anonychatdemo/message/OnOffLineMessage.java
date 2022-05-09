package top.franky.anonychatdemo.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

public class OnOffLineMessage extends AbstractMessage {

    public OnOffLineMessage(String type, String name) {
        map = new ConcurrentHashMap<>();
        addAttr("msgType", type);
        addAttr("name", name);
    }

    public OnOffLineMessage(String msgString) {
        map = new ConcurrentHashMap<>();
        JSONObject obj = JSON.parseObject(msgString);
        addAttr("msgType", obj.getString("msgType"));
        addAttr("name", obj.getString("name"));
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
