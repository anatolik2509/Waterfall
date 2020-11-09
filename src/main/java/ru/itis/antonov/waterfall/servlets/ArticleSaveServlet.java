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

@WebServlet("/save/article")
public class ArticleSaveServlet extends HttpServlet {
    private ServletContext context;
    private ArticleService articleService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = config.getServletContext();
        articleService = (ArticleService) context.getAttribute("articleService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("id") != null){
            Article a = Article.builder().id(Long.parseLong(req.getParameter("id"))).build();
            Profile p = (Profile) req.getSession().getAttribute("user");
            if(articleService.isSaved(a, p)){
                articleService.dropArticle(a, p);
            }
            else {
                articleService.saveArticle(a, p);
            }
        }
    }
}
