package com.devmon.crcp.ui.consent

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseFragment
import com.devmon.crcp.databinding.FragmentConsentCheckBinding
import com.devmon.crcp.ui.alert.AlertFactory
import com.devmon.crcp.utils.EventObserver
import com.devmon.crcp.utils.ViewExt.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudyConsentCheckFragment :
    BaseFragment<FragmentConsentCheckBinding, StudyConsentCheckViewModel>(R.layout.fragment_consent_check) {

    override val viewModel: StudyConsentCheckViewModel by viewModels()
    private val adapter: ConsentCheckAdapter by lazy { ConsentCheckAdapter() }

    @Inject
    lateinit var alertFactory: AlertFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            vm = viewModel
            rycConsentCheck.layoutManager = LinearLayoutManager(requireContext())
            rycConsentCheck.adapter = adapter

            adapter.submitList(viewModel.getCheckList())
        }

        viewModel.next.observe(viewLifecycleOwner, EventObserver {
            if (adapter.isCheck()) {
                findNavController().navigate(
                    StudyConsentCheckFragmentDirections.actionStudyConsentCheckFragmentToStudySignFragment(
                        viewModel.consent.value
                    )
                )
            } else {
                toast("전자 동의 체크를 확인해주세요.")
            }   
        })
    }
}
