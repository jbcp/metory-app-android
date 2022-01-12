package com.devmon.crcp.utils

import android.view.View
import androidx.databinding.BindingAdapter

interface OnThrottleClickListener {
    fun onClick()
}

@BindingAdapter(value = ["onThrottleClick"])
fun setThrottleClick(view: View, click: OnThrottleClickListener?) {
    view.onThrottleClick {
        click?.onClick()
    }
}
