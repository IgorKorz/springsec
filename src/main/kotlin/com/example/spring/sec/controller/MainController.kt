package com.example.spring.sec.controller
import com.example.spring.sec.model.Foo
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
@Validated
class MainController {
    @GetMapping("/test")
    fun test() = Foo().apply {
        head = "user"
    }

    @GetMapping("/admin/test")
    fun adminTest() = Foo().apply {
        head = "admin"
    }
}