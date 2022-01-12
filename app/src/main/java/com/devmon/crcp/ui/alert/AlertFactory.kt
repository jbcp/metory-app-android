package com.devmon.crcp.ui.alert

import androidx.fragment.app.FragmentManager
import com.devmon.crcp.domain.model.Alert

class AlertFactory {

    /**
     * 기본 알림 확인 알러트
     */
    fun openConfirmAlert(fragmentManager: FragmentManager, alert: Alert) {
        CommonAlertDialogFragment()
            .title(alert.title)
            .content(alert.content)
            .showCancel(false)
            .confirmListener { dialog, _ -> dialog.dismiss() }
            .show(fragmentManager, CommonAlertDialogFragment::class.java.simpleName)
    }

    fun openSelfScreeningAlert(
        fragmentManager: FragmentManager,
        msg: String,
        onConfirm: () -> Unit,
        onCancel: () -> Unit,
    ) {
        CommonAlertDialogFragment()
            .title("알림")
            .content(msg)
            .cancelable(false)
            .showCancel(true)
            .confirmText("예")
            .cancelText("아니오")
            .confirmListener { dialog, _ ->
                dialog.dismiss()
                onConfirm()
            }
            .cancelListener { dialog ->
                dialog.dismiss()
                onCancel()
            }
            .show(fragmentManager, CommonAlertDialogFragment::class.java.simpleName)
    }
}