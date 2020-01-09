package com.basumatarau.samples.oauth.authprovider.config.security.jwt;

import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;

public class SigningJwtAccessTokenConverter extends JwtAccessTokenConverter {

    private final RsaSigner signer;
    private final Map<String, String> headers;
    private JsonParser objectMapper = JsonParserFactory.create();

    public SigningJwtAccessTokenConverter(KeyPair keyPair, Map<String, String> headers) {
        super();
        super.setKeyPair(keyPair);
        this.signer = new RsaSigner((RSAPrivateKey) keyPair.getPrivate());
        this.headers = headers;
    }

    @Override
    protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String content;
        try {
            content = this.objectMapper.formatMap(getAccessTokenConverter().convertAccessToken(accessToken, authentication));
        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Cannot convert access token to JSON", ex);
        }
        return JwtHelper.encode(content, this.signer, this.headers).getEncoded();
    }
}
