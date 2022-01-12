package com.devmon.crcp.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recruiting(
    val sid: Int?,
    val title: String?,
    val sappl: String?,
    val ssex: Int?,
    val snum: Int,
    val starget: String,
    val sdate: String?,
    val emergency: Int?,
    val sitename: String?,
    val sactive: Boolean,
    val imageUrl: String?,
    val favorite: Boolean = false,
) : Parcelable