package com.devmon.crcp.ui.auth.certification

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PassResult(
    @SerializedName("pass_customer_code")
    val passCustomerCode: String,
    @SerializedName("pass_tx_seq_no")
    val passTxSeqNo: String,
    @SerializedName("pass_result_code")
    val passResultCode: String,
    @SerializedName("pass_result_msg")
    val passResultMsg: String,
    @SerializedName("pass_result_name")
    val passResultName: String,
    @SerializedName("pass_result_birthday")
    val passResultBirthday: String,
    @SerializedName("pass_result_sex_code")
    val passResultSexCode: String,
    @SerializedName("pass_result_local_forigner_code")
    val passResultLocalForignerCode: String,
    @SerializedName("pass_di")
    val passDi: String,
    @SerializedName("pass_ci")
    val passCi: String,
    @SerializedName("pass_ci_update")
    val passCiUpdate: String,
    @SerializedName("pass_tel_com_code")
    val passTelComCode: String,
    @SerializedName("pass_cellphone_no")
    val passCellphoneNo: String,
    @SerializedName("pass_return_msg")
    val passReturnMsg: String,
) : Parcelable