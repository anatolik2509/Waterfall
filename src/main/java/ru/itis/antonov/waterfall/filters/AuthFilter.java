package ru.itis.antonov.waterfall.filters;

import ru.itis.antonov.waterfall.services.SecurityService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("*")
public class AuthFilter implements Filter {

    private String[] protectedPaths;
    private SecurityService securityService;
    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        protectedPaths = new String[]{"/login", "/registration", "/feed", "/article", "/css", "/js", "/res", "/media"};
        context = filterConfig.getServletContext();
        securityService = (SecurityService)context.getAttribute("securityService");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        if(securityService.isAuthenticated(request, request.getSession())){
            request.setAttribute("auth", true);
            filterChain.doFilter(request, response);
        }
        else {
            request.setAttribute("auth", false);
            boolean isProtected = false;
            for(String s : protectedPaths){
                if(request.getRequestURI().startsWith(request.getContextPath() + s)){
                    isProtected = true;
                }
            }
            if(isProtected){
                filterChain.doFilter(request, response);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/login");
        }

    }

    @Override
    public void destroy() {

    }
}
