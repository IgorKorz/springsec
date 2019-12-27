package com.example.spring.sec.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    init {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)
    }

    override fun configure(http: HttpSecurity) {
        http.run {
            csrf().disable()
            authorizeRequests().apply {
                antMatchers("/api/test").authenticated()
                antMatchers("/api/admin/test").hasAnyRole("ADMIN")
            }
            formLogin().apply {
                disable()
            }
            logout()
            httpBasic()
        }
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication().apply {
            withUser("user").password(encoder().encode("user")).roles("USER")
            withUser("admin").password(encoder().encode("admin")).roles("ADMIN")
        }
    }
}

@Bean
fun encoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()