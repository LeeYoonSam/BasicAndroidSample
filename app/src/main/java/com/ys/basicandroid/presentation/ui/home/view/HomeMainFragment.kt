package com.ys.basicandroid.presentation.ui.home.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ys.basicandroid.common.log.L
import com.ys.basicandroid.databinding.FragmentHomeMainBinding
import com.ys.basicandroid.presentation.ui.home.viewmodel.HomeMainViewModel
import com.ys.basicandroid.presentation.ui.signin.view.SignInActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeMainFragment : Fragment() {

	private lateinit var binding: FragmentHomeMainBinding

	private val viewModel by viewModels<HomeMainViewModel>()

	// Firebase instance variables
	private lateinit var auth: FirebaseAuth

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		binding = FragmentHomeMainBinding.inflate(layoutInflater).apply {
			lifecycleOwner = viewLifecycleOwner
			viewModel = this@HomeMainFragment.viewModel
		}

		// Initialize Firebase Auth and check if the user is signed in
		auth = Firebase.auth

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.tvLoginToGoogle.setOnClickListener {
			if (auth.currentUser == null) {
				// Not signed in, launch the Sign In activity
				startActivity(Intent(requireContext(), SignInActivity::class.java))
			}
		}

		binding.tvLogoutToGoogle.setOnClickListener {
			logOut()
		}
	}

	override fun onStart() {
		super.onStart()
		// Check if user is signed in.
		checkSignedIn()
	}

	private fun checkSignedIn() {
		if (auth.currentUser != null) {
			val currentUser = auth.currentUser
			updateUI(currentUser)
		}
	}

	private fun getPhotoUrl(): String? {
		val user = auth.currentUser
		return user?.photoUrl?.toString()
	}

	private fun getUserName(): String? {
		val user = auth.currentUser
		return if (user != null) {
			user.displayName
		} else ""
	}

	private fun logOut() {
		AuthUI.getInstance()
			.signOut(requireContext())
			.addOnCompleteListener {
				updateUI(auth.currentUser)
			}
	}

	private fun updateUI(firebaseUser: FirebaseUser?) {
		if (firebaseUser != null) {
			binding.tvLoginToGoogle.visibility = View.GONE
			binding.tvLogoutToGoogle.visibility = View.VISIBLE

			L.i("firebaseUser: name - ${getUserName()}, photoUrl - ${getPhotoUrl()}")
			return
		}

		binding.tvLoginToGoogle.visibility = View.VISIBLE
		binding.tvLogoutToGoogle.visibility = View.GONE
	}
}