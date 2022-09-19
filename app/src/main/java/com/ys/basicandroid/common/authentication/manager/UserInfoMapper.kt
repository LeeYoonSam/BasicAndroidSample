package com.ys.basicandroid.common.authentication.manager

import com.google.android.gms.auth.api.identity.SignInCredential
import com.ys.basicandroid.common.model.UserInfo

object UserInfoMapper {
	fun getUserInfo(signInCredential: SignInCredential): UserInfo {
		return UserInfo(
			userId = signInCredential.id,
			password = signInCredential.password.orEmpty(),
			idToken = signInCredential.googleIdToken.orEmpty(),
			userName = signInCredential.displayName.orEmpty(),
			profilePictureUri = signInCredential.profilePictureUri.toString()
		)
	}
}