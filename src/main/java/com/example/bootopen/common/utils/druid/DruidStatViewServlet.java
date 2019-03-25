package com.example.bootopen.common.utils.druid;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.alibaba.druid.support.http.StatViewServlet;

@WebServlet(
        urlPatterns= {"/druid/*"},
        initParams= {
                @WebInitParam(name = "allow", value = "127.0.0.1"),
                @WebInitParam(name = "loginUsername", value = "druid"),
                @WebInitParam(name = "loginPassword", value = "druid"),
                @WebInitParam(name = "resetEnable", value = "true")
                // 允许HTML页面上的“Reset All”功能
        })
    public class DruidStatViewServlet extends StatViewServlet implements Servlet {
        private static final long serialVersionUID = 1L;
}
