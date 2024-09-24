package com.bnc.ticketmanager.di

import com.bnc.ticketmanager.data.repository.TicketManagerRepositoryImpl
import com.bnc.ticketmanager.domain.repository.TicketManagerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AbstractModule {

    @Binds
    abstract fun bindRepository(impl: TicketManagerRepositoryImpl): TicketManagerRepository

}