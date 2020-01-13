package com.example.spring.sec.model

import javax.servlet.http.HttpServletResponse

data class HttpResponse(
        val httpCode: Int = HttpServletResponse.SC_OK,
        val head: String,
        val body: String
)