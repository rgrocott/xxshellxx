package com.xxshellxx.config;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SSLFilter implements Filter {

    private FilterConfig filterConfig;

    public static final String X_FORWARDED_PROTO = "x-forwarded-proto";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getHeader(X_FORWARDED_PROTO) != null) {
            if (request.getHeader(X_FORWARDED_PROTO).toLowerCase().indexOf("https") != 0) {
                String pathInfo = request.getPathInfo();
                if (pathInfo == null) {
                    pathInfo = "";
                }
                String serverName = request.getServerName().toLowerCase();
                
                if (serverName.startsWith("xxshellxx.com")) {
                	// This is probably redundant now that the www domain is mapped to static HTML hosted outside of Heroku.
                    response.sendRedirect("http://www.xxshellxx.com" + pathInfo);
                    return;
                }
                response.sendRedirect("https://" + request.getServerName() + pathInfo);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nothing
    }
}