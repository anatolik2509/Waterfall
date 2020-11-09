package ru.itis.antonov.waterfall.services;

import ru.itis.antonov.waterfall.models.*;

import java.sql.Timestamp;
import java.util.List;

public interface ArticleService {
    Article getArticleById(Profile user, long id, CommentsOrder order);

    List<Article> getFeed(Profile user, FeedType feedType, boolean viewed, int begin, int end, Timestamp updated);

    List<Article> getFeed(Profile p, Profile user, FeedType feedType, boolean viewed, int begin, int end, Timestamp updated);

    List<Comment> getArticleComments(Article a, CommentsOrder order);

    List<Comment> getArticleComments(Profile p, Article a, CommentsOrder order);

    List<Comment> getChildComments(Comment c, CommentsOrder order);

    void updateArticle(Article a);

    void deleteArticle(Article a);

    void createArticle(Article a);

    void like(Article a, Profile p);

    void dislike(Article a, Profile p);

    void undoRate(Article a, Profile p);

    void like(Comment c, Profile p);

    void dislike(Comment c, Profile p);

    void undoRate(Comment c, Profile p);

    int isRated(Article a, Profile p);

    boolean isSaved(Article a, Profile p);

    int isRated(Comment c, Profile p);

    boolean isSaved(Comment c, Profile p);

    void saveArticle(Article a, Profile p);

    void dropArticle(Article a, Profile p);

    void saveComment(Comment c, Profile p);

    void dropComment(Comment c, Profile p);

    String replaceHtml(String raw);

    String replaceHtmlReverse(String raw);

    void createComment(Comment c);

    List<Article> getUserArticle(Profile p, int begin, int end, Timestamp updated);

    List<Article> getSavedArticle(Profile p, int begin, int end, Timestamp updated);
}
