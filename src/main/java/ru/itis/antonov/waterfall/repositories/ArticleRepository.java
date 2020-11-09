package ru.itis.antonov.waterfall.repositories;

import ru.itis.antonov.waterfall.models.*;

import java.util.Date;
import java.util.List;

public interface ArticleRepository extends CrudRepository<Article> {
    int profileRate(Profile p, Article a);

    List<Article> getByTag(String tag, FeedType type, int begin, int end, Date updated);

    void saveArticle(Profile p, Article a);

    boolean isSaved(Profile p, Article a);

    void deleteSavedArticle(Profile p, Article a);

    void likeArticle(Profile p, Article a);

    void dislikeArticle(Profile p, Article a);

    void removeRate(Profile p, Article a);

    List<Article> feed(Profile p, FeedType type, int begin, int end, Date updated);

    List<Article> getArticlesByAuthor(Profile p, FeedType type, int begin, int end, Date updated);

    List<Article> getArticlesByGroup(Group g, FeedType type, int begin, int end, Date updated);

    List<Article> savedArticles(Profile p, int begin, int end, Date updated);

    int commentCount(Article a);
}
