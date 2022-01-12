package com.devmon.crcp.ui.consent

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.devmon.crcp.R

@BindingAdapter("app:consentStage")
fun TextView.setCsStage(csStage: Int?) {
    text = when (csStage) {
        5 -> "연구자가 귀하의 서명을 확인하고 있습니다."
        6 -> "동의"
        else -> "미동의"
    }

    setTextColor(
        ContextCompat.getColor(context,
            when (csStage) {
                5 -> R.color.color_blue_900
                6 -> android.R.color.holo_blue_dark
                else -> android.R.color.holo_red_dark
            }
        )
    )
}