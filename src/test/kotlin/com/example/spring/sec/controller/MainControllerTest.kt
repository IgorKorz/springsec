package com.example.spring.sec.controller

import com.example.spring.sec.SpringSecApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.json.JacksonJsonParser
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.security.web.FilterChainProxy
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.context.WebApplicationContext


@WebAppConfiguration
@SpringBootTest(classes = [SpringSecApplication::class])
class MainControllerTest @Autowired constructor(
        private val webAppContext: WebApplicationContext,
        private val springSecurityFilterChain: FilterChainProxy
) {
    companion object {
        const val CLIENT_ID = "clientIdPassword"
        const val CLIENT_SECRET = "secret"
        const val CONTENT_TYPE = "application/json;charset=UTF-8"
    }

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .addFilter<DefaultMockMvcBuilder>(springSecurityFilterChain)
                .build()
    }

    @Test
    fun `given no token when get secure request then unauthorized`() {
        mockMvc.perform(get("/api/test")).andExpect(status().isUnauthorized)
    }

    @Test
    fun `given invalid role when get secure request then forbidden`() {
        val accessToken = obtainAccessToken("user", "user")
        mockMvc.perform(get("/api/admin/test")
                .header("Authorization", "Bearer $accessToken")
        ).andExpect(status().isForbidden)
    }

    @Test
    fun `given token when get secure request then ok`() {
        val accessToken = obtainAccessToken("admin", "admin")
        mockMvc.perform(get("/api/admin/test")
                .header("Authorization", "Bearer $accessToken")
        ).andExpect(status().isOk)
    }

    private fun obtainAccessToken(username: String, password: String): String {
        val params: MultiValueMap<String, String> = LinkedMultiValueMap(mapOf(
                "grant_type" to listOf("password"),
                "client_id" to listOf(CLIENT_ID),
                "username" to listOf(username),
                "password" to listOf(password)
        ))
        val result = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().contentType(CONTENT_TYPE))
        val resultString = result.andReturn().response.contentAsString
        return JacksonJsonParser().parseMap(resultString)["access_token"].toString()
    }
}