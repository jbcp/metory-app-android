package com.devmon.crcp.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.devmon.crcp.R
import com.devmon.crcp.databinding.ViewUserInfoBinding

class UserInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: ViewUserInfoBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.view_user_info,
        this,
        false,
    )

    init {
        addView(binding.root)
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        context.withStyledAttributes(
            set = attrs,
            attrs = R.styleable.UserInfoView,
        ) {
            val label = getString(R.styleable.UserInfoView_uiv_label)
            val content = getString(R.styleable.UserInfoView_uiv_content)
            val icon = getResourceId(R.styleable.UserInfoView_uiv_icon, -1)

            binding.tvLabel.text = label
            binding.tvContent.text = content
            if (icon != -1) {
                binding.ivIcon.setImageResource(icon)
            }
        }
    }

    fun fetchContent(text: String?) {
        binding.tvContent.text = text
    }

    fun onClick(click: OnClickListener?) {
        binding.ivIcon.setOnClickListener(click)
    }
}

@BindingAdapter("app:uiv_content")
fun UserInfoView.setContent(text: String?) {
    fetchContent(text)
}