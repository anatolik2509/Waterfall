package ru.itis.antonov.waterfall.repositories;

import ru.itis.antonov.waterfall.models.Article;
import ru.itis.antonov.waterfall.models.Comment;
import ru.itis.antonov.waterfall.models.CommentsOrder;
import ru.itis.antonov.waterfall.models.Profile;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentRepositoryJdbcImpl implements CommentRepository {

    //language=SQL
    private static final String SQL_GET_CHILD_COMMENTS_OF_POST_NEW = "WITH RECURSIVE r AS((" +
            "SELECT id, author_id, content, parent_comment_id, created, article_id, rate, 1 AS level FROM comment" +
            " WHERE article_id=? AND parent_comment_id  IS NULL) " +
            "UNION " +
            "SELECT comment.id, comment.author_id, comment.content, comment.parent_comment_id," +
            "comment.created, comment.article_id, comment.rate, level + 1 AS level FROM comment JOIN r " +
            "ON r.id = comment.parent_comment_id)" +
            "SELECT * FROM r ORDER BY level , created DESC";

    //language=SQL
    private static final String SQL_GET_CHILD_COMMENTS_OF_POST_OLD = "WITH RECURSIVE r AS((" +
            "SELECT id, author_id, content, parent_comment_id, created, article_id, rate, 1 AS level FROM comment" +
            " WHERE article_id=? AND parent_comment_id  IS NULL) " +
            "UNION " +
            "SELECT comment.id, comment.author_id, comment.content, comment.parent_comment_id," +
            "comment.created, comment.article_id, comment.rate, level + 1 AS level FROM comment JOIN r " +
            "ON r.id = comment.parent_comment_id)" +
            "SELECT * FROM r ORDER BY level, created";

    //language=SQL
    private static final String SQL_GET_CHILD_COMMENTS_OF_POST_TOP = "WITH RECURSIVE r AS((" +
            "SELECT id, author_id, content, parent_comment_id, created, article_id, rate, 1 AS level FROM comment" +
            " WHERE article_id=? AND parent_comment_id  IS NULL) " +
            "UNION " +
            "SELECT comment.id, comment.author_id, comment.content, comment.parent_comment_id," +
            "comment.created, comment.article_id, comment.rate, level + 1 AS level FROM comment JOIN r " +
            "ON r.id = comment.parent_comment_id)" +
            "SELECT * FROM r ORDER BY level, rate DESC";

    //language=SQL
    private static final String SQL_GET_CHILD_COMMENTS_OF_COMMENT_NEW = "WITH RECURSIVE r AS((" +
            "SELECT id, author_id, content, parent_comment_id, created, article_id, 0 AS level FROM comment " +
            "WHERE parent_comment_id=?) " +
            "UNION " +
            "SELECT comment.id, comment.author_id, comment.content, comment.parent_comment_id," +
            "comment.created, comment.article_id,  level+1 AS level FROM comment JOIN r " +
            "ON r.id = comment.parent_comment_id WHERE level < ?)" +
            "SELECT * FROM r";

    //language=SQL
    private static final String SQL_INSERT_COMMENT =
            "INSERT INTO comment (author_id, content, parent_comment_id, created, article_id, rate) " +
            "VALUES (?, ?, ?, now(), ?, 0) RETURNING id";

    //language=SQL
    private static final String SQL_INSERT_ATTACHMENTS =
            "INSERT INTO comment_attachments (comment_id, path) VALUES (?, ?)";

    //language=SQL
    private static final String SQL_UPDATE =
            "UPDATE comment SET content=? WHERE id=?";

    //language=SQL
    private static final String SQL_REMOVE_ATTACHMENT=
            "DELETE FROM comment_attachments WHERE comment_id=? AND path=?";

    //language=SQL
    private static final String SQL_ADD_RATE =
            "INSERT INTO comment_rate (profile_id, comment_id, value) " +
                    "VALUES (?, ?, ?)";

    //language=SQL
    private static final String SQL_REMOVE_RATE =
            "DELETE FROM comment_rate WHERE profile_id=? AND comment_id=? RETURNING value";

    //language=SQL
    private static final String SQL_UPDATE_RATE_COUNT =
            "UPDATE comment SET rate=rate+? WHERE id=?";

    //language=SQL
    private static final String SQL_DELETE_COMMENT =
            "DELETE FROM comment WHERE id=?";

    //language=SQL
    private static final String SQL_GET_PROFILE_RATE =
            "SELECT value FROM comment_rate WHERE comment_id=? AND profile_id=?";

    //language=SQL
    private static final String SQL_COMMENT_BY_ID =
            "SELECT * FROM comment WHERE id=?";

    //language=SQL
    private static final String SQL_SAVE_COMMENT =
            "INSERT INTO saved_comment (profile_id, comment_id, added) VALUES (?, ?, now())";

    //language=SQL
    private static final String SQL_DROP_SAVED_COMMENT =
            "DELETE FROM saved_comment WHERE profile_id=? AND comment_id=?";

    //language=SQL
    private static final String SQL_GET_SAVED_COMMENT =
            "SELECT * FROM saved_comment WHERE profile_id=? ORDER BY added DESC";

    //language=SQL
    private static final String SQL_IS_SAVED =
            "SELECT 1 FROM saved_comment WHERE profile_id=? AND comment_id=?";

    //language=SQL
    private static final String SQL_ATTACHMENTS =
            "SELECT path FROM comment_attachments WHERE comment_id=?";

    //language=SQL
    private static final String SQL_GET_SAVED_COMMENTS =
            "SELECT comment.id AS id, author_id, content, parent_comment_id, created, article_id, rate " +
                    "FROM comment JOIN saved_comment sc " +
                    "on comment.id = sc.comment_id AND profile_id = ? AND created < ? " +
                    "ORDER BY added DESC LIMIT ? OFFSET ?";

    private RowMapper<Comment> commentRowMapper = row -> Comment.builder()
            .id(row.getLong("id"))
            .article(Article.builder().id(row.getLong("article_id")).build())
            .author(Profile.builder().id(row.getLong("author_id")).build())
            .content(row.getString("content"))
            .parent(Comment.builder().id(row.getLong("parent_comment_id")).build())
            .childes(new ArrayList<>())
            .date(row.getTimestamp("created"))
            .rate(row.getInt("rate"))
            .build();


    private DataSource dataSource;

    private SimpleJdbcTemplate template;

    public CommentRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        template = new SimpleJdbcTemplate(dataSource);
    }

    @Override
    public List<Comment> getArticleComments(Article a, CommentsOrder order) {
        List<Comment> comments;
        switch (order) {
            case NEW:
                 comments = template.query(SQL_GET_CHILD_COMMENTS_OF_POST_NEW,
                        commentRowMapper, a.getId());
                rebuild(comments);
                return comments;
            case OLD:
                comments = template.query(SQL_GET_CHILD_COMMENTS_OF_POST_OLD,
                        commentRowMapper, a.getId());
                rebuild(comments);
                return comments;
            case POPULAR:
                comments = template.query(SQL_GET_CHILD_COMMENTS_OF_POST_TOP,
                        commentRowMapper, a.getId());
                rebuild(comments);
                return comments;
            default:
                return null;
        }
    }

    @Override
    public void likeComment(Profile p, Comment c) {
        template.update(SQL_ADD_RATE, p.getId(), c.getId(), 1);
        template.update(SQL_UPDATE_RATE_COUNT, 1, c.getId());
    }

    @Override
    public void dislikeComment(Profile p, Comment c) {
        template.update(SQL_ADD_RATE, p.getId(), c.getId(), -1);
        template.update(SQL_UPDATE_RATE_COUNT, -1, c.getId());
    }

    @Override
    public void removeRate(Profile p, Comment c) {
        List<Integer> l = template.query(SQL_REMOVE_RATE, row -> row.getInt("value"), p.getId(), c.getId());
        if(l.size() > 0){
            int value = l.get(0);
            template.update(SQL_UPDATE_RATE_COUNT, -value, c.getId());
        }
    }

    @Override
    public void addAttachment(Comment c, String path) {
        template.update(SQL_INSERT_ATTACHMENTS, c.getId(), path);
    }

    @Override
    public void removeAttachment(Comment c, String path) {
        template.update(SQL_REMOVE_ATTACHMENT, c.getId(), path);
    }

    private void rebuild(List<Comment> comments){
        for(int i = comments.size() - 1; i >= 0; i--){
            for(int j = i - 1; j >= 0; j--){
                if(i >= comments.size()) continue;
                if(comments.get(i).getParent().getId() == comments.get(j).getId()){
                    comments.get(i).setParent(comments.get(j));
                    if(comments.get(j).getChildes() == null){
                        comments.get(j).setChildes(new ArrayList<>());
                    }
                    comments.get(j).getChildes().add(0, comments.get(i));
                    comments.remove(i);
                }
            }
        }
    }

    @Override
    public List<Comment> getNestedComment(Comment c, CommentsOrder order, int nesting) {
        List<Comment> comments = template.query(SQL_GET_CHILD_COMMENTS_OF_POST_NEW,
                commentRowMapper, c.getId(), nesting);
        rebuild(comments);
        return comments;
    }

    @Override
    public int getProfileRate(Profile p, Comment c) {
        List<Integer> l = template.query(SQL_GET_PROFILE_RATE,row -> row.getInt("value"), c.getId(), p.getId());
        if(l.size() == 0){
            return 0;
        }
        return l.get(0);
    }

    @Override
    public void saveComment(Profile p, Comment c) {
        template.update(SQL_SAVE_COMMENT, p.getId(), c.getId());
    }

    @Override
    public boolean isSaved(Profile p, Comment c) {
        List<Integer> l = template.query(SQL_IS_SAVED, row -> 1, p.getId(), c.getId());
        return l.size() > 0;
    }

    @Override
    public void deleteSavedArticle(Profile p, Comment c) {
        template.update(SQL_DROP_SAVED_COMMENT,p.getId(), c.getId());
    }

    @Override
    public List<Comment> getSavedComments(Profile p, int begin, int end, Date updated) {
        return template.query(SQL_GET_SAVED_COMMENT, commentRowMapper,
                p.getId(), updated, end - begin, begin);
    }


    @Override
    public List<String> getAttachmentsPaths(Comment c) {
        return template.query(SQL_ATTACHMENTS, row -> row.getString("path"), c.getId());
    }

    @Override
    public void save(Comment entity) {
        entity.setId(template.query(SQL_INSERT_COMMENT, row -> row.getLong("id"),
                entity.getAuthor().getId(), entity.getContent(), entity.getParent().getId(), entity.getArticle().getId()).get(0));
        if(entity.getAttachmentsPaths() == null) return;
        for (String s : entity.getAttachmentsPaths()){
            template.update(SQL_INSERT_ATTACHMENTS, entity.getId(), s);
        }
    }

    @Override
    public void update(Comment entity) {
        template.update(SQL_UPDATE, entity.getContent(), entity.getId());
    }

    @Override
    public void delete(Comment entity) {
        template.update(SQL_DELETE_COMMENT, entity.getId());
    }

    @Override
    public Optional<Comment> findById(Long id) {
        List<Comment> l = template.query(SQL_COMMENT_BY_ID, commentRowMapper, id);
        if(l.size() == 0){
            return Optional.empty();
        }
        return Optional.of(l.get(0));
    }

    @Override
    public List<Comment> findAll() {
        return null;
    }
}
