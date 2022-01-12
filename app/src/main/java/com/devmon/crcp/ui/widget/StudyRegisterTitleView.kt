package com.devmon.crcp.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.devmon.crcp.R
import com.devmon.crcp.utils.dp2px

class StudyRegisterTitleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.color_blue_400)
        strokeWidth = context.dp2px(2)
    }

    init {
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
        setTypeface(null, Typeface.BOLD)
        setPaddingRelative(0, 0, 0, context.dp2px(6).toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawLine(
            0f,
            measuredHeight.toFloat(),
            context.dp2px(30),
            measuredHeight.toFloat(),
            paint,
        )
    }
}