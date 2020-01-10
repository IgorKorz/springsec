package com.example.spring.sec.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

//    init {
//        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)
//    }

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
            //            passwordEncoder(encoder())
            withUser("user")
                    .password("user")
                    .roles("USER")
            withUser("admin")
                    .password("admin")
                    .roles("ADMIN")
        }
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}

@Bean
fun encoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()