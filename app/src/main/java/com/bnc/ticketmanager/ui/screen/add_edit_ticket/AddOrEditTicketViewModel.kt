package com.bnc.ticketmanager.ui.screen.add_edit_ticket

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.bnc.ticketmanager.domain.repository.TicketManagerRepository
import com.bnc.ticketmanager.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddOrEditTicketViewModel @Inject constructor(
    private val repository: TicketManagerRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _state = MutableStateFlow(AddOrEditScreenState())
    val state = _state.asStateFlow()

    private val ticketID = savedStateHandle.toRoute<Screen.AddTicketScreen>().ticketId ?: 0
    val ticketInputState = TicketInputState(ticketID)

    init {
        loadTicket(ticketID)
    }

    private fun loadTicket(ticketID: Int) {
        _state.update { AddOrEditScreenState(loading = true) }
        viewModelScope.launch {
            if (ticketID != 0) {
                val ticket = repository.getTicketById(ticketID).firstOrNull()
                if (ticket != null) {
                    ticketInputState.name.text = ticket.name
                    ticketInputState.description.text = ticket.description
                    ticketInputState.priority.text = ticket.priority
                    ticket.dueDate?.let {
                        ticketInputState.dueDate.updateData(
                            ticket.dueDate,
                            ticket.dueDateText
                        )
                    }
                }
            }
            _state.update { AddOrEditScreenState() }
        }
    }


    fun saveTicket(onSaved: () -> Unit) {
        if (!ticketInputState.isValid()) return
        val ticket = ticketInputState.toTicketModel()
        viewModelScope.launch {
            if (ticketID == 0) {
                repository.addTicket(ticket)
            } else {
                repository.updateTicket(ticket)
            }
            ticketInputState.reset()
            onSaved()
        }
    }
}
