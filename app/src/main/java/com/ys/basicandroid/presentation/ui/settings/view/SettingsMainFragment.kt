package com.ys.basicandroid.presentation.ui.settings.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.ys.basicandroid.R

class SettingsMainFragment : PreferenceFragmentCompat() {

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.root_preferences, rootKey)
	}
}