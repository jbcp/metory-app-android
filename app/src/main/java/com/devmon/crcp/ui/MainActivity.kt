package com.devmon.crcp.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseActivity
import com.devmon.crcp.databinding.ActivityMainBinding
import com.devmon.crcp.push.Page
import com.devmon.crcp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(
    layoutId = R.layout.activity_main
), MenuStateListener {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel

        initDrawer()
        observeViewModel()
        handleNavigationEventFromSplash()
    }

    private fun handleNavigationEventFromSplash() {
        val home = intent.getStringExtra("home")
        val page = intent.getSerializableExtra("page") as? Page
        if (home != null) {
            findNavController(R.id.container_main).navigate(R.id.action_to_study_detail)
            if (page != null) {
                when (page) {
                    Page.NONE -> Unit
                    Page.CONSENT -> findNavController(R.id.container_main).navigate(R.id.action_to_study_consent)
                    Page.QNA -> findNavController(R.id.container_main).navigate(R.id.action_to_chatDetailFragment)
                }
            }
        } else {
            findNavController(R.id.container_main).navigate(R.id.action_to_login)
        }
    }

    private fun initDrawer() {
        binding.layoutDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        binding.ivHamburger.setOnClickListener {
            openDrawer()
        }
    }

    private fun observeViewModel() {
        viewModel.drawerOpenEvent.observe(this, EventObserver {
            if (it) {
                openDrawer()
            } else {
                closeDrawer()
            }
        })

        viewModel.goToDrawerMenuView.observe(this, EventObserver {
            it.navigate(findNavController(R.id.container_main))
        })

        viewModel.goToEmptyStudyEvent.observe(this, EventObserver {
            findNavController(R.id.container_main).navigate(R.id.action_global_emptyStudyFragment)
        })
    }

    private fun openDrawer() {
        if (!binding.layoutDrawer.isDrawerOpen(GravityCompat.START)) {
            binding.layoutDrawer.openDrawer(GravityCompat.START)
        }
    }

    private fun closeDrawer() {
        binding.layoutDrawer.closeDrawer(GravityCompat.START, false)
    }

    override fun onBackPressed() {
        if (binding.layoutDrawer.isDrawerOpen(GravityCompat.START)) {
            binding.layoutDrawer.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()
    }

    override fun showMenu() {
        binding.ivHamburger.visibility = View.VISIBLE
    }

    override fun hideMenu() {
        if (isBindingInitialized) {
            binding.ivHamburger.visibility = View.INVISIBLE
        }
    }
}