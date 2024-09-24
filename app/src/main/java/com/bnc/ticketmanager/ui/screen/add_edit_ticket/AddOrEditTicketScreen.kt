package com.bnc.ticketmanager.ui.screen.add_edit_ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bnc.ticketmanager.ui.component.DateInputField
import com.bnc.ticketmanager.ui.component.MyTextField
import com.bnc.ticketmanager.ui.component.OptionsTextField
import com.bnc.ticketmanager.ui.navigation.Navigator

@Composable
fun AddOrEditTicketScreen(
    navigator: Navigator,
    viewModel: AddOrEditTicketViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    AddTicketScreenContent(
        ticket = viewModel.ticketInputState,
        isLoading = state.loading,
        onSaveTicketClick = {
            viewModel.saveTicket(onSaved = { navigator.navUp() })
        },
        onCancelClick = { navigator.navUp() }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTicketScreenContent(
    ticket: TicketInputState,
    isLoading: Boolean,
    onSaveTicketClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (ticket.ticketId == 0) "Add Ticket" else "Edit Ticket")
                },
                navigationIcon = {
                    IconButton(onClick = onCancelClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                        .imePadding()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    MyTextField(
                        state = ticket.name,
                        label = { Text("Ticket Name") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    MyTextField(
                        state = ticket.description,
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        minLines = 3,
                        maxLines = 4,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OptionsTextField(
                        state = ticket.priority,
                        options = TicketInputState.priorities,
                        label = { Text("Priority") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Due Date
                    DateInputField(
                        state = ticket.dueDate,
                        label = {
                            Text("Select Due Date")
                        },
                        modifier = Modifier.fillMaxWidth()

                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val focusManager = LocalFocusManager.current
                        OutlinedButton(onClick = onCancelClick) {
                            Text(text = "Cancel")
                        }
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                focusManager.clearFocus()
                                onSaveTicketClick()
                            }) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
    }
}


