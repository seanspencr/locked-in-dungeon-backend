CREATE SEQUENCE IF NOT EXISTS user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE users (
                       id          BIGINT       NOT NULL DEFAULT nextval('user_id_seq'),
                       username    VARCHAR(32)  NOT NULL,
                       password    VARCHAR(32)  NOT NULL,
                       created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
                       updated_at  TIMESTAMPTZ,
                       CONSTRAINT users_pkey PRIMARY KEY (id),
                       CONSTRAINT users_username_unique UNIQUE (username)
);

ALTER SEQUENCE user_id_seq OWNED BY users.id;