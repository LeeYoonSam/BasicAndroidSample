package com.ys.basicandroid.presentation.ui.signin.view

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ys.basicandroid.common.log.L
import com.ys.basicandroid.utils.extensions.showToast

class SignInActivity : AppCompatActivity() {

	// Firebase instance variables
	private lateinit var auth: FirebaseAuth

	private val signIn: ActivityResultLauncher<Intent> =
		registerForActivityResult(FirebaseAuthUIActivityResultContract(), this::onSignInResult)

	override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
		super.onCreate(savedInstanceState, persistentState)

		// Initialize FirebaseAuth
		auth = Firebase.auth
	}

	public override fun onStart() {
		super.onStart()

		// If there is no signed in user, launch FirebaseUI
		// Otherwise head to MainActivity
		if (Firebase.auth.currentUser == null) {
			// Sign in with FirebaseUI, see docs for more details:
			// https://firebase.google.com/docs/auth/android/firebaseui
			val signInIntent = AuthUI.getInstance()
				.createSignInIntentBuilder()
				.setAvailableProviders(listOf(
					AuthUI.IdpConfig.GoogleBuilder().build(),
				))
				.build()

			signIn.launch(signInIntent)
		} else {
			goToMainActivity()
		}
	}

	private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
		if (result.resultCode == RESULT_OK) {
			L.d("Sign in successful!")
			goToMainActivity()
		} else {
			showToast("There was an error signing in")

			val response = result.idpResponse
			if (response == null) {
				L.w("Sign in canceled")
			} else {
				L.w("Sign in error", response.error?.message)
			}
		}
	}

	private fun goToMainActivity() {
		setResult(RESULT_OK)
		finish()
	}
}