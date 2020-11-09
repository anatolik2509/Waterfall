package ru.itis.antonov.waterfall.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/saveOffset")
public class SaveOffsetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("offset") != null && req.getParameter("lastArticle") != null){
            resp.addCookie(new Cookie("savedOffset", req.getParameter("offset")));
            resp.addCookie(new Cookie("lastArticle", req.getParameter("lastArticle")));
        }
    }
}
