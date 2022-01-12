package com.devmon.crcp.ui.studydetail

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:studySex")
fun TextView.fetchStudySex(ssex: Int?) {
    text = when (ssex) {
        1 -> "남자"
        2 -> "여자"
        else -> "성별무관"
    }
}