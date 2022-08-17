package cn.zflzqy.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * @author ：zfl
 * @description：
 * @date ：2022/3/5 13:26
 */
@RequestMapping
@Controller
public class IndexController {
    @RequestMapping(value = {"/","index","/page","/page/**"})
    public String index(){
        return  "index";
    }
    @RequestMapping(value = {"/socket"})
    public String socket(){
        return  "socket";
    }
}
