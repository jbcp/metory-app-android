package com.devmon.crcp.push

data class Message(
    val title: String,
    val body: String,
    val page: Page = Page.NONE,
)