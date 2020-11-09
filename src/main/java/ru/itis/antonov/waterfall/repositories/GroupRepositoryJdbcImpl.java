package ru.itis.antonov.waterfall.repositories;

import ru.itis.antonov.waterfall.models.Article;
import ru.itis.antonov.waterfall.models.FeedType;
import ru.itis.antonov.waterfall.models.Group;
import ru.itis.antonov.waterfall.models.Profile;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class GroupRepositoryJdbcImpl implements GroupRepository {

    //language=SQL
    private final static String SQL_PROFILE_GROUPS =
            "SELECT g.id AS id, title, description FROM" +
                    " \"group\" g JOIN profile_group pg on (g.id = pg.group_id AND pg.profile_id = ?)";

    //language=SQL
    private final static String SQL_INSERT_SUBSCRIBE =
            "INSERT INTO profile_group (profile_id, group_id) VALUES (?, ?)";

    //language=SQL
    private final static String SQL_IS_SUBSCRIBE =
            "SELECT 1 FROM profile_group WHERE profile_id=? AND group_id=?";

    //language=SQL
    private final static String SQL_DROP_SUBSCRIBE =
            "DELETE FROM profile_group WHERE profile_id=? AND group_id=?";

    //language=SQL
    private final static String SQL_INSERT_GROUP =
            "INSERT INTO \"group\" (title, description) VALUES (?, ?) RETURNING id";

    //language=SQL
    private final static String SQL_UPDATE_GROUP =
            "UPDATE \"group\" SET title=?, description=? WHERE id = ?";

    //language=SQL
    private final static String SQL_DELETE_GROUP =
            "DELETE FROM \"group\" WHERE id=?";

    //language=SQL
    private final static String SQL_FIND_BY_ID =
            "SELECT * FROM \"group\" WHERE id=?";

    //language=SQL
    private final static String SQL_FIND_BY_ID_ONLY_NAME =
            "SELECT id, title FROM \"group\" WHERE id=?";

    private DataSource dataSource;

    private SimpleJdbcTemplate template;

    private RowMapper<Group> groupRowMapper = row -> Group.builder()
            .id(row.getLong("id"))
            .name(row.getString("name"))
            .description(row.getString("description"))
            .build();

    public GroupRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        template = new SimpleJdbcTemplate(dataSource);
    }

    @Override
    public List<Group> profileGroups(Profile p) {
        return template.query(SQL_PROFILE_GROUPS, groupRowMapper, p.getId());
    }

    @Override
    public void subscribe(Profile p, Group g) {
        template.update(SQL_INSERT_SUBSCRIBE, p.getId(), g.getId());
    }

    @Override
    public void unsubscribe(Profile p, Group g) {
        template.update(SQL_DROP_SUBSCRIBE, p.getId(), g.getId());
    }

    @Override
    public boolean isSubscribed(Profile p, Group g) {
        return template.query(SQL_IS_SUBSCRIBE, row -> 1, p.getId(), g.getId()).size() > 0;
    }

    @Override
    public Group getWithName(long id) {
        List<Group> l = template.query(SQL_FIND_BY_ID_ONLY_NAME, groupRowMapper, id);
        if(l.size() == 0){
            return null;
        }
        return l.get(0);
    }

    @Override
    public void save(Group entity) {
        entity.setId(template.query(SQL_INSERT_GROUP, row -> row.getLong("id"),
                entity.getName(), entity.getDescription()).get(0));
    }

    @Override
    public void update(Group entity) {
        template.update(SQL_UPDATE_GROUP, entity.getName(), entity.getDescription(), entity.getId());
    }

    @Override
    public void delete(Group entity) {
        template.update(SQL_DELETE_GROUP, entity.getId());
    }

    @Override
    public Optional<Group> findById(Long id) {
        List<Group> l = template.query(SQL_FIND_BY_ID, groupRowMapper, id);
        if(l.size() == 0){
            return Optional.empty();
        }
        return Optional.of(l.get(0));
    }

    @Override
    public List<Group> findAll() {
        return null;
    }
}
