package com.devmon.crcp.utils

import android.content.Context
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun Context.dp2px(dp: Int) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        this.resources.displayMetrics)

fun Context.showKeyboard() {
    val manager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        ?: return
    manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun FragmentActivity.hideKeyboard() {
    val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        ?: return
    manager.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    requireActivity().hideKeyboard()
}
