package com.basumatarau.samples.oauth.authprovider.config.security;

import com.basumatarau.samples.oauth.authprovider.config.props.SecurityProperties;
import com.basumatarau.samples.oauth.authprovider.config.security.jwt.CustomTokenEnhancer;
import com.basumatarau.samples.oauth.authprovider.config.security.jwt.SigningJwtAccessTokenConverter;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class JwkSetConfiguration extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityProperties securityProperties;
    private final UserDetailsService userDetailsService;

    private JwtAccessTokenConverter jwtAccessTokenConverter;
    private TokenStore tokenStore;

    public JwkSetConfiguration(final DataSource dataSource,
                               final PasswordEncoder passwordEncoder,
                               final AuthenticationManager authenticationManager,
                               final SecurityProperties securityProperties,
                               final UserDetailsService userDetailsService) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.securityProperties = securityProperties;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                List.of(new CustomTokenEnhancer(), jwtAccessTokenConverter())
        );

        endpoints
                .authenticationManager(this.authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(this.userDetailsService)
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain);
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .jdbc(this.dataSource);
    }

    @Bean
    public TokenStore tokenStore() {
        if (tokenStore == null) {
            tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
        }
        return tokenStore;
    }

    @Bean
    public DefaultTokenServices tokenServices(final TokenStore tokenStore,
                                              final ClientDetailsService clientDetailsService) {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setTokenEnhancer(tokenEnhancer());
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setAuthenticationManager(this.authenticationManager);
        return tokenServices;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .passwordEncoder(this.passwordEncoder)
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        if (jwtAccessTokenConverter != null) {
            return jwtAccessTokenConverter;
        }
        Map<String, String> kidHeader = Map.of("kid", securityProperties.getJwt().getKid());
        jwtAccessTokenConverter = new SigningJwtAccessTokenConverter(keyPair(), kidHeader);

        return jwtAccessTokenConverter;
    }

    @Bean
    public KeyPair keyPair(){
        KeyStoreKeyFactory ksFactory =
                new KeyStoreKeyFactory(
                        securityProperties.getJwt().getKeyStore(),
                        securityProperties.getJwt().getKeyStorePassword().toCharArray()
                );
        return ksFactory.getKeyPair(securityProperties.getJwt().getKeyPairAlias());
    }

    @Bean
    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) keyPair().getPublic())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(securityProperties.getJwt().getKid());

        return new JWKSet(builder.build());
    }
}
