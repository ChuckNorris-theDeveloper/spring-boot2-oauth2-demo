-- The encrypted client_secret it `secret`
--INSERT INTO oauth_client_details (client_id, client_secret, scope, authorized_grant_types, authorities, access_token_validity)
--  VALUES ('clientId', '{bcrypt}$2a$10$vCXMWCn7fDZWOcLnIEhmK.74dvK1Eh8ae2WrWlhr2ETPLoxQctN4.', 'read,write', 'password,refresh_token,client_credentials', 'ROLE_CLIENT', 300);

INSERT INTO auth_schema.oauth_client_details (client_id, client_secret, scope, authorized_grant_types, authorities, access_token_validity, web_server_redirect_uri, autoapprove)
  VALUES ('clientId', '{bcrypt}$2a$10$vCXMWCn7fDZWOcLnIEhmK.74dvK1Eh8ae2WrWlhr2ETPLoxQctN4.', 'read,write,trust', 'authorization_code,refresh_token,client_credentials', 'ROLE_CLIENT', 600, 'http://localhost:8080/oauth2/callback/local-auth-provider', 'true');

-- The encrypted password is `pass`
INSERT INTO auth_schema.users (id, username, password, enabled) VALUES (1, 'user', '{bcrypt}$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', 1);
INSERT INTO auth_schema.users (id, username, password, enabled) VALUES (2, 'guest', '{bcrypt}$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', 1);

INSERT INTO auth_schema.authorities (username, authority) VALUES ('user', 'ROLE_USER');
INSERT INTO auth_schema.authorities (username, authority) VALUES ('guest', 'ROLE_GUEST');
