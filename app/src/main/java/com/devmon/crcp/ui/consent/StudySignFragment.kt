package com.devmon.crcp.ui.consent

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseFragment
import com.devmon.crcp.databinding.FragmentMyStudySignBinding
import com.devmon.crcp.ui.alert.AlertFactory
import com.devmon.crcp.utils.EventObserver
import com.devmon.crcp.utils.ViewExt.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudySignFragment :
    BaseFragment<FragmentMyStudySignBinding, StudySignViewModel>(R.layout.fragment_my_study_sign) {

    override val viewModel: StudySignViewModel by viewModels()

    @Inject
    lateinit var alertFactory: AlertFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        viewModel.state.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                StudySignViewModel.State.Prev -> findNavController().popBackStack()
                StudySignViewModel.State.Next -> {
                    findNavController().navigate(
                        StudySignFragmentDirections.actionStudySignFragmentToStudyConsentFragment()
                    )
                }
            }
        })

        viewModel.toast.observe(viewLifecycleOwner, EventObserver {
            toast(it)
        })

        viewModel.alert.observe(viewLifecycleOwner, EventObserver {
            alertFactory.openConfirmAlert(childFragmentManager, it)
        })
    }
}
