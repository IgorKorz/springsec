package com.example.spring.sec

import com.example.spring.sec.handler.MySavedRequestAwareAuthenticationSuccessHandler
import com.example.spring.sec.handler.RestAuthenticationEntryPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
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
class WebSecurityConfig @Autowired constructor(
        private val restAuthenticationEntryPoint: RestAuthenticationEntryPoint,
        private val successHandler: MySavedRequestAwareAuthenticationSuccessHandler
) : WebSecurityConfigurerAdapter() {
    private val failureHandler = SimpleUrlAuthenticationFailureHandler()

    init {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)
    }

//    override fun configure(http: HttpSecurity) {
//        http.csrf().disable().run {
//            exceptionHandling()
//                    .authenticationEntryPoint(restAuthenticationEntryPoint)
//            authorizeRequests()
//                    .antMatchers("/", "/home").permitAll()
//                    .anyRequest().authenticated()
//            formLogin()
//                    .successHandler(successHandler)
//                    .failureHandler(failureHandler)
//                    .permitAll()
//            logout()
//                    .deleteCookies("JSESSIONID")
//                    .permitAll()
//            requiresChannel()
//                    .antMatchers("/login*")
//                    .requiresSecure()
//        }
//    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication().apply {
            withUser("admin").password(encoder().encode("admin")).roles("ADMIN")
            withUser("user").password(encoder().encode("user")).roles("USER")
        }
    }
}

@Bean
fun encoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()