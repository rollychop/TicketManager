package com.bnc.ticketmanager.domain.model

enum class TicketSortOption(
    val title: String
) {
    Priority("Priority"),
    DueDate("Due Date"),
    Name("Name"),
    Description("Description")
}

