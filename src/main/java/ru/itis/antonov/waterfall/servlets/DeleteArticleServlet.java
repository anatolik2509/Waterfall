package ru.itis.antonov.waterfall.servlets;

import ru.itis.antonov.waterfall.models.Article;
import ru.itis.antonov.waterfall.models.CommentsOrder;
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

@WebServlet("/delete")
public class DeleteArticleServlet extends HttpServlet {
    private ServletContext context;
    private ArticleService articleService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = config.getServletContext();
        articleService = (ArticleService) context.getAttribute("articleService");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("id") == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long id = Long.parseLong(req.getParameter("id"));
        Article a = articleService.getArticleById(null, id, CommentsOrder.NEW);
        Profile p = (Profile)req.getSession().getAttribute("user");
        if(a.getAuthor().getId() == p.getId()){
            articleService.deleteArticle(a);
        }
        else {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
