package com.example.spring.sec.controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class SpringSecController {

    @GetMapping("/users")
    fun getUsers() = "success"
}