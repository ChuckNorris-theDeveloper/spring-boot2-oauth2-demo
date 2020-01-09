package com.basumatarau.samples.oauth.authprovider.config.controller;

import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Map;

@FrameworkEndpoint
public class JwkSetEndpoint {

    private final JWKSet jwkSet;

    public JwkSetEndpoint(JWKSet jwkSet) {
        this.jwkSet = jwkSet;
    }

    @GetMapping("/.well-known/jwks.json")
    @ResponseBody
    public Map<String, Object> getKey(Principal principal) {
        return jwkSet.toJSONObject();
    }
}
