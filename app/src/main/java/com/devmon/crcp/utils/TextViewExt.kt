package com.devmon.crcp.utils

import android.text.Html
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("app:textColor")
fun TextView.setTextColorRes(resId: Int) {
    setTextColor(ContextCompat.getColor(context, resId))
}

@BindingAdapter("app:htmlText")
fun TextView.setHtmlText(htmlText: String) {
    val html = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(htmlText)
    }
    text = html
}