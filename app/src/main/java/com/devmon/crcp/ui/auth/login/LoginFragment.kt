package com.devmon.crcp.ui.auth.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devmon.crcp.CRCPApplication
import com.devmon.crcp.NavMainDirections
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseFragment
import com.devmon.crcp.databinding.FragmentLoginBinding
import com.devmon.crcp.ui.MainViewModel
import com.devmon.crcp.ui.MenuStateListener
import com.devmon.crcp.utils.EventObserver
import com.devmon.crcp.utils.ViewExt.toast
import com.devmon.crcp.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(
    layoutId = R.layout.fragment_login
) {

    private val parentViewModel: MainViewModel by activityViewModels()
    override val viewModel: LoginViewModel by viewModels()

    private var menuStateListener: MenuStateListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        menuStateListener = context as? MenuStateListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuStateListener?.hideMenu()
        binding.vm = viewModel

        viewModel.toast.observe(viewLifecycleOwner, EventObserver {
            toast(it)
        })

        viewModel.register.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(
                LoginFragmentDirections.actionToRegisterFragment()
            )
        })

        viewModel.findPassword.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(
                NavMainDirections.actionGlobalPasswordFindDialogFragment()
            )
        })

        viewModel.login.observe(viewLifecycleOwner, EventObserver {
            parentViewModel.setUserInfo(it)

            (activity?.application as? CRCPApplication)?.sendPushToken()

            hideKeyboard()
            toast("로그인이 완료되었습니다.")
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToStudyFragment()
            )
        })
    }
}