package com.devmon.crcp.ui.auth.find

import android.app.Service
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseDialogFragment
import com.devmon.crcp.databinding.FragmentPasswordFindDialogBinding
import com.devmon.crcp.utils.EventObserver
import com.devmon.crcp.utils.ViewExt.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordFindDialogFragment : BaseDialogFragment<FragmentPasswordFindDialogBinding>(
    layoutId = R.layout.fragment_password_find_dialog
) {
    private var deviceWidth: Int = 0

    private val viewModel by viewModels<PasswordFindViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        viewModel.toast.observe(viewLifecycleOwner, EventObserver { msg ->
            toast(msg)
        })

        viewModel.finish.observe(viewLifecycleOwner, EventObserver {
            dismiss()
        })

        deviceWidth = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            (requireActivity().getSystemService(Service.WINDOW_SERVICE) as WindowManager).currentWindowMetrics.bounds.width()
        } else {
            val point = Point()
            (requireActivity().getSystemService(Service.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(
                point
            )
            point.x
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog?.window?.attributes
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params
    }
}