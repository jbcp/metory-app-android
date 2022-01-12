package com.devmon.crcp.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseActivity
import com.devmon.crcp.databinding.ActivitySplashBinding
import com.devmon.crcp.ui.MainActivity
import com.devmon.crcp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(
    layoutId = R.layout.activity_splash
) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.homeEvent.observe(this, EventObserver {
            startActivity(
                Intent(this@SplashActivity, MainActivity::class.java).apply {
                    putExtra("home", "home")
                }
            )
            finish()
        })

        viewModel.loginEvent.observe(this, EventObserver {
            startActivity(
                Intent(this@SplashActivity, MainActivity::class.java).apply {
                    putExtra("page", intent.getSerializableExtra("page"))
                }
            )
            finish()
        })

        viewModel.navigate()
    }
}