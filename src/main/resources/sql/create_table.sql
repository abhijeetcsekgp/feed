CREATE TABLE _user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email_id VARCHAR(50) NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    UNIQUE (email_id)
);

CREATE TABLE feed (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    UNIQUE(name)
);

CREATE TABLE article(
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    url VARCHAR(1024) NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
);

CREATE TABLE subscription (
    user_id BIGINT NOT NULL,
    feed_id BIGINT NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, feed_id),
    FOREIGN KEY (user_id) REFERENCES _user(id),
    FOREIGN KEY (feed_id) REFERENCES feed(id)
);

CREATE TABLE feed_article (
    feed_id BIGINT NOT NULL,
    article_id BIGINT NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (feed_id, article_id),
    FOREIGN KEY (feed_id) REFERENCES feed(id),
    FOREIGN KEY (article_id) REFERENCES article(id)
);