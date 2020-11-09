package ru.itis.antonov.waterfall.servlets;

import ru.itis.antonov.waterfall.exceptions.WeakPasswordException;
import ru.itis.antonov.waterfall.models.Profile;
import ru.itis.antonov.waterfall.exceptions.InvalidEmailException;
import ru.itis.antonov.waterfall.exceptions.OccupiedLoginException;
import ru.itis.antonov.waterfall.services.SecurityService;

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

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    private ServletContext context;
    private SecurityService securityService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = config.getServletContext();
        securityService = (SecurityService) context.getAttribute("securityService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        context.getRequestDispatcher("/WEB-INF/views/registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("password").equals(req.getParameter("password-repeat"))){
            Profile profile = Profile.builder()
                    .email(req.getParameter("email"))
                    .nickname(req.getParameter("nick"))
                    .build();
            try {
                UUID uuid = securityService.registration(profile, req.getParameter("password"), req.getSession());
                Cookie c = new Cookie(SecurityService.AUTH_COOKIE_NAME, uuid.toString());
                c.setMaxAge(60*60*24*365);
                resp.addCookie(c);
                resp.sendRedirect(req.getContextPath() + "/profile");
                return;
            } catch (InvalidEmailException e){
                req.setAttribute("message", "Неверный email");
            } catch (OccupiedLoginException e){
                req.setAttribute("message", "Email уже зарегистрирован");
            } catch (WeakPasswordException e){
                req.setAttribute("message", "Пароль слишком короткий(мин. 6 символов)");
            }
        }
        else {
            req.setAttribute("message", "Пароли не совпадают");
        }
        context.getRequestDispatcher("/WEB-INF/views/registration.jsp").forward(req, resp);
    }
}
