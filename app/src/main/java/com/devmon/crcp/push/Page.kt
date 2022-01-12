package com.devmon.crcp.push

enum class Page(val code: String) {
    NONE("0"),
    CONSENT("1"),
    QNA("2"),
    ;

    companion object {
        fun of(code: String) = when (code) {
            "1" -> CONSENT
            "2" -> QNA
            else -> NONE
        }
    }
}