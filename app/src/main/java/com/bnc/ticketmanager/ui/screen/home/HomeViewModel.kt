package com.bnc.ticketmanager.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnc.ticketmanager.domain.model.SortOrder
import com.bnc.ticketmanager.domain.model.TicketModel
import com.bnc.ticketmanager.domain.model.TicketSortOption
import com.bnc.ticketmanager.domain.repository.TicketManagerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class HomeScreenState(
    val isLoading: Boolean = false,
    val sortOrder: SortOrder = SortOrder.Ascending,
    val sortOption: TicketSortOption = TicketSortOption.Priority,
    val filterOption: TicketSortOption? = null
)


@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TicketManagerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state


    private val queryFlow = snapshotFlow { query }
        .debounce(300)
    val tickets: StateFlow<List<TicketModel>> = combine(
        flow = state,
        flow2 = queryFlow
    ) { state, query -> state to query }
        .flatMapLatest { (state, query) ->
            repository.getAllTickets(state.sortOption, state.sortOrder, state.filterOption, query)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var query by mutableStateOf("")
        private set

    fun onQueryChanged(query: String) {
        this.query = query
    }

    fun updateSortOrder(sortOrder: SortOrder) {
        _state.update { s -> s.copy(sortOrder = sortOrder) }
    }


    fun updateFilterColumn(filterOption: TicketSortOption?) {
        _state.update { s -> s.copy(filterOption = filterOption) }
    }

    fun updateSortOption(sortOption: TicketSortOption) {
        _state.update { s -> s.copy(sortOption = sortOption) }
    }


    fun deleteTicket(ticket: TicketModel) {
        viewModelScope.launch {
            repository.deleteTicket(ticket)
        }
    }
}
