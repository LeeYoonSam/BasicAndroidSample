package com.ys.basicandroid.common.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val photoUrl: String
)