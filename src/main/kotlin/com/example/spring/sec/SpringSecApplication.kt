package com.example.spring.sec

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SpringSecApplication

fun main(args: Array<String>) {
    SpringApplication.run(SpringSecApplication::class.java, *args)
}
