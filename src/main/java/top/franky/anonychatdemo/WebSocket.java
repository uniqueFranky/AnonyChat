package top.franky.anonychatdemo;

import org.springframework.stereotype.Component;
import top.franky.anonychatdemo.message.*;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/chat/{username}")
public class WebSocket {

    public static int onlineNum = 0;
    private static Map<String, WebSocket> name2wb = new ConcurrentHashMap<>();
    private Session session;
    private String username;
    private static List<String> onlineUsers = new ArrayList<>();

    @OnOpen
    public void onOpen(@PathParam("username") String name, Session session) {
        onlineNum++;
        this.username = name;
        this.session = session;
        AbstractMessage msg = new OnOffLineMessage("ConnectionEstablished", name);
        name2wb.put(name, this);
        onlineUsers.add(name);
        System.out.println(onlineUsers.size());
        msg.addAttr("onlineUsers", onlineUsers);
        sendMsgToAll(msg);
    }

    @OnClose()
    public void onClose() {
        onlineNum--;
        name2wb.remove(username);
        AbstractMessage msg = new OnOffLineMessage("ConnectionCut", username);
        onlineUsers.remove(username);
        msg.addAttr("onlineUsers", onlineUsers);
        sendMsgToAll(msg);
    }

    @OnMessage
    public void onMessage(String msgString, Session session) {
        AbstractMessage msg = MessageBuilder.build(msgString);

        //Normal Message
        if(msg instanceof NormalMessage) {

            String to = ((NormalMessage) msg).getTo();
            if("All".equals(to)) {
                sendMsgToAll(msg);
            } else {
                sendMsgToSingleSocketByName(msg, to);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    private void sendMsgToSingleSocketByName(AbstractMessage msg, String to) {
        sendMsgToSingleSocketByWB(msg, name2wb.get(to));
    }
    private void sendMsgToSingleSocketByWB(AbstractMessage msg, WebSocket to) {
        to.session.getAsyncRemote().sendText(msg.getJsonString());
    }

    private void sendMsgToAll(AbstractMessage msg) {

        for(WebSocket ws: name2wb.values()) {
            System.out.println("Send: " + msg + ", to: " + ws.username);
            sendMsgToSingleSocketByWB(msg, ws);
        }
    }
}
