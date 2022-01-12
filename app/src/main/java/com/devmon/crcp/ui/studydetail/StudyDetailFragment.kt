package com.devmon.crcp.ui.studydetail

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devmon.crcp.BuildConfig
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseFragment
import com.devmon.crcp.databinding.FragmentStudyDetailBinding
import com.devmon.crcp.ui.MenuStateListener
import com.devmon.crcp.ui.alert.AlertFactory
import com.devmon.crcp.ui.alert.CommonAlertDialogFragment
import com.devmon.crcp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudyDetailFragment : BaseFragment<FragmentStudyDetailBinding, StudyDetailViewModel>(
    layoutId = R.layout.fragment_study_detail
) {

    override val viewModel: StudyDetailViewModel by viewModels()

    @Inject
    lateinit var alertFactory: AlertFactory

    private var menuStateListener: MenuStateListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        menuStateListener = context as? MenuStateListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuStateListener?.showMenu()
        binding.vm = viewModel

        if (BuildConfig.DEBUG) {
            binding.btnCancel.isVisible = true
        }

        viewModel.study.observe(viewLifecycleOwner, {
            binding.data = it
        })

        viewModel.state.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                StudyDetailViewModel.State.Prev -> Unit
                is StudyDetailViewModel.State.Next -> {
                    findNavController().navigate(
                        StudyDetailFragmentDirections.actionRecruitingDetailFragmentToStudyPrivacyPolicyFragment(it.study)
                    )
                }
            }
        })

        viewModel.studyAlert.observe(viewLifecycleOwner, EventObserver {
            CommonAlertDialogFragment()
                .title(it.title)
                .content(it.content)
                .confirmText("동의서 보기")
                .cancelText("확인")
                .confirmListener { dialog, _ ->
                    dialog.dismiss()
                    findNavController().navigate(StudyDetailFragmentDirections.actionToStudyConsent())
                }
                .show(childFragmentManager, CommonAlertDialogFragment::class.java.simpleName)
        })

        viewModel.alert.observe(viewLifecycleOwner, EventObserver {
            alertFactory.openConfirmAlert(childFragmentManager, it)
        })
    }
}