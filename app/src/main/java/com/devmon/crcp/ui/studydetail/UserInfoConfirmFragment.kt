package com.devmon.crcp.ui.studydetail

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devmon.crcp.NavMainDirections
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseFragment
import com.devmon.crcp.databinding.FragmentUserInfoConfirmBinding
import com.devmon.crcp.domain.model.Alert
import com.devmon.crcp.ui.alert.AlertFactory
import com.devmon.crcp.ui.auth.certification.PassResultContract
import com.devmon.crcp.utils.EventObserver
import com.devmon.crcp.utils.ViewExt.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.threeten.bp.LocalDate

@AndroidEntryPoint
class UserInfoConfirmFragment : BaseFragment<FragmentUserInfoConfirmBinding, UserInfoConfirmViewModel>(
    layoutId = R.layout.fragment_user_info_confirm
) {
    override val viewModel: UserInfoConfirmViewModel by viewModels()

    @Inject
    lateinit var alertFactory: AlertFactory

    private val resultLauncher = registerForActivityResult(PassResultContract()) {
        if (it == null) {
            alertFactory.openConfirmAlert(childFragmentManager, Alert("본인인증", "잠시 후 다시 시도해주세요."))
            return@registerForActivityResult
        }
        viewModel.handlePassResult(it)
        viewModel.updateInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        viewModel.state.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                UserInfoConfirmViewModel.State.Prev -> findNavController().popBackStack()
                UserInfoConfirmViewModel.State.Next -> {
                    toast("연구 참가 신청 되었습니다")
                    findNavController().navigate(
                        UserInfoConfirmFragmentDirections.actionUserInfoConfirmFragmentToStudyConsentFragment()
                    )
                }
            }
        })

        viewModel.birthEdit.observe(viewLifecycleOwner, EventObserver {
            val dialog = DatePickerDialog(
                requireContext(),
                { datePicker: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    viewModel.setDate(LocalDate.of(year, month + 1, dayOfMonth))
                    viewModel.updateInfo()
                },
                it.year,
                it.monthValue - 1,
                it.dayOfMonth
            )
            dialog.show()
        })

        viewModel.genderEdit.observe(viewLifecycleOwner, EventObserver {
            setFragmentResultListener("gender") { _, bundle ->
                val gender = bundle.getInt("gender")
                viewModel.setGender(gender)
                viewModel.updateInfo()
            }

            findNavController().navigate(
                NavMainDirections.actionGlobalGenderSelectFragment()
            )
        })

        viewModel.alert.observe(viewLifecycleOwner, EventObserver {
            alertFactory.openConfirmAlert(childFragmentManager, it)
        })

        viewModel.cellphone.observe(viewLifecycleOwner, EventObserver {
            resultLauncher.launch(Unit)
        })

        viewModel.detail()
    }
}