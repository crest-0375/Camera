package com.app.vocab.features.auth.domain.model

data class User(
    val name: String = "",
    val email: String = "",
    val password: String = ""
)