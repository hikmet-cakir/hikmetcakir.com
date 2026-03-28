package com.hikmetcakir.coreapi.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper res = new ContentCachingResponseWrapper((HttpServletResponse) response);

        chain.doFilter(req, res);

        String requestBody = new String(req.getContentAsByteArray());
        String responseBody = new String(res.getContentAsByteArray());

        System.out.println("REQUEST: " + req.getMethod() + " " + req.getRequestURI());
        System.out.println("REQUEST BODY: " + requestBody);

        System.out.println("RESPONSE STATUS: " + res.getStatus());
        System.out.println("RESPONSE BODY: " + responseBody);

        res.copyBodyToResponse();
    }
}
