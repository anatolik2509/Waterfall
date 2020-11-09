package ru.itis.antonov.waterfall.servlets;

import ru.itis.antonov.waterfall.models.Article;
import ru.itis.antonov.waterfall.models.FeedType;
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
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/saved")
public class SavedServlet extends HttpServlet {
    private ServletContext context;
    private ArticleService articleService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = config.getServletContext();
        articleService = (ArticleService) context.getAttribute("articleService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("begin") == null){
            for (Cookie c : req.getCookies()){
                if(c.getName().equals("savedOffset")){
                    req.setAttribute("savedOffset", c.getValue());
                    resp.addCookie(new Cookie("savedOffset", "0"));
                }
                if(c.getName().equals("lastArticle")){
                    req.setAttribute("lastArticle", c.getValue());
                    resp.addCookie(new Cookie("lastArticle", "0"));
                }
            }
            context.getRequestDispatcher("/WEB-INF/views/saved.jsp").forward(req, resp);
        }
        else {
            int begin = Integer.parseInt(req.getParameter("begin"));
            int end = Integer.parseInt(req.getParameter("end"));
            Timestamp updated = new Timestamp(Long.parseLong(req.getParameter("updated")));
            List<Article> l = articleService.getSavedArticle((Profile)req.getSession().getAttribute("user"),
                    begin, end, updated);
            req.setAttribute("articleList", l);
            context.getRequestDispatcher("/WEB-INF/views/article_list.jsp").forward(req, resp);
        }
    }
}
