CREATE TABLE IF NOT EXISTS user_tbl
(
    id              BIGSERIAL PRIMARY KEY,
    first_name      VARCHAR(50),
    last_name       VARCHAR(50),
    username        VARCHAR(50) UNIQUE ,
    telegram_id     BIGINT NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS chat_tbl
(
    id                 BIGSERIAL PRIMARY KEY,
    title              VARCHAR(100),
    telegram_chat_id   BIGINT NOT NULL UNIQUE,
    chat_bot_status    VARCHAR(30) NOT NULL
    );

CREATE TABLE IF NOT EXISTS game_tbl
(
    id      BIGSERIAL PRIMARY KEY,
    status  VARCHAR(30) NOT NULL,
    chat_id BIGINT      NOT NULL REFERENCES chat_tbl (id)
    );

CREATE TABLE IF NOT EXISTS user_game_tbl
(
    id        BIGSERIAL   PRIMARY KEY,
    user_id   BIGINT      NOT NULL REFERENCES user_tbl (id),
    game_id   BIGINT      NOT NULL REFERENCES game_tbl (id),
    game_role VARCHAR(30) NOT NULL,
    score     SMALLINT    NOT NULL DEFAULT 0,
    explained BOOLEAN     DEFAULT false NOT NULL
    );

CREATE TABLE IF NOT EXISTS card_tbl
(
    id          BIGSERIAL PRIMARY KEY,
    answer      VARCHAR NOT NULL,
    taboos      VARCHAR[],
    all_taboos  VARCHAR[]
);
CREATE TABLE IF NOT EXISTS card_entity_taboos
(
    card_entity_id BIGINT NOT NULL REFERENCES card_tbl (id),
    taboos VARCHAR NOT NULL
);
CREATE TABLE IF NOT EXISTS card_entity_all_taboos
(
    card_entity_id BIGINT NOT NULL REFERENCES card_tbl (id),
    all_taboos VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS game_card_tbl
(
    id      BIGSERIAL PRIMARY KEY,
    card_id INT         NOT NULL REFERENCES card_tbl (id),
    game_id INT         NOT NULL REFERENCES game_tbl (id),
    status  VARCHAR(30) NOT NULL
    );

CREATE TABLE IF NOT EXISTS word_tbl
(
    id           BIGSERIAL PRIMARY KEY,
    word         VARCHAR(40) NOT NULL,
    init_form_id INT REFERENCES word_tbl (id)
    );
CREATE INDEX idx_word_word ON word_tbl (word);

CREATE TABLE IF NOT EXISTS wait_room_tbl
(
    id          BIGSERIAL   PRIMARY KEY,
    hash        VARCHAR(36) NOT NULL UNIQUE,
    status      VARCHAR(30) NOT NULL,
    chat_id     BIGINT      NOT NULL REFERENCES chat_tbl (id),
    message_id  BIGINT
    );

CREATE TABLE IF NOT EXISTS wait_room_user_tbl
(
    user_id      BIGINT NOT NULL REFERENCES user_tbl (id),
    wait_room_id BIGINT NOT NULL REFERENCES wait_room_tbl (id),
    PRIMARY KEY (user_id, wait_room_id)
    );