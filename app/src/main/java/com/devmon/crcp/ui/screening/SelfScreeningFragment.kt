package com.devmon.crcp.ui.screening

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseFragment
import com.devmon.crcp.databinding.FragmentSelfScreeningBinding
import com.devmon.crcp.utils.EventObserver
import com.devmon.crcp.utils.ViewExt.toast
import dagger.hilt.android.AndroidEntryPoint

/**
 * 자가진단 페이지
 */
@AndroidEntryPoint
class SelfScreeningFragment :
    BaseFragment<FragmentSelfScreeningBinding, ScreeningViewModel>(R.layout.fragment_self_screening) {

    override val viewModel: ScreeningViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.topProgress.observe(viewLifecycleOwner) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.pbScreening.setProgress(it, true)
            } else {
                binding.pbScreening.progress = it
            }
        }

        viewModel.toast.observe(viewLifecycleOwner, EventObserver {
            toast(it)
        })

        viewModel.goToHomeEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(SelfScreeningFragmentDirections.actionToStudyDetail())
        })

        viewModel.progressVisibility.observe(viewLifecycleOwner) {
            binding.pbScreening.visibility = if (it == true) View.VISIBLE else View.INVISIBLE
        }
    }
}