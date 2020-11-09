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
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/feed")
public class FeedServlet extends HttpServlet {
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
                if(c.getName().equals("feedType")){
                    req.setAttribute("feedType", c.getValue());
                }
            }
            if(req.getParameter("feedType") != null) {
                req.setAttribute("feedType", req.getParameter("feedType"));
            }
            context.getRequestDispatcher("/WEB-INF/views/feed.jsp").forward(req, resp);
        }
        else {
            int begin = Integer.parseInt(req.getParameter("begin"));
            int end = Integer.parseInt(req.getParameter("end"));
            Timestamp updated = new Timestamp(Long.parseLong(req.getParameter("updated")));
            FeedType type = FeedType.TOP_AT_ALL_TIME;
            String feedParam = req.getParameter("feedType");
            if(feedParam == null){
                feedParam = "";
            }
            if(feedParam.equals("")){
                for(Cookie c : req.getCookies()){
                    if(c.getName().equals("feedType")){
                        feedParam = c.getValue();
                    }
                }
            }
            switch (feedParam){
                case "fr":
                    type = FeedType.FRESH;
                    resp.addCookie(new Cookie("feedType", "fr"));
                    break;
                case "d":
                    type = FeedType.TOP_OF_DAY;
                    resp.addCookie(new Cookie("feedType", "d"));
                    break;
                case "w":
                    type = FeedType.TOP_OF_WEEK;
                    resp.addCookie(new Cookie("feedType", "w"));
                    break;
                case "m":
                    type = FeedType.TOP_OF_MONTH;
                    resp.addCookie(new Cookie("feedType", "m"));
                    break;
                case "y":
                    type = FeedType.TOP_OF_YEAR;
                    resp.addCookie(new Cookie("feedType", "y"));
                    break;
                case "all":
                    type = FeedType.TOP_AT_ALL_TIME;
                    resp.addCookie(new Cookie("feedType", "all"));
                    break;
            }
            List<Article> l = articleService.getFeed((Profile) req.getSession().getAttribute("user"), type, true, begin, end, updated);
            req.setAttribute("articleList", l);
            context.getRequestDispatcher("/WEB-INF/views/article_list.jsp").forward(req, resp);
        }
    }
}
