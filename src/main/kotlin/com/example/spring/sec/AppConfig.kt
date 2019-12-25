package com.example.spring.sec

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.apply {
            addViewController("/home").setViewName("home")
            addViewController("/").setViewName("home")
            addViewController("/hello").setViewName("hello")
            addViewController("/login").setViewName("login")
        }
    }
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    init {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)
    }

    override fun configure(http: HttpSecurity) {
        http.run {
            authorizeRequests()
                    .antMatchers("/", "/home").permitAll()
                    .anyRequest().authenticated()
            formLogin()
                    .permitAll()
            logout()
                    .permitAll()
        }
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication().apply {
            withUser("admin").password(encoder().encode("admin")).roles("ADMIN")
            withUser("user").password(encoder().encode("user")).roles("USER")
        }
    }
}

@Bean
fun encoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()