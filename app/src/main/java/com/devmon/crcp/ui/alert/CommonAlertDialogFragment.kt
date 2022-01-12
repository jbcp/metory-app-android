package com.devmon.crcp.ui.alert

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseDialogFragment
import com.devmon.crcp.databinding.DialogFragmentCommonAlertBinding

class CommonAlertDialogFragment :
    BaseDialogFragment<DialogFragmentCommonAlertBinding>(R.layout.dialog_fragment_common_alert) {

    private var title = "알림"
    private var content = ""
    private var showCancel = true
    private var confirmListener: DialogInterface.OnClickListener? = null
    private var cancelListener: DialogInterface.OnCancelListener? = null

    private var confirmText = "확인"
    private var cancelText = "취소"

    private var cancel = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(R.color.transperant)

        dialog?.setCancelable(cancel)

        binding.tvTitle.text = title
        binding.tvContent.text = content
        binding.btnCancel.visibility = if (showCancel) View.VISIBLE else View.GONE

        binding.btnConfirm.text = confirmText
        binding.btnConfirm.setOnClickListener {
            confirmListener?.onClick(dialog, Dialog.BUTTON_POSITIVE)
        }

        binding.btnCancel.text = cancelText
        binding.btnCancel.setOnClickListener {
            dismiss()
            cancelListener?.onCancel(dialog)
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog?.window?.attributes ?: return

        val manager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as? WindowManager ?: return
        val display = manager.defaultDisplay
        val size = Point().also {
            display.getSize(it)
        }
        params.width = (size.x * 0.9).toInt()

        dialog?.window?.attributes = params
    }

    fun title(title: String) = apply {
        this.title = title
    }

    fun content(content: String) = apply {
        this.content = content
    }

    fun showCancel(isShow: Boolean) = apply {
        this.showCancel = isShow
    }

    fun confirmText(confirmText: String) = apply {
        this.confirmText = confirmText
    }

    fun cancelText(cancelText: String) = apply {
        this.cancelText = cancelText
    }

    fun cancelable(cancel: Boolean) = apply {
        this.cancel = cancel
    }

    fun confirmListener(listener: DialogInterface.OnClickListener) = apply {
        this.confirmListener = listener
    }

    fun cancelListener(listener: DialogInterface.OnCancelListener) = apply {
        this.cancelListener = listener
    }
}