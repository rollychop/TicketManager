package com.bnc.ticketmanager.data.data_source.local.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.bnc.ticketmanager.common.Constant
import com.bnc.ticketmanager.data.data_source.local.DateConverter
import com.bnc.ticketmanager.data.data_source.local.entity.TicketEntity
import com.bnc.ticketmanager.domain.model.SortOrder
import com.bnc.ticketmanager.domain.model.TicketSortOption
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Dao
interface TicketManagerDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: TicketEntity)


    @Update
    suspend fun updateTicket(ticket: TicketEntity)


    @Delete
    suspend fun deleteTicket(ticket: TicketEntity)

    @RawQuery(observedEntities = [TicketEntity::class])
    fun getTickets(query: SupportSQLiteQuery): Flow<List<TicketEntity>>

    fun getTickets(
        query: String,
        sortOption: TicketSortOption,
        filterOption: TicketSortOption?,
        order: SortOrder
    ): Flow<List<TicketEntity>> {

        val trimmedQuery = query.trim()
        val sortOrder = if (order == SortOrder.Descending) "DESC" else "ASC"
        val baseQuery = "SELECT * FROM tickets"
        val filterQuery = filterOption?.let {
            if (it == TicketSortOption.DueDate) {
                val parsedDate = parseDate(trimmedQuery)
                if (parsedDate != null) {
                    " WHERE dueDate = '$parsedDate'"
                } else {
                    ""
                }
            } else {
                " WHERE ${toColumnName(it)} LIKE '%$trimmedQuery%'"
            }
        } ?: " WHERE name LIKE '%$trimmedQuery%' OR description LIKE '%$trimmedQuery%'"

        val sortQuery = " ORDER BY ${toColumnName(sortOption)} $sortOrder"
        val fullQuery = baseQuery + filterQuery + sortQuery
        val simpleQuery = SimpleSQLiteQuery(fullQuery)
        return getTickets(simpleQuery)
    }

    private fun toColumnName(sortOption: TicketSortOption) = when (sortOption) {
        TicketSortOption.Priority -> "priority"
        TicketSortOption.DueDate -> "dueDate"
        TicketSortOption.Name -> "name"
        TicketSortOption.Description -> "description"
    }

    private fun parseDate(dateString: String): String? {
        return try {
            val formatter = DateTimeFormatter.ofPattern(Constant.LOCAL_DATE_FORMAT)
            val localDate = LocalDate.parse(dateString, formatter)
            DateConverter.dateToString(localDate)
        } catch (e: DateTimeParseException) {
            null
        }
    }


    @Query("SELECT * FROM tickets WHERE id = :ticketId LIMIT 1")
    fun getTicketById(ticketId: Int): Flow<TicketEntity?>


    @Query("SELECT * FROM tickets WHERE priority = :priority")
    fun getTicketsByPriority(priority: String): Flow<List<TicketEntity>>

    @Query("SELECT * FROM tickets ORDER BY dueDate ASC")
    fun getTicketsSortedByDueDate(): Flow<List<TicketEntity>>
}
