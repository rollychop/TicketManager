package com.bnc.ticketmanager.ui.component


import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.bnc.ticketmanager.common.Constant
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInputField(
    state: DataTextFieldState<LocalDate>,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    enabled: Boolean = true
) {
    var showDatePickerDialog by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val context = LocalContext.current
    val fm = LocalFocusManager.current

    MyTextField(
        state = state,
        label = label,
        modifier = modifier
            .onFocusChanged {
                if (it.isFocused) {
                    showDatePickerDialog = true
                }
            },
        enabled = enabled,
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = ""
            )
        }
    )

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePickerDialog = false
                fm.clearFocus()
            },
            confirmButton = {
                Button(onClick = {
                    try {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val ld = Instant.ofEpochMilli(millis)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                            state.updateData(
                                data = ld,
                                text = ld.format(DateTimeFormatter.ofPattern(Constant.LOCAL_DATE_FORMAT))
                            )

                        }
                    } catch (ex: Exception) {
                        Toast.makeText(context, "Failed to select date", Toast.LENGTH_SHORT).show()
                    }
                    fm.clearFocus()
                    showDatePickerDialog = false
                }) {
                    Text(text = "Select")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePickerDialog = false
                    fm.clearFocus()
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