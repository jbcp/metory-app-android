package com.devmon.crcp.ui.screening

import android.app.DatePickerDialog
import android.content.Context
import java.util.Locale
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

fun LocalDate.formattedString(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA))
}

fun showDatePicker(
    context: Context,
    date: String,
    onDateSelect: (date: String) -> Unit,
    onCancel: () -> Unit = {},
) {
    val localDate = date.toLocalDate()

    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            val text = selectedDate.formattedString()
            onDateSelect(text)
        },
        localDate.year,
        localDate.monthValue - 1,
        localDate.dayOfMonth
    ).apply {
        setOnCancelListener { onCancel() }
    }
        .show()
}

fun String.toLocalDate(): LocalDate {
    return try {
        val dateString = this.split("-").map { it.toInt() }
        if (dateString.size == 3) {
            LocalDate.of(dateString[0], dateString[1], dateString[2])
        } else {
            LocalDate.now()
        }
    } catch (e: Exception) {
        Timber.w(e)
        LocalDate.now()
    }
}