package cn.piesat.test.websocket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;


@ServerEndpoint("/webSocket/{uid}")
@Component
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static final AtomicInteger onlineNum = new AtomicInteger(0);

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static final CopyOnWriteArraySet<Session> sessionPools = new CopyOnWriteArraySet<>();

    // 使用一个私有的锁对象来避免潜在的锁竞争
    private final Object lock = new Object();

    /**
     * 有客户端连接成功
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "uid") String uid){
        sessionPools.add(session);
        onlineNum.incrementAndGet();
        log.info(uid + "加入webSocket！当前人数为" + onlineNum);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        sessionPools.remove(session);
        int cnt = onlineNum.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 发送消息
     */
    public void sendMessage(Session session, String message)  {
        // 检查 message 是否为 null
        if (message == null) {
            throw new IllegalArgumentException("消息内容不能为空");
        }

        if (session != null) {
            synchronized (lock) {
                try {
                    // 尝试发送消息，并捕获可能的异常
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    // 异常处理：记录异常信息，根据业务需求可以考虑重试发送等机制
                    System.err.println("发送消息失败，异常信息：" + e.getMessage());
                    // 如果业务逻辑允许，可以考虑重试发送等逻辑
                }
            }
        }
    }

    /**
     * 群发消息
     */
    public void broadCastInfo(String message)  {
        for (Session session : sessionPools) {
            if(session.isOpen()){
                sendMessage(session, message);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        log.error("发生错误");
        if(session.isOpen()){
            onClose(session);
        }
        throwable.printStackTrace();
    }

}