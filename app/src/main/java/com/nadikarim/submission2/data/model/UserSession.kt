package com.nadikarim.submission2.data.model

data class UserSession(
    val name: String,
    val token: String,
    val userId: String,
    val isLogin: Boolean
)