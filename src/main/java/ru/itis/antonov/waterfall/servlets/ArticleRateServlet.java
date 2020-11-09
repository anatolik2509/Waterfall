package ru.itis.antonov.waterfall.servlets;

import ru.itis.antonov.waterfall.models.Article;
import ru.itis.antonov.waterfall.models.Profile;
import ru.itis.antonov.waterfall.services.ArticleService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/rate/article")
public class ArticleRateServlet extends HttpServlet {
    private ServletContext context;
    private ArticleService articleService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = config.getServletContext();
        articleService = (ArticleService) context.getAttribute("articleService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("id") != null && req.getParameter("value") != null){
            switch (req.getParameter("value")){
                case "-1":
                    articleService.dislike(Article.builder().id(Long.parseLong(req.getParameter("id"))).build(),
                            (Profile) req.getSession().getAttribute("user"));
                    break;
                case "0":
                    articleService.undoRate(Article.builder().id(Long.parseLong(req.getParameter("id"))).build(),
                            (Profile) req.getSession().getAttribute("user"));
                    break;
                case "1":
                    articleService.like(Article.builder().id(Long.parseLong(req.getParameter("id"))).build(),
                            (Profile) req.getSession().getAttribute("user"));
                    break;
            }
        }
    }
}
