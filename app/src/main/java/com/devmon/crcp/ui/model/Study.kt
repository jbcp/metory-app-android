package com.devmon.crcp.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Study(
    val sid: Int,
    val said: Int,
) : Parcelable