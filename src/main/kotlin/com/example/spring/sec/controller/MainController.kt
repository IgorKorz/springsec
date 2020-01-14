package com.example.spring.sec.controller

import com.example.spring.sec.config.dataSource
import com.example.spring.sec.config.passwordEncoder
import com.example.spring.sec.model.ClientDto
import com.example.spring.sec.model.Foo
import com.example.spring.sec.model.HttpResponse
import com.example.spring.sec.model.UserDto
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException
import org.springframework.security.oauth2.provider.ClientRegistrationService
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api")
@PreAuthorize("#oauth2.hasScope('read')")
class MainController constructor(
        private val userDetailsManager: UserDetailsManager = JdbcUserDetailsManager(dataSource()),
        private val clientRegistrationService: ClientRegistrationService = JdbcClientDetailsService(dataSource())
                .apply {
                    setPasswordEncoder(passwordEncoder())
                }
) {
    @PostMapping("/user/reg")
    fun userRegistration(@RequestBody userDetails: UserDto): HttpResponse {
        if (userDetailsManager.userExists(userDetails.username)) {
            return HttpResponse(
                    HttpServletResponse.SC_CONFLICT,
                    "user registration error",
                    "user ${userDetails.username} already exists"
            )
        }

        userDetailsManager.createUser(User.builder()
                .username(userDetails.username)
                .password(passwordEncoder().encode(userDetails.password))
                .roles("USER")
                .build()
        )

        return HttpResponse(
                head = "registration success", body = "created new user ${userDetails.username}")
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/client/reg")
    fun clientRegistration(@RequestBody clientDetails: ClientDto) =
            try {
                clientRegistrationService.addClientDetails(clientDetails)

                HttpResponse(
                        head = "registration success",
                        body = "created new client ${clientDetails.clientId}"
                )
            } catch (e: ClientAlreadyExistsException) {
                HttpResponse(
                        HttpServletResponse.SC_CONFLICT,
                        "client registration error",
                        "user ${clientDetails.clientId} already exists"
                )
            }

    @GetMapping("/test")
    fun test() = Foo().apply {
        head = "user"
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/test")
    fun adminTest() = Foo().apply {
        head = "admin"
    }
}