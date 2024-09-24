package com.bnc.ticketmanager.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bnc.ticketmanager.data.data_source.local.dao.TicketManagerDao
import com.bnc.ticketmanager.data.data_source.local.entity.TicketEntity


@Database(
    entities = [TicketEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DateConverter::class
)
abstract class TicketManagerDB : RoomDatabase() {
    abstract val ticketManagerDao: TicketManagerDao
}