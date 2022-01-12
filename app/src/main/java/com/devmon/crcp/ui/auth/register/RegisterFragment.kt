package com.devmon.crcp.ui.auth.register

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseFragment
import com.devmon.crcp.databinding.FragmentRegisterBinding
import com.devmon.crcp.ui.alert.AlertFactory
import com.devmon.crcp.ui.auth.TermsActivity
import com.devmon.crcp.ui.auth.certification.PassResultContract
import com.devmon.crcp.utils.EventObserver
import com.devmon.crcp.utils.ViewExt.toast
import com.devmon.crcp.utils.hideKeyboard
import com.devmon.crcp.utils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>(
    layoutId = R.layout.fragment_register
) {

    override val viewModel by viewModels<RegisterViewModel>()

    @Inject
    lateinit var alertFactory: AlertFactory

    private val result = registerForActivityResult(PassResultContract()) {
        if (it != null) {
            viewModel.passResult = it
            viewModel.disablePass()
        }
        Timber.e("registerForActivityResult test : $it")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        viewModel.toast.observe(viewLifecycleOwner, EventObserver { msg ->
            toast(msg)
        })

        viewModel.finish.observe(viewLifecycleOwner, EventObserver {
            hideKeyboard()
            findNavController().navigate(
                RegisterFragmentDirections.actionRegisterToLogin()
            )
        })

        viewModel.passEvent.observe(viewLifecycleOwner, EventObserver {
            result.launch(Unit)
        })

        viewModel.alert.observe(viewLifecycleOwner, EventObserver {
            alertFactory.openConfirmAlert(childFragmentManager, it)
        })

        viewModel.termsEvent.observe(viewLifecycleOwner, EventObserver { terms ->
            activity?.let {
                TermsActivity.startActivity(it, terms)
            }
        })

        binding.tvTermsContent.movementMethod = ScrollingMovementMethod()
        binding.etPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        binding.etName.requestFocus()
        requireContext().showKeyboard()
    }
}