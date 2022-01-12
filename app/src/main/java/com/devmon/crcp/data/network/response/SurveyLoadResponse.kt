package com.devmon.crcp.data.network.response
import com.google.gson.annotations.SerializedName

data class SurveyLoadResponse(
    @SerializedName("screening_answer")
    val screeningAnswer: List<ScreeningAnswerResponse>?,
    @SerializedName("group_answer")
    val groupAnswer: List<GroupAnswerResponse>?
) {
    data class ScreeningAnswerResponse(
        @SerializedName("SCREENING_ANSWER_ID")
        val screeningAnswerId: Int?, // 133
        @SerializedName("SAID")
        val said: Int?, // 17
        @SerializedName("SCREENING_ID")
        val screeningId: Int?, // 1
        @SerializedName("SCREENING_QUESTION_ID")
        val screeningQuestionId: Int?, // 1
        @SerializedName("ANSWER_SOURCE_VALUE")
        val answerSourceValue: String?, // 2021-02-22
        @SerializedName("ANSWERDTC")
        val answerDtc: String?, // 2021-02-22 22:45:41
        @SerializedName("DATA_LOCKING")
        val dataLocking: Int? // 0
    )

    data class GroupAnswerResponse(
        @SerializedName("GROUP_ANSWER_ID")
        val groupAnswerId: Int?, // 41
        @SerializedName("SAID")
        val said: Int?, // 17
        @SerializedName("SCREENING_ID")
        val screeningId: Int?, // 1
        @SerializedName("QUESTION_GROUP_ID")
        val questionGroupId: Int?, // 1
        @SerializedName("SUB_QUESTION_ID")
        val subQuestionId: Int?, // 1
        @SerializedName("ANSWER_SOURCE_VALUE")
        val answerSourceValue: String?, // androidx.appcompat.widget.AppCompatEditText{98c6e04 VFED..CL. ........ 576,211-1384,386 #7f0a00da app:id/et_drug_name aid=1073741941}
        @SerializedName("ANSWERDTC")
        val answerDtc: String?, // 2021-02-22 22:45:41
        @SerializedName("DATA_LOCKING")
        val dataLocking: Int? // 0
    )
}