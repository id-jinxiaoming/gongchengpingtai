package com.ff.api.controller;

import com.ff.common.util.CheckUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/api/test")
public class WeixinServletController extends HttpServlet {

    /*
    *验证消息的确来自微信服务器
    */
    @RequestMapping(value = "/doGet",method = RequestMethod.GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 接收微信服务器以Get请求发送的4个参数
        String signature = request.getParameter("signature");//微信加密签名
        String timestamp = request.getParameter("timestamp");//时间戳
        String nonce = request.getParameter("nonce");//随机数
        String echostr = request.getParameter("echostr");//随机字符串

        PrintWriter out = response.getWriter();
        if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
            out.print(echostr);		// 校验通过，原样返回echostr参数内容
        }

    }
}
