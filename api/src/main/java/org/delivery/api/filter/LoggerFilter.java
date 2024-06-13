package org.delivery.api.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
@Component
@Slf4j
public class LoggerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        ContentCachingResponseWrapper res = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        log.info("INIT URI : {}", req.getRequestURI());

        filterChain.doFilter(req, res);

        // request 정보
        Enumeration<String> headerNames = req.getHeaderNames();
        StringBuilder requestHeaderValues = new StringBuilder();

        headerNames.asIterator().forEachRemaining(headerKey -> {
            String headerValue = req.getHeader(headerKey);

            requestHeaderValues
                    .append("[")
                    .append(headerKey)
                    .append(" : ")
                    .append(headerValue)
                    .append("] ");
        });

        String requestBody = new String(req.getContentAsByteArray());
        String uri = req.getRequestURI();
        String method = req.getMethod();

        log.info(">>>>> uri : {} , method : {} , header : {} , body : {}", uri, method, requestHeaderValues, requestBody);

        // response 정보
        StringBuilder responseHeaderValues = new StringBuilder();

        // Collection 반환
        res.getHeaderNames().forEach(headerKey -> {
            String headerValue = res.getHeader(headerKey);

            responseHeaderValues
                    .append("[")
                    .append(headerKey)
                    .append(" : ")
                    .append(headerValue)
                    .append("] ");
        });

        String responseBody = new String(res.getContentAsByteArray());
        log.info("<<<<< uri : {} , method : {} , header : {} , body : {}", uri, method, responseHeaderValues, responseBody);

        res.copyBodyToResponse();
    }
}
