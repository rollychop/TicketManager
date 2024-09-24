package com.bnc.ticketmanager.domain.model

import com.bnc.ticketmanager.util.toFormattedString
import java.time.LocalDate

data class TicketModel(
    val id: Int,
    val name: String,
    val description: String,
    val priority: String,
    val dueDate: LocalDate?
) {
    val dueDateText: String = dueDate?.toFormattedString() ?: ""
}
