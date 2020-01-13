package com.example.spring.sec.controller

import com.example.spring.sec.config.dataSource
import com.example.spring.sec.config.passwordEncoder
import com.example.spring.sec.model.Foo
import com.example.spring.sec.model.HttpResponse
import com.example.spring.sec.model.UserDto
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api")
@PreAuthorize("#oauth2.hasScope('read')")
class MainController constructor(
        private val userDetailsManager: UserDetailsManager = JdbcUserDetailsManager(dataSource())
) {
    @PostMapping("/reg")
    fun registration(@RequestBody userDetails: UserDto): HttpResponse {
        if (userDetailsManager.userExists(userDetails.username)) {
            return HttpResponse(
                    HttpServletResponse.SC_CONFLICT,
                    "registration error",
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

    @GetMapping("/test")
    fun test() = Foo().apply {
        head = "user"
    }

    @GetMapping("/admin/test")
    fun adminTest() = Foo().apply {
        head = "admin"
    }
}