package com.bnc.ticketmanager.domain.repository

import com.bnc.ticketmanager.common.UiState
import com.bnc.ticketmanager.domain.model.TicketModel
import com.bnc.ticketmanager.domain.model.TicketSortOption
import com.bnc.ticketmanager.domain.model.SortOrder
import kotlinx.coroutines.flow.Flow

interface TicketManagerRepository {

    suspend fun addTicket(ticket: TicketModel)

    suspend fun updateTicket(ticket: TicketModel)

    suspend fun deleteTicket(ticket: TicketModel)

    fun getAllTickets(
        sortOption: TicketSortOption,
        sortOrder: SortOrder,
        filterOption: TicketSortOption?,
        query: String
    ): Flow<UiState<List<TicketModel>>>

    fun getTicketById(ticketId: Int): Flow<TicketModel?>

    fun getTicketsByPriority(priority: String): Flow<List<TicketModel>>

    fun getTicketsSortedByDueDate(): Flow<List<TicketModel>>
}
