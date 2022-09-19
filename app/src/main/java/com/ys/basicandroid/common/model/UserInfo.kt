package com.ys.basicandroid.common.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
	val userId: String,
	val password: String,
	var idToken: String,
	var userName: String,
	var profilePictureUri: String
)