CREATE TABLE IF NOT EXISTS profile
(
    id            BIGSERIAL PRIMARY KEY,
    nickname      VARCHAR(50),
    password_hash VARCHAR(60),
    email         VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS "group"
(
    id          BIGSERIAL PRIMARY KEY,
    title        VARCHAR(50),
    description VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS profile_group
(
    id         BIGSERIAL PRIMARY KEY,
    profile_id BIGINT REFERENCES profile (id),
    group_id   BIGINT REFERENCES "group" (id),
    UNIQUE (profile_id, group_id)
);

CREATE TABLE IF NOT EXISTS article
(
    id        BIGSERIAL PRIMARY KEY,
    title     VARCHAR(100),
    content   TEXT,
    author_id BIGINT REFERENCES profile (id),
    group_id  BIGINT REFERENCES "group" (id),
    created   TIMESTAMP,
    rate      INTEGER
);

CREATE TABLE IF NOT EXISTS saved_article
(
    id         BIGSERIAL PRIMARY KEY,
    profile_id BIGINT REFERENCES profile (id),
    article_id BIGINT REFERENCES article (id),
    added      TIMESTAMP
);

CREATE TABLE IF NOT EXISTS article_rate
(
    id         BIGSERIAL PRIMARY KEY,
    profile_id BIGINT REFERENCES profile (id),
    article_id BIGINT REFERENCES article (id),
    value      INTEGER
);

CREATE TABLE IF NOT EXISTS tag
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS tag_article
(
    id         BIGSERIAL PRIMARY KEY,
    article_id BIGINT REFERENCES article (id),
    tag        VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS comment
(
    id                BIGSERIAL PRIMARY KEY,
    author_id         BIGINT REFERENCES profile (id),
    content           TEXT,
    parent_comment_id BIGINT REFERENCES comment (id),
    created           TIMESTAMP,
    article_id        BIGINT REFERENCES article (id),
    rate              INTEGER
);

CREATE TABLE IF NOT EXISTS comment_attachments
(
    id         BIGSERIAL PRIMARY KEY,
    comment_id BIGINT REFERENCES comment (id),
    path       VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS saved_comment
(
    id         BIGSERIAL PRIMARY KEY,
    profile_id BIGINT REFERENCES profile (id),
    comment_id BIGINT REFERENCES comment (id),
    added      TIMESTAMP
);

CREATE TABLE IF NOT EXISTS comment_rate
(
    id         BIGSERIAL PRIMARY KEY,
    profile_id BIGINT REFERENCES profile (id),
    comment_id BIGINT REFERENCES comment (id),
    value      INTEGER
);

CREATE TABLE IF NOT EXISTS profile_uuid
(
    id         BIGSERIAL PRIMARY KEY,
    profile_id BIGSERIAL REFERENCES profile (id),
    uuid       VARCHAR(36),
    added      TIMESTAMP
);

CREATE TABLE IF NOT EXISTS profile_subscribe
(
    id BIGSERIAL PRIMARY KEY,
    profile_id BIGSERIAL REFERENCES profile(id),
    subscribe_id BIGSERIAL REFERENCES profile(id)
);
