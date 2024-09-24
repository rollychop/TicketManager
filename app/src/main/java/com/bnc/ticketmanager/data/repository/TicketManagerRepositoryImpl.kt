package com.bnc.ticketmanager.data.repository

import com.bnc.ticketmanager.common.UiState
import com.bnc.ticketmanager.data.data_source.local.dao.TicketManagerDao
import com.bnc.ticketmanager.data.mapper.TicketMapper
import com.bnc.ticketmanager.domain.model.SortOrder
import com.bnc.ticketmanager.domain.model.TicketModel
import com.bnc.ticketmanager.domain.model.TicketSortOption
import com.bnc.ticketmanager.domain.repository.TicketManagerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketManagerRepositoryImpl @Inject constructor(
    private val ticketDao: TicketManagerDao
) : TicketManagerRepository {

    override suspend fun addTicket(ticket: TicketModel) {
        ticketDao.insertTicket(TicketMapper.toEntity(ticket))
    }

    override suspend fun updateTicket(ticket: TicketModel) {
        ticketDao.updateTicket(TicketMapper.toEntity(ticket))
    }

    override suspend fun deleteTicket(ticket: TicketModel) {
        ticketDao.deleteTicket(TicketMapper.toEntity(ticket))
    }

    override fun getAllTickets(
        sortOption: TicketSortOption,
        sortOrder: SortOrder,
        filterOption: TicketSortOption?,
        query: String
    ): Flow<UiState<List<TicketModel>>> {
        return ticketDao.getTickets(query, sortOption, filterOption, sortOrder).map { entities ->
            entities.map(TicketMapper::fromEntity)
        }.map { UiState.Success(it) }
    }

    override fun getTicketById(ticketId: Int): Flow<TicketModel?> {
        return ticketDao.getTicketById(ticketId).map { entity ->
            entity?.let(TicketMapper::fromEntity)
        }
    }

    override fun getTicketsByPriority(priority: String): Flow<List<TicketModel>> {
        return ticketDao.getTicketsByPriority(priority).map { entities ->
            entities.map(TicketMapper::fromEntity)
        }
    }

    override fun getTicketsSortedByDueDate(): Flow<List<TicketModel>> {
        return ticketDao.getTicketsSortedByDueDate().map { entities ->
            entities.map(TicketMapper::fromEntity)
        }
    }
}
