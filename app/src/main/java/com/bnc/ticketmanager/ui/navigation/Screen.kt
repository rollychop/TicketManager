package com.bnc.ticketmanager.ui.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object HomeScreen : Screen()

    @Serializable
    data class AddTicketScreen(val ticketId: Int? = null) : Screen()


}