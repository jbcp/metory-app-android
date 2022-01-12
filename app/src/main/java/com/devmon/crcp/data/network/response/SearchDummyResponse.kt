package com.devmon.crcp.data.network.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchDummyResponse(
    val hospitalName: String,
    val title: String,
    val ing: Boolean,
    val content: String,
    val spot: String,
    val constraint: String,
    val age: String
) : Parcelable