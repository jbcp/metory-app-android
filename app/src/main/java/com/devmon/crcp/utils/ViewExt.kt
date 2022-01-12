package com.devmon.crcp.utils

import android.view.View
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import java.util.regex.Pattern

object ViewExt {

    fun getIng(emergency: Int?): String {
        return if (emergency == 1) {
            "모집중"
        } else {
            "마감"
        }
    }

    fun Fragment.toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    fun isEmailMatch(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isPasswordMatch(password: String) = Pattern.matches(
        "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&]).{8,15}.\$",
        password
    )
}

fun sexToString(sex: Int?) = when (sex) {
    1 -> "남자"
    2 -> "여자"
    else -> "남녀모두"
}

@BindingAdapter("visibleIf")
fun View.isVisible(visible: Boolean?) {
    visibility = visible?.let {
        if (it) View.VISIBLE else View.INVISIBLE
    } ?: View.INVISIBLE
}

@BindingAdapter("goneIf")
fun View.isVisibleOrGone(visible: Boolean?) {
    visibility = visible?.let {
        if (it) View.VISIBLE else View.GONE
    } ?: View.GONE
}

inline fun View.onThrottleClick(crossinline block: () -> Unit) {
    this.setOnClickListener(
        OnThrottleOnClickListener { block() }
    )
}