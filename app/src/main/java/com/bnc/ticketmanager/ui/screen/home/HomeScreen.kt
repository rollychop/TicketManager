package com.bnc.ticketmanager.ui.screen.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bnc.ticketmanager.R
import com.bnc.ticketmanager.common.Constant
import com.bnc.ticketmanager.domain.model.SortOrder
import com.bnc.ticketmanager.domain.model.TicketModel
import com.bnc.ticketmanager.domain.model.TicketSortOption
import com.bnc.ticketmanager.ui.navigation.Navigator
import com.bnc.ticketmanager.ui.navigation.Screen
import com.bnc.ticketmanager.ui.screen.add_edit_ticket.TicketInputState
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    navigator: Navigator,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val tickets by viewModel.tickets.collectAsStateWithLifecycle()

    HomeScreenContent(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        tickets = tickets,
        onAddTicketClick = {
            navigator.navigate(Screen.AddTicketScreen())
        },
        onTicketClick = { ticket ->
            navigator.navigate(Screen.AddTicketScreen(ticket.id))
        },
        onDeleteTicketClick = { ticket ->
            viewModel.deleteTicket(ticket)
        },
        onQueryChange = viewModel::onQueryChanged,
        query = viewModel.query,
        onSortChange = viewModel::updateSortOption,
        onSortOrdersChange = viewModel::updateSortOrder,
        onChangeFilterColumn = viewModel::updateFilterColumn
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun HomeScreenContent(
    state: HomeScreenState,
    tickets: List<TicketModel>,
    query: String,
    onQueryChange: (String) -> Unit,
    onAddTicketClick: () -> Unit,
    onTicketClick: (TicketModel) -> Unit,
    onDeleteTicketClick: (TicketModel) -> Unit,
    onSortChange: (TicketSortOption) -> Unit,
    onSortOrdersChange: (SortOrder) -> Unit,
    onChangeFilterColumn: (TicketSortOption?) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var showSortDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val context = LocalContext.current

    BackHandler(expanded) {
        expanded = false
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTicketClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Ticket"
                )
            }
        },
        topBar = {
            DockedSearchBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = onQueryChange,
                        onSearch = {
                            expanded = false
                        },
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = it
                        },
                        leadingIcon = {
                            if (expanded) {
                                IconButton(onClick = {
                                    expanded = false
                                }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Close search"
                                    )

                                }
                            } else {
                                Icon(Icons.Default.Search, contentDescription = "Search")
                            }
                        },
                        trailingIcon = {
                            Row {
                                if (query.isNotEmpty()) {
                                    IconButton(onClick = {
                                        onQueryChange("")
                                    }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                                    }
                                }
                                IconButton(onClick = {
                                    showSortDialog = true
                                }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.Sort,
                                        contentDescription = "sort"
                                    )
                                }
                            }
                        },
                        placeholder = {
                            Text("Search tickets ...")
                        }
                    )
                },
                expanded = expanded,
                onExpandedChange = {
                    expanded = it
                }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Select column to search",
                        style = MaterialTheme.typography.titleSmall
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        FilterChip(
                            selected = state.filterOption == null,
                            onClick = {
                                onChangeFilterColumn(null)
                            },
                            label = {
                                Text("All")
                            }
                        )
                        TicketSortOption.entries.forEach {
                            FilterChip(
                                selected = state.filterOption == it,
                                onClick = {
                                    onChangeFilterColumn(it)
                                },
                                label = { Text(it.title) }
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    AnimatedContent(state.filterOption, label = "") { option ->
                        when (option) {
                            TicketSortOption.Priority -> {
                                Column {
                                    Text(
                                        text = "Suggestions",
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    ) {
                                        TicketInputState.priorities.forEach {
                                            SuggestionChip(
                                                onClick = {
                                                    onQueryChange(it)
                                                },
                                                label = { Text(it) }
                                            )
                                        }
                                    }
                                }
                            }

                            TicketSortOption.DueDate -> Column {
                                Text(
                                    text = "Enter Date format in ${Constant.LOCAL_DATE_FORMAT} or select date by clicking calendar icon",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            showDatePicker = true
                                        },
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Select Date")
                                    IconButton(onClick = {
                                        showDatePicker = true
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "select date"
                                        )
                                    }
                                }
                            }

                            null -> Text(
                                buildAnnotatedString {
                                    append("Only ")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Nane")
                                    }
                                    append(" and ")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Description")
                                    }
                                    append(" will be searched on ")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("All")
                                    }
                                    append(" option")
                                },
                                style = MaterialTheme.typography.titleSmall
                            )

                            else -> Text(
                                buildAnnotatedString {
                                    append("Search by ")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(option.title)
                                    }
                                    append(" will be performed")
                                },
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (tickets.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.empty_state),
                        contentDescription = "no tickets",
                        modifier = Modifier
                    )
                    Text(
                        text = "No tickets available",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    columns = GridCells.Adaptive(minSize = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item(
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        Text(
                            text = "Tickets",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    items(
                        items = tickets,
                        key = { it.id }
                    ) { ticket ->
                        TicketItem(
                            modifier = Modifier.animateItem(),
                            ticket = ticket,
                            onClick = { onTicketClick(ticket) },
                            onDeleteClick = { onDeleteTicketClick(ticket) }
                        )
                    }
                }
            }
        }
    }

    if (showSortDialog) {
        Dialog(
            onDismissRequest = {
                showSortDialog = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sort Tickets",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        IconButton(onClick = { showSortDialog = false }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Sort By",
                        style = MaterialTheme.typography.titleLarge
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        TicketSortOption.entries.forEach {
                            FilterChip(
                                selected = state.sortOption == it,
                                onClick = {
                                    onSortChange(it)
                                },
                                label = { Text(text = it.title) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Sort Order",
                        style = MaterialTheme.typography.titleLarge
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        SortOrder.entries.forEach {
                            FilterChip(
                                selected = state.sortOrder == it,
                                onClick = {
                                    onSortOrdersChange(it)
                                },
                                label = { Text(text = it.title) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))


                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                Button(onClick = {
                    try {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val ld = Instant.ofEpochMilli(millis)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                            onQueryChange(ld.format(DateTimeFormatter.ofPattern(Constant.LOCAL_DATE_FORMAT)))

                        }
                    } catch (ex: Exception) {
                        Toast.makeText(context, "Failed to select date", Toast.LENGTH_SHORT).show()
                    }
                    showDatePicker = false
                }) {
                    Text(text = "Select")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text(text = "Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
            )
        }
    }

}


@Composable
fun TicketItem(
    ticket: TicketModel,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = ticket.name,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = ticket.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Priority: ${ticket.priority}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = when (ticket.priority.lowercase()) {
                                "high" -> MaterialTheme.colorScheme.error
                                "medium" -> MaterialTheme.colorScheme.secondary
                                else -> MaterialTheme.colorScheme.primary
                            },
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Due: ${ticket.dueDateText}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Ticket",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

