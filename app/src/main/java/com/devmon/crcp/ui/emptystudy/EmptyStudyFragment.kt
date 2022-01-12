package com.devmon.crcp.ui.emptystudy

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devmon.crcp.NavMainDirections
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseFragment
import com.devmon.crcp.databinding.FragmentEmptyStudyBinding
import com.devmon.crcp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmptyStudyFragment : BaseFragment<FragmentEmptyStudyBinding, EmptyStudyViewModel>(
    R.layout.fragment_empty_study
) {

    override val viewModel: EmptyStudyViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        viewModel.goStudyListEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(NavMainDirections.actionToStudyDetail())
        })

        val message = arguments?.getString(EXTRA_ERROR_MESSAGE) ?: getString(R.string.error_empty_study)

        viewModel.setErrorMessage(message)
    }

    companion object {
        private const val EXTRA_ERROR_MESSAGE = "EXTRA_ERROR_MESSAGE"

        fun getIntent(message: String): Intent {
            return Intent().apply {
                putExtras(bundleOf(EXTRA_ERROR_MESSAGE to message))
            }
        }
    }
}