package com.ys.basicandroid.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.bundleOf

inline fun <reified T : Activity> Context.buildIntent(
    vararg argument: Pair<String, Any?>
): Intent = Intent(this, T::class.java).apply {
    putExtras(bundleOf(*argument))
}

inline fun <reified T: Activity> Context.startActivity(
    vararg argument: Pair<String, Any?>
) {
    startActivity(buildIntent<T>(*argument))
}

fun Int.dp2px() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun View.hideKeyboard(): Boolean {
    clearFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}