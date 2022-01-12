package com.devmon.crcp.ui.progress

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.devmon.crcp.R

class FullScreenProgressBar {
    private var dialog: Dialog? = null

    fun show(
        context: Context,
        title: CharSequence? = null,
        cancelable: Boolean = false,
        cancelListener: DialogInterface.OnCancelListener? = null,
    ) {
        if (dialog?.isShowing == true) return

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.dialog_full_screen_progress_bar, null)

        if (title != null) {
            val tv = view.findViewById(R.id.id_title) as TextView
            tv.text = title
        }

        Dialog(context, R.style.NewDialog)
            .apply {
                setContentView(view)
                setCancelable(cancelable)
                setOnCancelListener(cancelListener)
            }
            .also { dialog = it }
            .run { show () }
    }

    fun dismiss() {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }
}