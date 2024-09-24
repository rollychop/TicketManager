package com.bnc.ticketmanager.util

import com.bnc.ticketmanager.common.Constant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern(Constant.LOCAL_DATE_FORMAT)
    return formatter.format(this)
}