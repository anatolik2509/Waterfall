package ru.itis.antonov.waterfall.repositories;

import ru.itis.antonov.waterfall.models.Article;
import ru.itis.antonov.waterfall.models.Comment;
import ru.itis.antonov.waterfall.models.CommentsOrder;
import ru.itis.antonov.waterfall.models.Profile;

import java.sql.Date;
import java.util.List;

public interface CommentRepository extends CrudRepository<Comment>{

    List<Comment> getNestedComment(Comment c, CommentsOrder order, int nesting);

    int getProfileRate(Profile p, Comment c);

    void saveComment(Profile p,Comment c);

    boolean isSaved(Profile p, Comment c);

    void deleteSavedArticle(Profile p, Comment c);

    List<Comment> getSavedComments(Profile p, int begin, int end, Date updated);

    List<String> getAttachmentsPaths(Comment c);

    List<Comment> getArticleComments(Article a, CommentsOrder order);

    void likeComment(Profile p, Comment c);

    void dislikeComment(Profile p, Comment c);

    void removeRate(Profile p, Comment c);

    void addAttachment(Comment c, String path);

    void removeAttachment(Comment c, String path);
}
