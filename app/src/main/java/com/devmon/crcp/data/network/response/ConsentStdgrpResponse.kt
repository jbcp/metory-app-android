package com.devmon.crcp.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ConsentStdgrpResponse(
    @SerializedName("CSGRPID")
    val csgrpid: Int, // 5
    @SerializedName("CSGRPTITLE")
    val csgrptitle: String, // Informed Consent

    /**
     * 동의 그룹 타입(ex. 1: Informed consent, 2: ETC)
     * 1 은 관리자 서명까지 받아야 완료
     * 2 는 관리자 서명 X
     */
    @SerializedName("CSGRPTYPE")
    val csgrptype: Int?, // 1
    @SerializedName("CONSENTID")
    val consentid: Int, // 17
    @SerializedName("CVERSION")
    val cversion: String, // v1.3
    @SerializedName("CONTACT_COPTION")
    val contactcoption: Int?, // 0
    @SerializedName("INV_SIGN_COPTION")
    val invsigncoption: Int?, // 1
    @SerializedName("CFILE")
    val cfile: String?, // /upload/1/c_1612364883939.pdf
    @SerializedName("CFILENAME")
    val cfilename: String?, // KINECT_01_별첨2_시험대상자 동의서 및 설명서_v1.0_20170908.pdf
    @SerializedName("CSID")
    val csid: Int?, // 57

    @SerializedName("CHECK")
    val check: List<Check>?, // 0
    /**
     * 동의별 자원자 단계
     * 0 : 서명 전
     * 5 : 지원자 서명 완료
     * 6 : 관리자 서명 완료 (서명 종료)
     */
    @SerializedName("CSSTAGE")
    val csstage: Int?, // 1
    @SerializedName("CSIDENTIFICATION")
    val csidentification: String?, // null

    /**
     * csstage 6 까지 가면 완료(1)
     * 아니면 미완료(0)
     */
    @SerializedName("CSCLOSE")
    val csclose: Int? // 0
)

@Parcelize
data class Check(
    @SerializedName("CDIDNUM")
    val cdidnum: Int?,
    @SerializedName("CONSENTID")
    val consentid: Int?,
    @SerializedName("CDTITLE")
    val cdtitle: String?,
    @SerializedName("CDCONTENT")
    val content: String?,
    var accept: Boolean = false,
) : Parcelable