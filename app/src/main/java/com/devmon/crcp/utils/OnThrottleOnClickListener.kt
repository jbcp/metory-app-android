package com.devmon.crcp.utils

import android.view.View
import timber.log.Timber

class OnThrottleOnClickListener(
    private val interval: Long = 300,
    private val onClickListener: View.OnClickListener?,
) :
    View.OnClickListener {
    private var click = true

    override fun onClick(v: View?) {
        if (click && onClickListener != null) {
            click = false
            v?.apply {
                postDelayed({
                    click = true
                }, interval)
            }
            onClickListener.onClick(v)
        } else {
            Timber.d("waiting for a while")
        }
    }
}
