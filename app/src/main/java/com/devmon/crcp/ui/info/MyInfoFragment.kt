package com.devmon.crcp.ui.info

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
import com.devmon.crcp.databinding.FragmentMyInfoBinding
import com.devmon.crcp.domain.model.Alert
import com.devmon.crcp.ui.alert.AlertFactory
import com.devmon.crcp.ui.auth.certification.PassResultContract
import com.devmon.crcp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.threeten.bp.LocalDate

@AndroidEntryPoint
class MyInfoFragment : BaseFragment<FragmentMyInfoBinding, MyInfoViewModel>(
    layoutId = R.layout.fragment_my_info
) {

    override val viewModel: MyInfoViewModel by viewModels({ requireParentFragment() })

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

        viewModel.password.observe(viewLifecycleOwner, EventObserver {
            setFragmentResultListener("password") { _, _ ->
                viewModel.updateInfo()
            }

            findNavController().navigate(
                NavMainDirections.actionGlobalPasswordFindDialogFragment()
            )
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

        viewModel.pass.observe(viewLifecycleOwner, EventObserver {
            resultLauncher.launch(Unit)
        })

        viewModel.alert.observe(viewLifecycleOwner, EventObserver {
            alertFactory.openConfirmAlert(childFragmentManager, it)
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

        viewModel.cellphone.observe(viewLifecycleOwner, EventObserver {
            resultLauncher.launch(Unit)
        })
    }
}