package com.devmon.crcp.ui.study

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseFragment
import com.devmon.crcp.databinding.FragmentStudyBinding
import com.devmon.crcp.ui.MenuStateListener
import com.devmon.crcp.ui.alert.CommonAlertDialogFragment
import com.devmon.crcp.ui.model.Recruiting
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudyFragment : BaseFragment<FragmentStudyBinding, StudyViewModel>(
    layoutId = R.layout.fragment_study
) {

    private val studyAdapter: StudyAdapter by lazy { StudyAdapter() }
    override val viewModel: StudyViewModel by viewModels()

    /**
     * 다이얼로그 뒤로가기
     */
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            CommonAlertDialogFragment()
                .title("종료하기")
                .content("앱을 종료하시겠습니까?")
                .confirmListener { dialog, _ ->
                    dialog.dismiss()
                    activity?.finish()
                }
                .show(childFragmentManager, CommonAlertDialogFragment::class.java.simpleName)
        }
    }

    private var menuStateListener: MenuStateListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        menuStateListener = context as? MenuStateListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuStateListener?.showMenu()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)

        binding.rycRecruit.adapter = studyAdapter.apply {
            setOnClickListener(object : StudyAdapter.OnClickListener {
                override fun onClick(recruitingData: Recruiting) {
                    findNavController().navigate(
                        StudyFragmentDirections.actionStudyFragmentToStudyDetailFragment(recruitingData)
                    )
                }
            })
        }

        viewModel.allRecruiting()

        viewModel.recruitings.observe(viewLifecycleOwner) {
            studyAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        callback.remove()
        super.onDestroyView()
    }
}