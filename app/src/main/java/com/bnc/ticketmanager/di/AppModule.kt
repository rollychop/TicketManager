package com.bnc.ticketmanager.di

import android.content.Context
import androidx.room.Room
import com.bnc.ticketmanager.data.data_source.local.TicketManagerDB
import com.bnc.ticketmanager.data.data_source.local.dao.TicketManagerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesAppDb(@ApplicationContext appContext: Context): TicketManagerDB {
        return Room.databaseBuilder(
            context = appContext,
            klass = TicketManagerDB::class.java,
            name = "TicketManager.db"
        ).fallbackToDestructiveMigrationFrom().build()
    }

    @Singleton
    @Provides
    fun providesTicketDao(db: TicketManagerDB): TicketManagerDao = db.ticketManagerDao


}