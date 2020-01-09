**OAuth2 authorization_code grant via Spring Boot 2, Spring Security 5**

The demo comprises 3 modules:
1. stateful web-client 
2. authorization server (identity provider)
3. secured resource server (stateless)

The web-client leverages openId protocol (over oauth2) for authentication,
userInfo endpoint is secured with rsa signed jwt (public key is stored on the client, 
though may be shared on the endpoint at authorization server, 
and discovered by the resource server), oauth token validation by the authorization server 
has not been implemented...

to be continued...  
