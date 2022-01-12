package com.devmon.crcp.utils

object PhoneNumberFormatter {
    fun translatePhoneHyphen(phoneNumber: String): String {
        val phone = StringBuilder()
        val length = phoneNumber.length

        for (i in 0 until length) {
            if (i == 3 || i == (length - 5))
                phone.append("-")
            else
                phone.append(phoneNumber[i])
        }

        return phone.toString()
    }
}