package com.example.spring.sec.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.core.io.Resource
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.jdbc.datasource.init.DatabasePopulator
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import javax.sql.DataSource

@Configuration
class DataSourceConfig @Autowired constructor(
        @Value("classpath:schema.sql")
        private val schemaScript: Resource
) : EnvironmentAware {
    private lateinit var env: Environment

    override fun setEnvironment(environment: Environment) {
        this.env = environment
    }

    @Bean
    fun dataSourceInitializer(dataSource: DataSource) = DataSourceInitializer().apply {
        setDataSource(dataSource())
        setDatabasePopulator(databasePopulator())
    }

    @Bean
    fun dataSource(): DataSource = DriverManagerDataSource().apply {
        setDriverClassName(env["spring.datasource.driver-class-name"]!!)
        url = env["spring.datasource.url"]
        username = env["spring.datasource.username"]
        password = env["spring.datasource.password"]
    }

    private fun databasePopulator(): DatabasePopulator = ResourceDatabasePopulator().apply {
        addScript(schemaScript)
    }
}