package ru.itis.antonov.waterfall.filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebFilter("*")
public class LoggingFilter implements Filter {

    private static Logger logger;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger = LoggerFactory.getLogger("Request Logger");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String out = "";
        out += request.getMethod() + " " + request.getRequestURI() + "\n";
        String header;
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()){
            header = headers.nextElement();
            out += header + ": " + request.getHeader(header) + "\n";
        }
        logger.info(out);
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
