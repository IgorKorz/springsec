package com.example.spring.sec.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.run {
            authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/api/admin/test").hasAnyRole("ADMIN")
                    .anyRequest().authenticated()
            formLogin().permitAll()
        }
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication().apply {
            withUser("user")
                    .password("{noop}user")
                    .roles("USER")
            withUser("admin")
                    .password("{noop}admin")
                    .roles("ADMIN")
        }
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}