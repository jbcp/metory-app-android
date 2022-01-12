package com.devmon.crcp.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.devmon.crcp.R
import com.devmon.crcp.utils.dp2px

class StudyRegisterHeaderTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setBackgroundColor(ContextCompat.getColor(context, R.color.color_blue_400))
        gravity = Gravity.CENTER_VERTICAL
        setPaddingRelative(
            context.dp2px(16).toInt(),
            context.dp2px(8).toInt(),
            context.dp2px(16).toInt(),
            context.dp2px(8).toInt(),
        )
        setTextColor(ContextCompat.getColor(context, R.color.color_white))
        text = context.getString(R.string.recruiting_information)
        setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_study_register_header_arrow,
            0,
            0,
            0,
        )
    }
}