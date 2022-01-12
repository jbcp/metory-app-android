package com.devmon.crcp.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseActivity
import com.devmon.crcp.databinding.ActivityTermsBinding
import com.devmon.crcp.utils.onThrottleClick

class TermsActivity : BaseActivity<ActivityTermsBinding>(R.layout.activity_terms) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ivClose.onThrottleClick {
            finish()
        }

//        binding.clTerms.setOnApplyWindowInsetsListener { v, insets ->
//            binding.clTerms.updatePadding(bottom = v.paddingBottom + insets.systemWindowInsetBottom)
//            return@setOnApplyWindowInsetsListener insets.consumeSystemWindowInsets()
//        }

        binding.terms = intent.getStringExtra(EXTRA_TERMS)
    }

    companion object {
        private const val EXTRA_TERMS = "EXTRA_TERMS"

        fun startActivity(context: Context, terms: String) {
            context.startActivity(
                Intent(context, TermsActivity::class.java).apply {
                    putExtra(EXTRA_TERMS, terms)
                }
            )
        }
    }
}