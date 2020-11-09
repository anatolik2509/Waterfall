package ru.itis.antonov.waterfall.servlets;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itis.antonov.waterfall.services.SecurityService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private ServletContext context;
    private SecurityService securityService;
    private Logger logger;

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = config.getServletContext();
        securityService = (SecurityService) context.getAttribute("securityService");
        logger = LoggerFactory.getLogger("AuthServlet");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        securityService.logout(req, req.getSession());
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
