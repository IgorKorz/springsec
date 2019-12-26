package com.example.spring.sec.controller
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {
    @GetMapping(value = ["/", "/index"])
    fun index() = "/index"

    @GetMapping("/admin")
    fun admin() = "/admin"

    @GetMapping("/user")
    fun user() = "/user"

    @GetMapping("/about")
    fun about() = "/about"

    @GetMapping("/login")
    fun login() = "/login"

    @GetMapping("/403")
    fun error403() = "/error/403"
}