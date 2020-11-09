package ru.itis.antonov.waterfall.repositories;

import ru.itis.antonov.waterfall.models.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProfileRepositoryJdbcImpl implements ProfileRepository {

    //language=SQL
    private final static String SQL_INSERT_PROFILE =
            "INSERT INTO profile (nickname, password_hash, email) VALUES (?, ?, ?) RETURNING id";

    //language=SQL
    private final static String SQL_UPDATE_PROFILE =
            "UPDATE profile SET nickname=? WHERE id = ?";

    //language=SQL
    private final static String SQL_DELETE_PROFILE =
            "DELETE FROM profile WHERE id=?";

    //language=SQL
    private final static String SQL_FIND_BY_ID =
            "SELECT * FROM profile WHERE id=?";

    //language=SQL
    private final static String SQL_FIND_BY_ID_ONLY_NAME =
            "SELECT id, nickname FROM profile WHERE id=?";

    //language=SQL
    private final static String SQL_PROFILE_SUBSCRIBES =
            "SELECT p.id AS id, nickname FROM profile_subscribe JOIN profile p " +
                    "on (profile_subscribe.subscribe_id = p.id AND profile_id = ?)";

    //language=SQL
    private final static String SQL_PROFILE_BY_UUID =
            "SELECT  p.id AS id, nickname, email, password_hash FROM " +
                    "profile p JOIN profile_uuid pu on (p.id = pu.profile_id AND pu.uuid = ?)";

    //language=SQL
    private final static String SQL_DELETE_ALL_UUID =
            "DELETE FROM profile_uuid WHERE profile_id = ?";

    //language=SQL
    private final static String SQL_ADD_SUBSCRIBE =
            "INSERT INTO profile_subscribe (profile_id, subscribe_id) VALUES (?, ?)";

    //language=SQL
    private final static String SQL_DELETE_SUBSCRIBE =
            "DELETE FROM profile_subscribe WHERE profile_id = ? AND subscribe_id = ?";

    //language=SQL
    private final static String SQL_IS_SUBSCRIBED =
            "SELECT 1 FROM profile_subscribe WHERE profile_id = ? AND subscribe_id = ?";

    //language=SQL
    private final static String SQL_PROFILE_BY_EMAIL =
            "SELECT * FROM profile WHERE email = ?";

    //language=SQL
    private final static String SQL_ADD_UUID =
            "INSERT INTO profile_uuid (profile_id, uuid, added) VALUES (?, ?, now())";

    //language=SQL
    private final static String SQL_REMOVE_UUID =
            "DELETE FROM profile_uuid WHERE uuid=?";

    //language=SQL
    private final static String SQL_REFRESH_UUIDs =
            "DELETE FROM profile_uuid WHERE added < (now() - interval '1 year')";

    private DataSource dataSource;

    private SimpleJdbcTemplate template;

    private RowMapper<Profile> profileRowMapper = row -> Profile.builder()
            .id(row.getLong("id"))
            .nickname(row.getString("nickname"))
            .email(row.getString("email"))
            .passwordHash(row.getString("password_hash"))
            .build();

    public ProfileRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        template = new SimpleJdbcTemplate(dataSource);
    }

    @Override
    public List<Profile> getProfileSubscribes(Profile p) {
        return template.query(SQL_PROFILE_SUBSCRIBES, profileRowMapper, p.getId());
    }

    @Override
    public Profile getProfileByUUID(UUID uuid) {
        List<Profile> l = template.query(SQL_PROFILE_BY_UUID, profileRowMapper, uuid.toString());
        if(l.size() == 0){
            return null;
        }
        return l.get(0);
    }

    @Override
    public void deleteAllUUIDS(Profile p) {
        template.update(SQL_DELETE_ALL_UUID, p.getId());
    }

    @Override
    public void addUUID(Profile p, UUID uuid) {
        template.update(SQL_ADD_UUID, p.getId(), uuid.toString());
    }

    @Override
    public void removeUUID(UUID uuid) {
        template.update(SQL_REMOVE_UUID, uuid.toString());
    }

    @Override
    public void refreshUUIDs() {

    }

    @Override
    public void subscribe(Profile subscriber, Profile subscribe) {
        template.update(SQL_ADD_SUBSCRIBE, subscriber.getId(), subscribe.getId());
    }

    @Override
    public void unsubscribe(Profile subscriber, Profile subscribe) {
        template.update(SQL_DELETE_SUBSCRIBE, subscriber.getId(), subscribe.getId());
    }

    @Override
    public boolean isSubscribed(Profile subscriber, Profile subscribe) {
        return template.query(SQL_IS_SUBSCRIBED, row -> 1, subscriber.getId(), subscribe.getId()).size() > 0;
    }

    @Override
    public Profile getByEmail(String email) {
        List<Profile> l = template.query(SQL_PROFILE_BY_EMAIL, profileRowMapper, email);
        if(l.size() == 0){
            return null;
        }
        return l.get(0);
    }

    @Override
    public Profile getWithName(long id) {
        List<Profile> l = template.query(SQL_FIND_BY_ID_ONLY_NAME,
                row -> Profile.builder().id(row.getLong("id")).nickname(row.getString("nickname")).build(),
                id);
        if(l.size() == 0){
            return null;
        }
        return l.get(0);
    }

    @Override
    public void save(Profile entity) {
        entity.setId(template.query(SQL_INSERT_PROFILE,
                row -> row.getLong("id"),
                entity.getNickname(), entity.getPasswordHash(), entity.getEmail()).get(0));
    }

    @Override
    public void update(Profile entity) {
        template.update(SQL_UPDATE_PROFILE, entity.getNickname(), entity.getId());
    }

    @Override
    public void delete(Profile entity) {
        template.update(SQL_DELETE_PROFILE, entity.getId());
    }

    @Override
    public Optional<Profile> findById(Long id) {
        List<Profile> l = template.query(SQL_FIND_BY_ID, profileRowMapper, id);
        if(l.size() == 0){
            return Optional.empty();
        }
        return Optional.of(l.get(0));
    }

    @Override
    public List<Profile> findAll() {
        return null;
    }
}
