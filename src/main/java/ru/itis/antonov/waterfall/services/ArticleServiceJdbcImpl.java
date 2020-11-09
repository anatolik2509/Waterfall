package ru.itis.antonov.waterfall.services;

import ru.itis.antonov.waterfall.models.*;
import ru.itis.antonov.waterfall.repositories.ArticleRepository;
import ru.itis.antonov.waterfall.repositories.CommentRepository;
import ru.itis.antonov.waterfall.repositories.GroupRepository;
import ru.itis.antonov.waterfall.repositories.ProfileRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class ArticleServiceJdbcImpl implements ArticleService {

    private ArticleRepository articleRepository;

    private CommentRepository commentRepository;

    private GroupRepository groupRepository;

    private ProfileRepository profileRepository;

    public ArticleServiceJdbcImpl(ArticleRepository articleRepository, CommentRepository commentRepository, GroupRepository groupRepository, ProfileRepository profileRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.groupRepository = groupRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public Article getArticleById(Profile user, long id, CommentsOrder order) {
        Optional<Article> a = articleRepository.findById(id);
        a.ifPresent(article -> {
            article.setComments(getArticleComments(user, article, order));
            article.setGroup(groupRepository.getWithName(article.getGroup().getId()));
            article.setAuthor(profileRepository.getWithName(article.getAuthor().getId()));
            article.setUserRate(isRated(article, user));
            article.setSaved(isSaved(article, user));
        });
        return a.orElse(null);
    }

    @Override
    public List<Article> getFeed(Profile user, FeedType feedType, boolean viewed, int begin, int end, Timestamp updated) {
        List<Article> articles = articleRepository.feed(null, feedType, begin, end, updated);
        for (Article a : articles) {
            if (a.getGroup().getId() != -1) {
                a.setGroup(groupRepository.getWithName(a.getGroup().getId()));
            }
            a.setAuthor(profileRepository.getWithName(a.getAuthor().getId()));
            a.setCommentAmount(articleRepository.commentCount(a));
            if(user != null){
                a.setUserRate(isRated(a, user));
                a.setSaved(isSaved(a, user));
            }
        }
        return articles;
    }

    @Override
    public List<Article> getFeed(Profile p, Profile user, FeedType feedType, boolean viewed, int begin, int end, Timestamp updated) {
        List<Article> articles = articleRepository.feed(p, feedType, begin, end, updated);
        for (Article a : articles) {
            if (a.getGroup().getId() != -1) {
                a.setGroup(groupRepository.getWithName(a.getGroup().getId()));
            }
            a.setAuthor(profileRepository.getWithName(a.getAuthor().getId()));
            a.setCommentAmount(countComments(a.getComments()));
            if(user != null){
                a.setUserRate(isRated(a, user));
                a.setSaved(isSaved(a, user));
            }
        }
        return articles;
    }

    @Override
    public List<Comment> getArticleComments(Article a, CommentsOrder order) {
        List<Comment> l = commentRepository.getArticleComments(a, order);
        addProfileNames(l);
        return l;
    }

    @Override
    public List<Comment> getArticleComments(Profile p, Article a, CommentsOrder order) {
        List<Comment> l = commentRepository.getArticleComments(a, order);
        if(p != null) {
            addUsersRates(p, l);
        }
        addProfileNames(l);
        return l;
    }

    private void addUsersRates(Profile p, List<Comment> comments){
        for (Comment c : comments){
            c.setUserRate(commentRepository.getProfileRate(p, c));
            System.out.println(c.getUserRate());
            addUsersRates(p, c.getChildes());
        }
    }

    private void addProfileNames(List<Comment> comments){
        if(comments == null) return;
        for(Comment c : comments){
            c.setAuthor(profileRepository.getWithName(c.getAuthor().getId()));
            addProfileNames(c.getChildes());
        }
    }

    private int countComments(List<Comment> comments){
        if(comments == null) return 0;
        int result = 0;
        for(Comment c : comments){
            result += countComments(c.getChildes()) + 1;
        }
        return result;
    }

    @Override
    public List<Comment> getChildComments(Comment c, CommentsOrder order) {
        return null;
    }

    @Override
    public void updateArticle(Article a) {
        articleRepository.update(a);
    }

    @Override
    public void deleteArticle(Article a) {
        articleRepository.delete(a);
    }

    @Override
    public void createArticle(Article a) {
        articleRepository.save(a);
    }

    @Override
    public void like(Article a, Profile p) {
        articleRepository.removeRate(p, a);
        articleRepository.likeArticle(p, a);
    }

    @Override
    public void dislike(Article a, Profile p) {
        articleRepository.removeRate(p, a);
        articleRepository.dislikeArticle(p, a);
    }

    @Override
    public void undoRate(Article a, Profile p) {
        articleRepository.removeRate(p, a);
    }

    @Override
    public void like(Comment c, Profile p) {
        commentRepository.removeRate(p, c);
        commentRepository.likeComment(p, c);
    }

    @Override
    public void dislike(Comment c, Profile p) {
        commentRepository.removeRate(p, c);
        commentRepository.dislikeComment(p, c);
    }

    @Override
    public void undoRate(Comment c, Profile p) {
        commentRepository.removeRate(p, c);
    }

    @Override
    public int isRated(Article a, Profile p) {
        return articleRepository.profileRate(p, a);
    }

    @Override
    public boolean isSaved(Article a, Profile p) {
        return articleRepository.isSaved(p, a);
    }

    @Override
    public int isRated(Comment c, Profile p) {
        return commentRepository.getProfileRate(p, c);
    }

    @Override
    public boolean isSaved(Comment c, Profile p) {
        return commentRepository.isSaved(p, c);
    }

    @Override
    public void saveArticle(Article a, Profile p) {
        articleRepository.saveArticle(p, a);
    }

    @Override
    public void dropArticle(Article a, Profile p) {
        articleRepository.deleteSavedArticle(p, a);
    }

    @Override
    public void saveComment(Comment c, Profile p) {
        commentRepository.saveComment(p, c);
    }

    @Override
    public void dropComment(Comment c, Profile p) {
        commentRepository.deleteSavedArticle(p, c);
    }

    @Override
    public String replaceHtml(String raw) {
        String result;
        result = raw.replace("&", "&amp;");
        result = result.replace("<", "&lt;");
        result = result.replace(">", "&gt;");
        return result;
    }

    @Override
    public String replaceHtmlReverse(String raw) {
        String result;
        result = raw.replace("&gt;", ">");
        result = result.replace("&lt;", "<");
        result = result.replace("&amp;", "&");
        result = result.replace("/", "\\/");
        return result;
    }

    @Override
    public void createComment(Comment c) {
        commentRepository.save(c);
    }

    @Override
    public List<Article> getUserArticle(Profile p, int begin, int end, Timestamp updated) {
        List<Article> articles = articleRepository.getArticlesByAuthor(p, FeedType.FRESH, begin, end, updated);
        for (Article a : articles) {
            if (a.getGroup().getId() != -1) {
                a.setGroup(groupRepository.getWithName(a.getGroup().getId()));
            }
            a.setAuthor(profileRepository.getWithName(a.getAuthor().getId()));
            a.setCommentAmount(countComments(a.getComments()));
        }
        return articles;
    }

    @Override
    public List<Article> getSavedArticle(Profile p, int begin, int end, Timestamp updated) {
        List<Article> articles = articleRepository.savedArticles(p, begin, end, updated);
        for (Article a : articles) {
            if (a.getGroup().getId() != -1) {
                a.setGroup(groupRepository.getWithName(a.getGroup().getId()));
            }
            a.setAuthor(profileRepository.getWithName(a.getAuthor().getId()));
            a.setCommentAmount(countComments(a.getComments()));
            a.setUserRate(isRated(a, p));
            a.setSaved(isSaved(a, p));
        }
        return articles;
    }
}
