package com.example.spring.sec.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.access.AccessDeniedHandler


@Configuration
@EnableWebSecurity
class WebSecurityConfig @Autowired constructor(private val accessDeniedHandler: AccessDeniedHandler)
    : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.run {
            csrf().disable()
            authorizeRequests().apply {
                antMatchers("/", "/index", "/about").permitAll()
                antMatchers("/admin/**").hasAnyRole("ADMIN")
                antMatchers("/user/**").hasAnyRole("USER")
                anyRequest().authenticated()
            }
            formLogin()
                    .loginPage("/login").permitAll()
            logout().permitAll()
            exceptionHandling().accessDeniedHandler(accessDeniedHandler)
        }
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
                .inMemoryAuthentication().apply {
                    withUser("user").password("user").roles("USER")
                    withUser("admin").password("admin").roles("ADMIN")
                }
    }
}