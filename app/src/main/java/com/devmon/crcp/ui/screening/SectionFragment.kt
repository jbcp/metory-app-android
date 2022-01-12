package com.devmon.crcp.ui.screening

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseFragment
import com.devmon.crcp.databinding.FragmentSectionBinding
import com.devmon.crcp.ui.alert.AlertFactory
import com.devmon.crcp.ui.screening.components.SurveyScreen
import com.devmon.crcp.utils.ViewExt.toast
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SectionFragment : BaseFragment<FragmentSectionBinding, ScreeningViewModel>(R.layout.fragment_section) {

    // NavHostFragment 의 부모인 SelfScreeningFragment
    override val viewModel: ScreeningViewModel by viewModels({ requireParentFragment().requireParentFragment() })

    @Inject
    lateinit var alertFactory: AlertFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt(SECTION_ID) ?: 1

        viewModel.setCurrentPage(id)

        binding.composeView.setContent {
            MdcTheme {
                SurveyScreen(
                    viewModel = viewModel,
                    sectionId = id,
                    onNextButtonClick = {
                        lifecycleScope.launch {
                            if (validateQuestion(id)) {
                                if (viewModel.isLastPage(id)) {
                                    viewModel.updateSurvey()
                                } else {
                                    findNavController().navigate(
                                        R.id.action_global_sectionFragment,
                                        bundleOf(SECTION_ID to viewModel.getNextPage()),
                                    )
                                }
                            }
                        }
                    },
                    onErrorClick = {
                        viewModel.goToHome()
                    }
                )
            }
        }
    }

    private suspend fun validateQuestion(sectionId: Int): Boolean {
        val unchecked = viewModel.findNoneAnswerList(sectionId)

        for (message in unchecked) {
            val result = suspendCoroutine<Boolean> { cont ->
                alertFactory.openSelfScreeningAlert(
                    fragmentManager = childFragmentManager,
                    msg = message,
                    onConfirm = {
                        cont.resume(true)
                    },
                    onCancel = {
                        cont.resume(false)
                    }
                )
            }

            if (!result) {
                return false
            }
        }

        if (sectionId != 3) {
            return true
        }

        val (isValid, message) = viewModel.validateDrugList()
        if (!isValid) {
            toast(message)
            return false
        }

        return true
    }

    companion object {
        private const val SECTION_ID = "SECTION_ID"

        fun newInstance(sectionId: Int): SectionFragment {
            return SectionFragment().apply {
                arguments = bundleOf(SECTION_ID to sectionId)
            }
        }
    }
}