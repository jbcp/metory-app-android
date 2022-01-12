package com.devmon.crcp.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:imageUrl")
fun ImageView.fetchUrl(url: String?) {
    if (url.isNullOrEmpty()) return
    GlideApp.with(context)
        .load(url)
        .into(this)
}