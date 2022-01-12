package com.devmon.crcp.ui.studydetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseFragment
import com.devmon.crcp.databinding.FragmentStudyPrivacyPolicyBinding
import com.devmon.crcp.ui.alert.AlertFactory
import com.devmon.crcp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudyPrivacyPolicyFragment : BaseFragment<FragmentStudyPrivacyPolicyBinding, StudyPrivacyPolicyViewModel>(
    layoutId = R.layout.fragment_study_privacy_policy
) {

    override val viewModel: StudyPrivacyPolicyViewModel by viewModels()

    @Inject
    lateinit var alertFactory: AlertFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        viewModel.state.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                StudyPrivacyPolicyViewModel.State.Prev -> findNavController().popBackStack()
                is StudyPrivacyPolicyViewModel.State.Next -> {
                    findNavController().navigate(
                        StudyPrivacyPolicyFragmentDirections.actionStudyPrivacyPolicyFragmentToUserInfoConfirmFragment(it.study)
                    )
                }
            }
        })

        viewModel.alert.observe(viewLifecycleOwner, EventObserver {
            alertFactory.openConfirmAlert(childFragmentManager, it)
        })
    }
}