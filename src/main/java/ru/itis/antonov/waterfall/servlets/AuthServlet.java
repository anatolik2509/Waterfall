package ru.itis.antonov.waterfall.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itis.antonov.waterfall.exceptions.NoSuchLoginException;
import ru.itis.antonov.waterfall.services.SecurityService;
import ru.itis.antonov.waterfall.exceptions.WrongPasswordException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/login")
public class AuthServlet extends HttpServlet {

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
        if (req.getParameter("email") != null) {
            req.setAttribute("login", req.getParameter("email"));
            try {
                UUID uuid = securityService.authorize(req.getParameter("email"),
                        req.getParameter("password"), req.getSession());
                Cookie c = new Cookie(SecurityService.AUTH_COOKIE_NAME, uuid.toString());
                c.setMaxAge(60*60*24*365);
                resp.addCookie(c);
                resp.sendRedirect(req.getContextPath() + "/profile");
                return;
            } catch (WrongPasswordException e) {
                req.setAttribute("message", "Неверный логин или пароль");
                logger.error("Пароль не верный");
            } catch (NoSuchLoginException e) {
                req.setAttribute("message", "Неверный логин или пароль");
                logger.error("Логин не верный");
            }

        }
        context.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }
}
