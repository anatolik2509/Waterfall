package ru.itis.antonov.waterfall.servlets;

import ru.itis.antonov.waterfall.models.Article;
import ru.itis.antonov.waterfall.models.CommentsOrder;
import ru.itis.antonov.waterfall.models.Profile;
import ru.itis.antonov.waterfall.services.ArticleService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/article")
public class ArticleServlet extends HttpServlet {
    private ArticleService articleService;
    private ServletContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = config.getServletContext();
        articleService = (ArticleService) context.getAttribute("articleService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("offset") != null){
            resp.addCookie(new Cookie("savedOffset", req.getParameter("offset")));
        }
        Article a = articleService.getArticleById((Profile) req.getSession().getAttribute("user"),
                Long.parseLong(req.getParameter("id")), CommentsOrder.OLD);
        if(req.getAttribute("user") != null) {
            a.setUserRate(articleService.isRated(a, (Profile) req.getSession().getAttribute("user")));
            a.setSaved(articleService.isSaved(a, (Profile) req.getSession().getAttribute("user")));

        }
        req.setAttribute("article", a);
        context.getRequestDispatcher("/WEB-INF/views/article_view.jsp").forward(req, resp);
    }
}
