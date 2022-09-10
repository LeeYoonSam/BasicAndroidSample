package com.ys.basicandroid.presentation.ui.main.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ys.basicandroid.R
import com.ys.basicandroid.databinding.ActivityMainBinding
import com.ys.basicandroid.presentation.base.ui.BaseActivity
import com.ys.basicandroid.presentation.base.ui.PermissionCheckActivity
import com.ys.basicandroid.presentation.ui.main.viewmodel.MainViewModel
import com.ys.basicandroid.utils.extensions.isGrantedPermission
import com.ys.basicandroid.utils.extensions.parseUriPackageName
import com.ys.basicandroid.utils.extensions.permissionRationalOr
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel by viewModels<MainViewModel>()

	private lateinit var navController: NavController

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setUpNavigation()

		moveToContactPermissionTest()
	}

	private fun setUpNavigation() {
		val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
		navController = navHostFragment.navController

		binding.bnvMain.run {
			setupWithNavController(navController)
			setOnItemSelectedListener {
				// menu id에 해당하는 탭으로 이동
				navigateBottomMenu(it.itemId)
				true
			}
		}
	}

	private fun navigateBottomMenu(destinationId: Int, args: Bundle? = null) {
		navController.navigate(destinationId, args)
	}
	
	override fun initObserve() {
		viewModel.contributors.observe(this) {
			Timber.d("contributors: $it")
		}
	}

	override fun initData() {
        viewModel.getContributors()
    }

	private fun moveToContactPermissionTest() {
		activityResultLauncher.launch(
			PermissionCheckActivity.generateIntent(
				context = this,
				requestPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
			)
		)
	}

	private val activityResultLauncher = registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) { result: ActivityResult ->
		when (result.resultCode) {
			// 권한이 허용되어있으면 항상 호출
			PermissionCheckActivity.PERMISSION_RESULT_SUCCESS -> {
				Timber.d("activityResultLauncher - ${Manifest.permission.READ_EXTERNAL_STORAGE} is Granted")
			}
			PermissionCheckActivity.PERMISSION_RESULT_FAIL -> {
				permissionRationalOr(
					permission = Manifest.permission.READ_EXTERNAL_STORAGE,
					rationaleAction = {
						Toast.makeText(
							this,
							getString(R.string.denied_external_storage_message),
							Toast.LENGTH_SHORT
						).show()
					},
					deniedAction = {
						moveToSetting()
					}
				)
			}
		}
	}

	private val settingResultLauncher = registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) {
		if (isGrantedPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
			Timber.d("settingResultLauncher - ${Manifest.permission.READ_EXTERNAL_STORAGE} is Granted")
		}
	}

	private fun moveToSetting() {
		AlertDialog.Builder(this).run {
			setTitle(R.string.denied_external_storage_title)
			setMessage(R.string.move_to_setting_permission)
			setPositiveButton(
				R.string.do_setting
			) { _, _ ->
				parseUriPackageName()?.let { packageNameUri ->
					val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
					intent.data = packageNameUri
					settingResultLauncher.launch(intent)
				}
			}
			setNegativeButton(
				R.string.cancel
			) { _, _ -> }
			show()
		}
	}
}