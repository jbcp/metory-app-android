package com.devmon.crcp.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.devmon.crcp.R
import com.devmon.crcp.utils.dp2px

class BaseButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setTextColor(ContextCompat.getColor(context, R.color.color_white))
        minWidth = context.dp2px(140).toInt()
        isClickable = true
        isFocusable = true
    }
}