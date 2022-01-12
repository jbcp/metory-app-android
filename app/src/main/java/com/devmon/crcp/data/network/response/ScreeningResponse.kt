package com.devmon.crcp.data.network.response


import com.google.gson.annotations.SerializedName

data class ScreeningResponse(
    @SerializedName("info")
    val info: InfoResponse?,
    @SerializedName("ref")
    val ref: RefResponse?,
    @SerializedName("devmon_question_list")
    val devmonQuestionList: List<DevmonQuestionResponse>?,
    @SerializedName("screening_set")
    val screeningSet: ScreeningSetResponse?,
) {
    data class InfoResponse(
        @SerializedName("sid")
        val sid: String?, // 9
    )

    data class RefResponse(
        @SerializedName("concept")
        val concept: List<ConceptResponse>?,
        @SerializedName("answer_item")
        val answerItem: List<AnswerItemResponse>?,
        @SerializedName("screening_sub_question")
        val screeningSubQuestion: List<SubQuestionResponse>?,
    )

    data class ConceptResponse(
        @SerializedName("concept_id")
        val conceptId: Int?, // 1006
        @SerializedName("concept_name")
        val conceptName: String?, // 선택  checkbox 
        @SerializedName("concept_class_id")
        val conceptClassId: String?, // 질문응답유형
    )

    data class AnswerItemResponse(
        @SerializedName("answer_item_group_id")
        val answerItemGroupId: Int?, // 1
        @SerializedName("answer_item_seq")
        val answerItemSeq: Int?, // 1
        @SerializedName("answer_item_id")
        val answerItemId: Int?, // 1
        @SerializedName("answer_item_text")
        val answerItemText: String?, // 예
    )

    data class DevmonQuestionResponse(
        @SerializedName("screening_question_id")
        val screeningQuestionId: Int?,
    )

    data class SubQuestionResponse(
        @SerializedName("question_group_id")
        val questionGroupId: Int?,
        @SerializedName("sub_question_content")
        val subQuestionContent: String?,
        @SerializedName("answer_type_concept_id")
        val answerTypeConceptId: Int?,
        @SerializedName("answer_item_group_id")
        val answerItemGroupId: Int?,
        @SerializedName("sub_question_seq")
        val subQuestionSeq: Int?,
    )

    data class ScreeningSetResponse(
        @SerializedName("screening_header_info")
        val screeningHeaderInfo: List<ScreeningHeaderInfoResponse>?,
        @SerializedName("screening_question")
        val screeningQuestion: List<QuestionResponse>?,
    )

    data class ScreeningHeaderInfoResponse(
        @SerializedName("screening_head_id")
        val screeningHeadId: Int?,
        @SerializedName("screening_head_title")
        val screeningHeadTitle: String?,
        @SerializedName("screening_head_value")
        val screeningHeadValue: String?,
    )

    data class QuestionResponse(
        @SerializedName("screening_question_id")
        val screeningQuestionId: Int?, // 1
        @SerializedName("question_id")
        val questionId: Int?, // 15
        @SerializedName("question_group_id")
        val questionGroupId: Int?,
        @SerializedName("question_content")
        val questionContent: String?, // COVID-19 확진날짜를 입력하세요
        @SerializedName("answer_item_group_id")
        val answerItemGroupId: Int?, // null
        @SerializedName("answer_type_concept_id")
        val answerTypeConceptId: Int?, // 1004
        @SerializedName("question_seq")
        val questionSeq: Int?, // 1
        @SerializedName("survey_section_id")
        val surveySectionId: Int?, // 1
        @SerializedName("section_seq")
        val sectionSeq: Int?, // 1
        @SerializedName("survey_section_name")
        val surveySectionName: String?, // 기본정보입력
        @SerializedName("survey_section_info")
        val surveySectionInfo: Any?, // null
    )
}