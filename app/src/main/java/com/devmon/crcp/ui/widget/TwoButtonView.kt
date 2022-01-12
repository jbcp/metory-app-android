package com.devmon.crcp.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.devmon.crcp.R
import com.devmon.crcp.databinding.ViewTwoButtonBinding

/**
 * 2개 버튼이 묶여있는 뷰
 *
 * `app:onClickStart`, `app:onClickEnd` 로 데이터바인딩을 활용한 클릭리스너 구현 가능
 */
class TwoButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var binding: ViewTwoButtonBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.view_two_button,
        this,
        false,
    )

    init {
        addView(binding.root)
        context.withStyledAttributes(
            set = attrs,
            attrs = R.styleable.TwoButtonView,
        ) {
            val startVisibility = getBoolean(R.styleable.TwoButtonView_startVisible, true)
            val startText = getString(R.styleable.TwoButtonView_startText)
            val endText = getString(R.styleable.TwoButtonView_endText)

            binding.btnStart.isVisible = startVisibility
            binding.btnStart.text = startText
            binding.btnEnd.text = endText
        }
    }

    fun setOnStartClickListener(listener: OnClickListener) {
        binding.btnStart.setOnClickListener(listener)
    }

    fun setOnEndClickListener(listener: OnClickListener) {
        binding.btnEnd.setOnClickListener(listener)
    }
}

@BindingAdapter("app:onStartClick")
fun TwoButtonView.onClickStart(listener: View.OnClickListener) {
    setOnStartClickListener(listener)
}

@BindingAdapter("app:onEndClick")
fun TwoButtonView.onClickEnd(listener: View.OnClickListener) {
    setOnEndClickListener(listener)
}