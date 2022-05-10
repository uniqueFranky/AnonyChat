package top.franky.anonychatdemo.websocket;

import org.springframework.stereotype.Component;
import top.franky.anonychatdemo.message.*;
import top.franky.anonychatdemo.messagehandler.MessageHandler;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/chat/{username}")
public class WebSocket {

    private static int onlineNum = 0;
    private static Map<String, WebSocket> name2wb = new ConcurrentHashMap<>();
    private Session session;
    private String username;
    private static List<String> onlineUsers = new ArrayList<>();

    @OnOpen
    public void onOpen(@PathParam("username") String name, Session session) {
        onlineNum++;
        if(name2wb.get(name) != null) {
            AbstractMessage rjctMsg = new RejectConnectionMessage(name, "匿名名称重复！");
            session.getAsyncRemote().sendText(rjctMsg.getJsonString());
            return;
        }
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
        MessageHandler.handleMessgae(msg);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public static void sendMsgToSingleSocketByName(AbstractMessage msg, String to) {
        sendMsgToSingleSocketByWB(msg, name2wb.get(to));
    }
    private static void sendMsgToSingleSocketByWB(AbstractMessage msg, WebSocket to) {
        if(to == null) {
            return;
        }
        to.session.getAsyncRemote().sendText(msg.getJsonString());
    }

    public static void sendMsgToAll(AbstractMessage msg) {

        for(WebSocket ws: name2wb.values()) {
            System.out.println("Send: " + msg + ", to: " + ws.username);
            sendMsgToSingleSocketByWB(msg, ws);
        }
    }
}
