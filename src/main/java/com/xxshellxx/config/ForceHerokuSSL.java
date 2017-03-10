package com.xxshellxx.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ForceHerokuSSL extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String proto = request.getHeader("x-forwarded-proto");
        return proto == null || "https".equalsIgnoreCase(proto);
    }
}
