package com.bnc.ticketmanager.data.mapper

import com.bnc.ticketmanager.data.data_source.local.entity.TicketEntity
import com.bnc.ticketmanager.domain.model.TicketModel

object TicketMapper {

    fun fromEntity(ticketEntity: TicketEntity): TicketModel {
        return TicketModel(
            id = ticketEntity.id,
            name = ticketEntity.name,
            description = ticketEntity.description,
            priority = ticketEntity.priority,
            dueDate = ticketEntity.dueDate
        )
    }

    fun toEntity(ticketModel: TicketModel): TicketEntity {
        return TicketEntity(
            id = ticketModel.id,
            name = ticketModel.name,
            description = ticketModel.description,
            priority = ticketModel.priority,
            dueDate = ticketModel.dueDate
        )
    }
}
