package cn.piesat.test.websocket.controller;

import cn.piesat.test.websocket.service.WebSocketServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2024-06-11 9:16.
 *
 * @author zhouxp
 */
@RestController
@RequestMapping("/open/socket")
public class WebSocketController {

    @Value("${mySocket.pwd}")
    public String password;

    @Resource
    private WebSocketServer webSocketServer;

    /**
     * 手机客户端请求接口
     * @param id    发生异常的设备ID
     * @param pwd   密码（实际开发记得加密）
     */
    @PostMapping(value = "/onReceive")
    public void onReceive(String id,String pwd)  {
        if(password.equals(pwd)){  //密码校验一致（这里举例，实际开发还要有个密码加密的校验的），则进行群发
            webSocketServer.broadCastInfo(id);
        }
    }

}
