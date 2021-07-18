package com.carsonlius.websocket.websocketdemo.handler;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/testWebSocket/{id}/{name}")
@RestController
public class TestWebSocket {

    // 用来记录当前连接数的变量
    private static volatile int onlineCount = 0;

    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
    private static CopyOnWriteArraySet<TestWebSocket> webSocketSet = new CopyOnWriteArraySet<TestWebSocket>();

    // 与某个客户端的连接会话，需要通过它来与客户端进行数据收发
    private Session session;

    private static final Logger LOGGER = LoggerFactory.getLogger(TestWebSocket.class);

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "name") String name, @PathParam(value = "id") Integer id){
        this.session = session;
        webSocketSet.add(this);
        System.out.println("open a websocket id=" + id + "name=" + name);

    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        System.out.println("Close a websocket. ");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        System.out.println("Receive a message from client: " + message);
        if (message.equals("hello world")) {
            sendMessage(message + "nick" + "哈哈");
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        LOGGER.error("Error while websocket. ", error);
    }

    public void sendMessage(String message) throws Exception {
        if (this.session.isOpen()) {
            this.session.getBasicRemote().sendText("Send a message from server. " + message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        TestWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        TestWebSocket.onlineCount--;
    }
}
