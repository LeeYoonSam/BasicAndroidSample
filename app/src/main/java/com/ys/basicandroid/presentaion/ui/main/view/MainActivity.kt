package com.ys.basicandroid.presentaion.ui.main.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.ys.basicandroid.R
import com.ys.basicandroid.databinding.ActivityMainBinding
import com.ys.basicandroid.presentaion.base.ui.BaseActivity
import com.ys.basicandroid.presentaion.base.ui.PermissionCheckActivity
import com.ys.basicandroid.presentaion.ui.main.viewmodel.MainViewModel
import com.ys.basicandroid.utils.extensions.isGrantedPermission
import com.ys.basicandroid.utils.extensions.parseUriPackageName
import com.ys.basicandroid.utils.extensions.permissionRationalOr
import com.ys.basicandroid.utils.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel by viewModels<MainViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		moveToContactPermissionTest()
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
				showToast("activityResultLauncher - ${Manifest.permission.READ_EXTERNAL_STORAGE} is Granted")
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
			showToast("settingResultLauncher - ${Manifest.permission.READ_EXTERNAL_STORAGE} is Granted")
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