package com.example.spring.sec.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.RemoteTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore

@Configuration
@EnableAuthorizationServer
class AuthServerOAuth2Config @Autowired constructor(
        @Qualifier("authenticationManagerBean")
        private val authenticationManager: AuthenticationManager
) : AuthorizationServerConfigurerAdapter() {
    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.jdbc(dataSource())
    }

    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints
                .tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
    }
}

@Primary
@Bean
fun tokenService() = RemoteTokenServices().apply {
    setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token")
    setClientId("fooClientIdPassword")
    setClientSecret("secret")
}

@Bean
fun tokenStore(): TokenStore = JdbcTokenStore(dataSource())

@Bean
fun passwordEncoder(): PasswordEncoder = object : PasswordEncoder {
    override fun encode(rawPassword: CharSequence?) = "{noop}$rawPassword"

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?) =
            if (encodedPassword != null) rawPassword == encodedPassword.substring(6)
            else rawPassword == null
}