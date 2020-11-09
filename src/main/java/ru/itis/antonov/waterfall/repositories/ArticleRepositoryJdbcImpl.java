package ru.itis.antonov.waterfall.repositories;

import ru.itis.antonov.waterfall.models.*;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ArticleRepositoryJdbcImpl implements ArticleRepository {

    //language=SQL
    private static final String SQL_INSERT = "INSERT INTO article (title, content, author_id, group_id, created, rate) " +
            "VALUES (?, ?, ?, ?, now(), 0) RETURNING id";

    //language=SQL
    private static final String SQL_UPDATE = "UPDATE article SET " +
            "title=?, content=? WHERE id=?";

    //language=SQL
    private static final String SQL_DELETE = "DELETE FROM article WHERE id=?";

    //language=SQL
    private static final String SQL_FIND_BY_ID = "SELECT * FROM article WHERE id=?";

    //language=SQL
    private static final String SQL_GET_ARTICLE_RATE = "SELECT sum(value) AS rate FROM article_rate WHERE article_id=?";

    //language=SQL
    private static final String SQL_GET_ARTICLES_BY_TAG_NEW =
            "SELECT a.id AS id, title, content, author_id, group_id, created, rate " +
            "FROM tag_article ta JOIN article a on ta.article_id = a.id" +
                    " WHERE tag=? AND created < ? ORDER BY created DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_ARTICLES_BY_TAG_TOP =
            "SELECT a.id AS id, title, content, author_id, group_id, created, rate " +
                    "FROM tag_article ta JOIN article a on ta.article_id = a.id" +
                    " WHERE tag=? AND created > now() - interval '?' AND created < ?" +
                    " ORDER BY rate DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_ARTICLES_BY_TAG_TOP_ALL_TIME =
            "SELECT a.id AS id, title, content, author_id, group_id, created, rate " +
                    "FROM tag_article ta JOIN article a on ta.article_id = a.id" +
                    " WHERE tag=? AND created < ? ORDER BY rate DESC LIMIT ? OFFSET ?";


    //language=SQL
    private static final String SQL_SAVE_ARTICLE = "INSERT INTO saved_article (profile_id, article_id, added) " +
            "VALUES(?, ?, now())";

    //language=SQL
    private static final String SQL_REMOVE_SAVE_ARTICLE = "DELETE FROM saved_article " +
            "WHERE profile_id=? AND article_id=?";

    //language=SQL
    private static final String SQL_RATE_ARTICLE = "INSERT INTO article_rate (profile_id, article_id, value) " +
            "VALUES (?, ?, ?)";

    //language=SQL
    private static final String SQL_UPDATE_RATE_COUNT = "UPDATE article SET rate=rate+? " +
            "WHERE id=?";

    //language=SQL
    private static final String SQL_UNDO_RATE_ARTICLE = "DELETE FROM article_rate " +
            "WHERE profile_id=? AND article_id=? RETURNING value";

    //language=SQL
    private static final String SQL_ARTICLE_SAVED = "SELECT 1 FROM saved_article WHERE profile_id=? AND article_id=?";

    //language=SQL
    private static final String SQL_GET_ARTICLES_NEW =
            "SELECT * FROM article WHERE created < ?" +
                    " ORDER BY created DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_ARTICLES_TOP_OF_DAY =
            "SELECT * FROM article" +
                    " WHERE created > now() - interval '1 day' AND created < ?" +
                    " ORDER BY rate DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_ARTICLES_TOP_OF_WEEK =
            "SELECT * FROM article" +
                    " WHERE created > now() - interval '7 day' AND created < ?" +
                    " ORDER BY rate DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_ARTICLES_TOP_OF_MONTH =
            "SELECT * FROM article" +
                    " WHERE created > now() - interval '1 month' AND created < ?" +
                    " ORDER BY rate DESC LIMIT ? OFFSET ?";
    //language=SQL
    private static final String SQL_GET_ARTICLES_TOP_OF_YEAR =
            "SELECT * FROM article" +
                    " WHERE created > now() - interval '1 year' AND created < ?" +
                    " ORDER BY rate DESC LIMIT ? OFFSET ?";


    //language=SQL
    private static final String SQL_GET_ARTICLES_TOP_ALL_TIME =
            "SELECT * FROM article " +
                    " WHERE created < ? ORDER BY rate" +
                    " DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_ARTICLES_OF_PROFILE_NEW =
            "WITH feed AS ((SELECT a.id AS id, title, content, author_id, a.group_id AS group_id, created, rate " +
                    "FROM article a JOIN profile_group pg on a.group_id = pg.group_id " +
                    "WHERE pg.profile_id=? AND created < ?) " +
                    "UNION " +
                    "(SELECT a.id AS id, title, content, author_id, group_id, created, rate "+
                    "FROM article a JOIN profile_subscribe ps on a.author_id = ps.subscribe_id " +
                    "WHERE ps.profile_id=? AND created < ?)) " +
                    "SELECT * from feed ORDER BY created DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_ARTICLES_OF_PROFILE_TOP_OF_DAY =
            "WITH feed AS ((SELECT a.id AS id, title, content, author_id, a.group_id AS group_id, created, rate " +
                    "FROM article a JOIN profile_group pg on a.group_id = pg.group_id " +
                    "WHERE pg.profile_id=? AND created > now() - interval '1 day' AND created < ?) " +
                    "UNION " +
                    "(SELECT a.id AS id, title, content, author_id, group_id, created, rate "+
                    "FROM article a JOIN profile_subscribe ps on a.author_id = ps.subscribe_id " +
                    "WHERE ps.profile_id=? AND created > now() - interval '1 day' AND created < ?)) " +
                    "SELECT * from feed ORDER BY rate DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_ARTICLES_OF_PROFILE_TOP_OF_WEEK =
            "WITH feed AS ((SELECT a.id AS id, title, content, author_id, a.group_id AS group_id, created, rate " +
                    "FROM article a JOIN profile_group pg on a.group_id = pg.group_id " +
                    "WHERE pg.profile_id=? AND created > now() - interval '7 day' AND created < ?) " +
                    "UNION " +
                    "(SELECT a.id AS id, title, content, author_id, group_id, created, rate "+
                    "FROM article a JOIN profile_subscribe ps on a.author_id = ps.subscribe_id " +
                    "WHERE ps.profile_id=? AND created > now() - interval '7 day' AND created < ?)) " +
                    "SELECT * from feed ORDER BY rate DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_ARTICLES_OF_PROFILE_TOP_OF_MONTH =
            "WITH feed AS ((SELECT a.id AS id, title, content, author_id, a.group_id AS group_id, created, rate " +
                    "FROM article a JOIN profile_group pg on a.group_id = pg.group_id " +
                    "WHERE pg.profile_id=? AND created > now() - interval '1 month' AND created < ?) " +
                    "UNION " +
                    "(SELECT a.id AS id, title, content, author_id, group_id, created, rate "+
                    "FROM article a JOIN profile_subscribe ps on a.author_id = ps.subscribe_id " +
                    "WHERE ps.profile_id=? AND created > now() - interval '1 month' AND created < ?)) " +
                    "SELECT * from feed ORDER BY rate DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_ARTICLES_OF_PROFILE_TOP_OF_YEAR =
            "WITH feed AS ((SELECT a.id AS id, title, content, author_id, a.group_id AS group_id, created, rate " +
                    "FROM article a JOIN profile_group pg on a.group_id = pg.group_id " +
                    "WHERE pg.profile_id=? AND created > now() - interval '1 year' AND created < ?) " +
                    "UNION " +
                    "(SELECT a.id AS id, title, content, author_id, group_id, created, rate "+
                    "FROM article a JOIN profile_subscribe ps on a.author_id = ps.subscribe_id " +
                    "WHERE ps.profile_id=? AND created > now() - interval '1 year' AND created < ?)) " +
                    "SELECT * from feed ORDER BY rate DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_ARTICLES_OF_PROFILE_TOP_ALL_TIME =
            "WITH feed AS ((SELECT a.id AS id, title, content, author_id, a.group_id AS group_id, created, rate " +
                    "FROM article a JOIN profile_group pg on a.group_id = pg.group_id " +
                    "WHERE pg.profile_id=? AND created < ?) " +
                    "UNION " +
                    "(SELECT a.id AS id, title, content, author_id, group_id, created, rate "+
                    "FROM article a JOIN profile_subscribe ps on a.author_id = ps.subscribe_id " +
                    "WHERE ps.profile_id=? AND created < ?)) " +
                    "SELECT * from feed ORDER BY rate DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_AUTHORS_ARTICLES_NEW =
            "SELECT * FROM article WHERE author_id=? AND created < ? ORDER BY created DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_AUTHORS_ARTICLES_TOP =
            "SELECT * FROM article WHERE author_id=? AND created > now() - interval ?" +
                    " AND created < ? ORDER BY rate DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_AUTHORS_ARTICLES_TOP_ALL_TIME =
            "SELECT * FROM article WHERE author_id=? AND created < ? " +
                    "ORDER BY rate DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_GROUP_ARTICLES_NEW =
            "SELECT * FROM article WHERE group_id=? AND created < ? " +
                    "ORDER BY created DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_GROUP_ARTICLES_TOP =
            "SELECT * FROM article WHERE group_id=? AND created > now() - interval ?" +
                    " AND created < ? ORDER BY rate DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_GET_GROUP_ARTICLES_TOP_ALL_TIME =
            "SELECT * FROM article WHERE group_id=? AND created < ? " +
                    "ORDER BY rate DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_PROFILE_RATE =
            "SELECT value FROM article_rate WHERE article_id=? AND profile_id=?";

    //language=SQL
    private static final String SQL_SAVED_ARTICLES =
            "SELECT a.id AS id, title, content, author_id, a.group_id AS group_id, created, rate FROM article a JOIN saved_article sa " +
                    "on (a.id = sa.article_id AND sa.profile_id = ? AND added<?) ORDER BY added DESC LIMIT ? OFFSET ?";

    //language=SQL
    private static final String SQL_COUNT_COMMENTS =
            "SELECT count(*) FROM comment WHERE article_id = ?";

    private DataSource dataSource;

    private SimpleJdbcTemplate template;

    private RowMapper<Article> articleRowMapper = row -> Article.builder()
            .id(row.getLong("id"))
            .author(Profile.builder().id(row.getLong("author_id")).build())
            .title(row.getString("title"))
            .content(row.getString("content"))
            .group(Group.builder().id(row.getObject("group_id") == null ? -1 : row.getLong("group_id")).build())
            .date(row.getTimestamp("created"))
            .rate(row.getInt("rate"))
            .build();

    public ArticleRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        template = new SimpleJdbcTemplate(dataSource);
    }

    private int recount(Article a){
        return template.query(SQL_GET_ARTICLE_RATE, row -> row.getInt("value"), a.getId()).get(0);
    }

    @Override
    public int profileRate(Profile p, Article a) {
        if(p == null) return 0;
        List<Integer> l = template.query(SQL_PROFILE_RATE, row -> row.getInt("value"), a.getId(), p.getId());
        if(l.size() == 0) return 0;
        else return l.get(0);
    }

    @Override
    public List<Article> getByTag(String tag, FeedType type, int begin, int end, Date updated) {
        switch (type){
            case FRESH:
                return template.query(SQL_GET_ARTICLES_BY_TAG_NEW, articleRowMapper,tag, updated, end - begin, begin);
            case TOP_OF_DAY:
                return template.query(SQL_GET_ARTICLES_BY_TAG_TOP, articleRowMapper,tag, updated, "'1 day'", end - begin, begin);
            case TOP_OF_WEEK:
                return template.query(SQL_GET_ARTICLES_BY_TAG_TOP, articleRowMapper,tag, updated, "'7 day'", end - begin, begin);
            case TOP_OF_MONTH:
                return template.query(SQL_GET_ARTICLES_BY_TAG_TOP, articleRowMapper,tag, updated, "'1 month'", end - begin, begin);
            case TOP_OF_YEAR:
                return template.query(SQL_GET_ARTICLES_BY_TAG_TOP, articleRowMapper,tag, updated, "'1 year'", end - begin, begin);
            case TOP_AT_ALL_TIME:
                return template.query(SQL_GET_ARTICLES_BY_TAG_TOP_ALL_TIME, articleRowMapper,tag, updated, end - begin, begin);
            default:
                return null;
        }
    }

    @Override
    public void saveArticle(Profile p, Article a) {
        template.update(SQL_SAVE_ARTICLE, p.getId(), a.getId());
    }

    @Override
    public boolean isSaved(Profile p, Article a) {
        if(p == null) return false;
        return template.query(SQL_ARTICLE_SAVED, row -> 1, p.getId(), a.getId()).size() > 0;
    }

    @Override
    public void deleteSavedArticle(Profile p, Article a) {
        template.update(SQL_REMOVE_SAVE_ARTICLE, p.getId(), a.getId());
    }

    @Override
    public void likeArticle(Profile p, Article a) {
        template.update(SQL_RATE_ARTICLE, p.getId(), a.getId(), 1);
        template.update(SQL_UPDATE_RATE_COUNT, 1, a.getId());
    }

    @Override
    public void dislikeArticle(Profile p, Article a) {
        template.update(SQL_RATE_ARTICLE, p.getId(), a.getId(), -1);
        template.update(SQL_UPDATE_RATE_COUNT, -1, a.getId());
    }

    @Override
    public void removeRate(Profile p, Article a) {
        List<Integer> l = template.query(SQL_UNDO_RATE_ARTICLE, row -> row.getInt("value"), p.getId(), a.getId());
        if(l.size() > 0) {
            int value = l.get(0);
            template.update(SQL_UPDATE_RATE_COUNT, -value, a.getId());
        }
    }

    @Override
    public List<Article> feed(Profile p, FeedType type, int begin, int end, Date updated) {
        if(p == null) {
            switch (type) {
                case FRESH:
                    return template.query(SQL_GET_ARTICLES_NEW, articleRowMapper, updated, end - begin, begin);
                case TOP_OF_DAY:
                    return template.query(SQL_GET_ARTICLES_TOP_OF_DAY, articleRowMapper, updated, end - begin, begin);
                case TOP_OF_WEEK:
                    return template.query(SQL_GET_ARTICLES_TOP_OF_WEEK, articleRowMapper, updated, end - begin, begin);
                case TOP_OF_MONTH:
                    return template.query(SQL_GET_ARTICLES_TOP_OF_MONTH, articleRowMapper, updated, end - begin, begin);
                case TOP_OF_YEAR:
                    return template.query(SQL_GET_ARTICLES_TOP_OF_YEAR, articleRowMapper, updated, end - begin, begin);
                case TOP_AT_ALL_TIME:
                    return template.query(SQL_GET_ARTICLES_TOP_ALL_TIME, articleRowMapper, updated, end - begin, begin);
                default:
                    return null;
            }
        }
        else {
            switch (type){
                case FRESH:
                    return template.query(SQL_GET_ARTICLES_OF_PROFILE_NEW, articleRowMapper,
                            p.getId(), updated, p.getId(), updated, end - begin, begin);
                case TOP_OF_DAY:
                    return template.query(SQL_GET_ARTICLES_OF_PROFILE_TOP_OF_DAY, articleRowMapper,
                            p.getId(), updated, p.getId(), updated, end - begin, begin);
                case TOP_OF_WEEK:
                    return template.query(SQL_GET_ARTICLES_OF_PROFILE_TOP_OF_WEEK, articleRowMapper,
                            p.getId(), updated, p.getId(), updated, end - begin, begin);
                case TOP_OF_MONTH:
                    return template.query(SQL_GET_ARTICLES_OF_PROFILE_TOP_OF_MONTH, articleRowMapper,
                            p.getId(), updated, p.getId(), updated, end - begin, begin);
                case TOP_OF_YEAR:
                    return template.query(SQL_GET_ARTICLES_OF_PROFILE_TOP_OF_YEAR, articleRowMapper,
                            p.getId(), updated, p.getId(), updated, end - begin, begin);
                case TOP_AT_ALL_TIME:
                    return template.query(SQL_GET_ARTICLES_OF_PROFILE_TOP_ALL_TIME, articleRowMapper,
                            p.getId(), updated, p.getId(), updated, end - begin, begin);
                default:
                    return null;
            }
        }
    }

    @Override
    public List<Article> getArticlesByAuthor(Profile p, FeedType type, int begin, int end, Date updated) {
        switch (type) {
            case FRESH:
                return template.query(SQL_GET_AUTHORS_ARTICLES_NEW, articleRowMapper,
                        p.getId(), updated, end - begin, begin);
            case TOP_OF_DAY:
                return template.query(SQL_GET_AUTHORS_ARTICLES_TOP, articleRowMapper,
                        p.getId(), "'1 day'", updated, end - begin, begin);
            case TOP_OF_WEEK:
                return template.query(SQL_GET_AUTHORS_ARTICLES_TOP, articleRowMapper,
                        p.getId(), "'7 day'", updated, end - begin, begin);
            case TOP_OF_MONTH:
                return template.query(SQL_GET_AUTHORS_ARTICLES_TOP, articleRowMapper,
                        p.getId(), "'1 month'", updated, end - begin, begin);
            case TOP_OF_YEAR:
                return template.query(SQL_GET_AUTHORS_ARTICLES_TOP, articleRowMapper,
                        p.getId(), "'1 year'", updated, end - begin, begin);
            case TOP_AT_ALL_TIME:
                return template.query(SQL_GET_AUTHORS_ARTICLES_TOP_ALL_TIME, articleRowMapper,
                        p.getId(), updated, end - begin, begin);
            default:
                return null;
        }
    }

    @Override
    public List<Article> getArticlesByGroup(Group g, FeedType type, int begin, int end, Date updated) {
        switch (type) {
            case FRESH:
                return template.query(SQL_GET_GROUP_ARTICLES_NEW, articleRowMapper,
                        g.getId(), updated, end - begin, begin);
            case TOP_OF_DAY:
                return template.query(SQL_GET_GROUP_ARTICLES_TOP, articleRowMapper,
                        g.getId(), "'1 day'", updated, end - begin, begin);
            case TOP_OF_WEEK:
                return template.query(SQL_GET_GROUP_ARTICLES_TOP, articleRowMapper,
                        g.getId(), "'7 day'", updated, end - begin, begin);
            case TOP_OF_MONTH:
                return template.query(SQL_GET_GROUP_ARTICLES_TOP, articleRowMapper,
                        g.getId(), "'1 month'", updated, end - begin, begin);
            case TOP_OF_YEAR:
                return template.query(SQL_GET_GROUP_ARTICLES_TOP, articleRowMapper,
                        g.getId(), "'1 year'", updated, end - begin, begin);
            case TOP_AT_ALL_TIME:
                return template.query(SQL_GET_GROUP_ARTICLES_TOP_ALL_TIME, articleRowMapper,
                        g.getId(), updated, end - begin, begin);
            default:
                return null;
        }
    }

    @Override
    public List<Article> savedArticles(Profile p, int begin, int end, Date updated) {
        return template.query(SQL_SAVED_ARTICLES, articleRowMapper, p.getId(), updated, end - begin, begin);
    }

    @Override
    public void save(Article entity) {
        entity.setId(template.query(SQL_INSERT, row -> row.getLong("id"),
                entity.getTitle(),
                entity.getContent(),
                entity.getAuthor().getId(),
                entity.getGroup() == null ? null : entity.getGroup().getId()).get(0));
    }

    @Override
    public void update(Article entity) {
        template.update(SQL_UPDATE, entity.getTitle(), entity.getContent(), entity.getId());
    }

    @Override
    public void delete(Article entity) {
        template.update(SQL_DELETE, entity.getId());
    }

    @Override
    public Optional<Article> findById(Long id) {
        List<Article> l = template.query(SQL_FIND_BY_ID, articleRowMapper, id);
        if(l.size() == 0){
            return Optional.empty();
        }
        return Optional.of(l.get(0));
    }

    @Override
    public List<Article> findAll() {
        return null;
    }

    @Override
    public int commentCount(Article a) {
        return template.query(SQL_COUNT_COMMENTS, row -> row.getInt("count"), a.getId()).get(0);
    }
}
