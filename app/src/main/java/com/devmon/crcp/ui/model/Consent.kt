package com.devmon.crcp.ui.model

import android.os.Parcelable
import com.devmon.crcp.data.network.response.Check
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Consent(
    val said: Int,
    val consentId: Int,
    val displayName: String,
    val fileUrl: String,
    val csClose: Int,
    val csStage: Int,
    val csCloseMsg: String,
    val checkContent: @RawValue List<Check>,
) : Parcelable