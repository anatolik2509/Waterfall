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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/edit")
public class ArticleEditServlet extends HttpServlet {
    private ServletContext context;
    private ArticleService articleService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = config.getServletContext();
        articleService = (ArticleService) context.getAttribute("articleService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id") != null) {
            Article a = articleService.getArticleById(null, Long.parseLong(req.getParameter("id")), CommentsOrder.OLD);
            if(a == null){
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            if(a.getAuthor().getId() != ((Profile)req.getSession().getAttribute("user")).getId()){
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            String content = a.getContent();
            content = content.replace("\n", "&shy;");
            Pattern p = Pattern.compile("(<p class=\"article-p\">([^<>]*)</p>)|(<img class=\"article-img\" src=\"([^<>]*)\">)", Pattern.DOTALL);
            Matcher m = p.matcher(content);
            List<String> list = new ArrayList<>();
            while (m.find()){
                if(m.group(2) != null){
                    list.add("text:" + articleService.replaceHtmlReverse(m.group(2)));
                }
                if(m.group(4) != null){
                    list.add("img:" + articleService.replaceHtmlReverse(m.group(4)));
                }
            }
            req.setAttribute("list", list);
            req.setAttribute("a_id", req.getParameter("id"));
            req.setAttribute("title", a.getTitle());
        }
        context.getRequestDispatcher("/WEB-INF/views/article_edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        if(title == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        List<String> article = new ArrayList<>();
        String in;
        int count = 0;
        while ((in = req.getParameter("part-" + count)) != null) {
            if (req.getParameter("type-" + count) != null) {
                if (req.getParameter("type-" + count).equals("img")) {
                    article.add("<img class=\"article-img\" src=\"" + context.getContextPath() + "/media?path=" + articleService.replaceHtml(in) + "\">");
                } else {
                    article.add("<p class=\"article-p\">" + articleService.replaceHtml(in) + "</p>");
                }
            }
            count++;
        }
        StringBuilder content = new StringBuilder();
        for(String s : article){
            content.append(s).append('\n');
        }
        Article a = Article.builder()
                .rate(0)
                .title(title)
                .content(content.toString())
                .author((Profile)req.getSession().getAttribute("user"))
                .build();
        if(req.getParameter("id") != null){
            Article b = articleService.getArticleById(null, Long.parseLong(req.getParameter("id")), CommentsOrder.OLD);
            if(b == null){
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            if(b.getAuthor().getId() != ((Profile)req.getSession().getAttribute("user")).getId()){
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            a.setId(Long.parseLong(req.getParameter("id")));
            articleService.updateArticle(a);
        }
        else {
            articleService.createArticle(a);
        }
        resp.sendRedirect(req.getContextPath() + "/userArticles");
    }
}
