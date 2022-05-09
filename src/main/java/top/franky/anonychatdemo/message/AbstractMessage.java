package top.franky.anonychatdemo.message;

import java.util.Map;

public abstract class AbstractMessage {
    Map<String, Object> map;
    public abstract void addAttr(String key, Object value);
    public abstract void removeAttr(String key);
    public abstract String getJsonString();
    @Override
    public abstract String toString();
}
