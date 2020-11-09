package ru.itis.antonov.waterfall.servlets;


import ru.itis.antonov.waterfall.models.Article;
import ru.itis.antonov.waterfall.models.Comment;
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
import java.util.Enumeration;

@WebServlet("/addComment")
public class AddCommentServlet extends HttpServlet {
    private ArticleService articleService;
    private ServletContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = config.getServletContext();
        articleService = (ArticleService) context.getAttribute("articleService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long articleId = Long.parseLong(req.getParameter("id"));
        String content = req.getParameter("content");
        Long parentId = null;
        if(req.getParameter("response_id") != null){
            if(!req.getParameter("response_id").equals("")){
                parentId = Long.parseLong(req.getParameter("response_id"));
            }
        }
        Comment comment = Comment.builder()
                .article(Article.builder().id(articleId).build())
                .author((Profile) req.getSession().getAttribute("user"))
                .content(articleService.replaceHtml(content))
                .parent(Comment.builder().id(parentId).build())
                .build();
        articleService.createComment(comment);
        req.setAttribute("commentObject", comment);
        context.getRequestDispatcher("/WEB-INF/views/comment.jsp").forward(req, resp);
    }
}
