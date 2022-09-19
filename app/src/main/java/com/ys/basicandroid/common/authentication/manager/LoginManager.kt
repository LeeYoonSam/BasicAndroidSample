package com.ys.basicandroid.common.authentication.manager

import android.content.IntentSender
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.ys.basicandroid.BuildConfig
import com.ys.basicandroid.common.log.L

class LoginManager (private val fragment: Fragment) {

	private val signInRequest: BeginSignInRequest by lazy {
		BeginSignInRequest.builder()
			.setPasswordRequestOptions(
				BeginSignInRequest.PasswordRequestOptions.builder()
					.setSupported(true)
					.build()
			)
			.setGoogleIdTokenRequestOptions(
				BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
					.setSupported(true)
					.setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
					.setFilterByAuthorizedAccounts(true)
					.build()
			)
			// Automatically sign in when exactly one credential is retrieved.
			.setAutoSelectEnabled(true)
			.build()
	}

	private val signInLauncher: ActivityResultLauncher<IntentSenderRequest> =
		fragment.registerForActivityResult(
			ActivityResultContracts.StartIntentSenderForResult()
		) { result: ActivityResult ->
			try {
				val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
				UserInfoManager.updateUserInfoBySignInCredential(credential)
			} catch (e: ApiException) {
				when (e.statusCode) {
					CommonStatusCodes.CANCELED -> {
						L.d("One-tap dialog was closed.")
						// Don't re-prompt the user.
					}
					CommonStatusCodes.NETWORK_ERROR -> {
						L.d("One-tap encountered a network error.")
						// Try again or just ignore.
					}
					else -> {
						L.d(
							"Couldn't get credential from result." +
								" (${e.localizedMessage})"
						)
					}
				}
			}
		}

	private val oneTapClient: SignInClient by lazy {
		Identity.getSignInClient(fragment.requireActivity())
	}

	fun beginSignIn() {
		oneTapClient.beginSignIn(signInRequest)
			.addOnSuccessListener(fragment.requireActivity()) { result ->
				try {

					val intentSenderRequest: IntentSenderRequest =
						IntentSenderRequest
							.Builder(result.pendingIntent.intentSender)
							.build()

					signInLauncher.launch(intentSenderRequest)
				} catch (e: IntentSender.SendIntentException) {
					L.e("Couldn't start One Tap UI: ${e.localizedMessage}")
				}
			}
			.addOnFailureListener(fragment.requireActivity()) { e ->
				// No saved credentials found. Launch the One Tap sign-up flow, or
				// do nothing and continue presenting the signed-out UI.
				L.d(e.localizedMessage)
			}
	}

	fun signOut(updateAction: () -> Unit) {
		oneTapClient.signOut()
			.addOnSuccessListener {
				UserInfoManager.clearUserInfo()
				updateAction()
			}
			.addOnFailureListener(fragment.requireActivity()) { e ->
				// No saved credentials found. Launch the One Tap sign-up flow, or
				// do nothing and continue presenting the signed-out UI.
				L.d(e.localizedMessage)
			}
	}
}