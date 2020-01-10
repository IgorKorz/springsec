package com.example.spring.sec.config

import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.stereotype.Component
import javax.sql.DataSource

private val env = EnvironmentHolder.env!!

@Bean
fun dataSource(): DataSource = DriverManagerDataSource().apply {
    setDriverClassName(env["spring.datasource.driver-class-name"]!!)
    url = env["spring.datasource.url"]
    username = env["spring.datasource.username"]
    password = env["spring.datasource.password"]
}

@Component
private object EnvironmentHolder : EnvironmentAware {
    var env: Environment? = null

    override fun setEnvironment(environment: Environment) {
        this.env = environment
    }
}