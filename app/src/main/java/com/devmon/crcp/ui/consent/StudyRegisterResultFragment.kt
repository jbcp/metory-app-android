package com.devmon.crcp.ui.consent

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseFragment
import com.devmon.crcp.databinding.FragmentStudyRegisterResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudyRegisterResultFragment : BaseFragment<FragmentStudyRegisterResultBinding, StudyRegisterResultViewModel>(
    layoutId = R.layout.fragment_study_register_result
) {

    override val viewModel: StudyRegisterResultViewModel by viewModels()

    /**
     * 동의서 목록 화면 리프레시 시키기 위함
     */
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
//            findNavController().navigate(
//                StudyRegisterResultFragmentDirections.actionStudyRegisterResultFragmentToStudyConsentFragment()
//            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)

        binding.btnMove.setOnClickListener {
//            findNavController().navigate(
//                StudyRegisterResultFragmentDirections.actionStudyRegisterResultFragmentToStudyConsentFragment()
//            )
        }
    }

    override fun onDestroyView() {
        callback.remove()
        super.onDestroyView()
    }
}