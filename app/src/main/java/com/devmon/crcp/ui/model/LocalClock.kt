package com.devmon.crcp.ui.model

import java.util.Locale
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class LocalClock : Clock {

    override fun now(): String {
        return LocalDateTime
            .now(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREA))
    }

    override fun date(): String {
        return LocalDateTime
            .now(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA))
    }
}