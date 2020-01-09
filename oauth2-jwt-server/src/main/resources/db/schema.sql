DROP SCHEMA if exists auth_schema CASCADE;

CREATE SCHEMA if not exists auth_schema AUTHORIZATION postgres;

SET search_path = auth_schema;

COMMENT ON SCHEMA auth_schema IS 'oauth2 authorization server db schema';

-- DROP TABLE auth_schema.oauth_client_details;
CREATE TABLE IF NOT EXISTS auth_schema.oauth_client_details (
  client_id VARCHAR(256),
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256) NOT NULL,
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity int4,
  refresh_token_validity int4,
  additional_information VARCHAR(4000),
  autoapprove VARCHAR(256),

  CONSTRAINT oauth_client_details_pk PRIMARY KEY (client_id)
);

-- DROP TABLE auth_schema.oauth_client_token;
CREATE TABLE IF NOT EXISTS auth_schema.oauth_client_token (
  token_id VARCHAR(256),
  token bytea,
  authentication_id VARCHAR(256),
  user_name VARCHAR(256),
  client_id VARCHAR(256),

  CONSTRAINT oauth_client_token_pk PRIMARY KEY (authentication_id)
);

-- DROP TABLE auth_schema.oauth_access_token;
CREATE TABLE IF NOT EXISTS auth_schema.oauth_access_token (
  token_id VARCHAR(256),
  token bytea,
  authentication_id VARCHAR(256),
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication bytea,
  refresh_token VARCHAR(256)
);

-- DROP TABLE auth_schema.oauth_refresh_token;
CREATE TABLE IF NOT EXISTS auth_schema.oauth_refresh_token (
  token_id VARCHAR(256),
  token bytea,
  authentication bytea
);

-- DROP TABLE auth_schema.oauth_code;
CREATE TABLE IF NOT EXISTS auth_schema.oauth_code (
  code VARCHAR(256),
  authentication bytea
);

-- DROP SEQUENCE instant_messenger_db_schema.contact_entries_id_seq;
CREATE SEQUENCE if not exists auth_schema.users_id_seq
  INCREMENT BY 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  CACHE 1
  NO CYCLE;

-- DROP TABLE auth_schema.users;
CREATE TABLE IF NOT EXISTS auth_schema.users (
  id bigint NOT NULL DEFAULT nextval('auth_schema.users_id_seq'),
  username VARCHAR(256) NOT NULL,
  password VARCHAR(256) NOT NULL,
  enabled int,

  CONSTRAINT users_pk PRIMARY KEY (id)
);
CREATE UNIQUE INDEX if not exists unique_username_idx ON auth_schema.users USING btree (username);
ALTER SEQUENCE auth_schema.users_id_seq OWNED BY auth_schema.users.id;

-- DROP TABLE auth_schema.authorities;
CREATE TABLE IF NOT EXISTS auth_schema.authorities (
  username VARCHAR(256) NOT NULL,
  authority VARCHAR(256) NOT NULL,

  CONSTRAINT authorities_pk PRIMARY KEY(username, authority)
);