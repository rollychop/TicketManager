package com.bnc.ticketmanager.ui.screen.add_edit_ticket

import androidx.compose.runtime.Stable
import com.bnc.ticketmanager.domain.model.TicketModel
import com.bnc.ticketmanager.ui.component.DataTextFieldState
import com.bnc.ticketmanager.ui.component.TextFieldState
import java.time.LocalDate

@Stable
class TicketInputState(
    val ticketId: Int = 0,
) {

    companion object {
        val priorities = listOf("High", "Medium", "Low")
    }

    val name = TextFieldState(
        validator = { it.isNotBlank() },
        errorFor = { "Name can't be blank" }
    )
    val description = TextFieldState(
        validator = {
            it.isNotBlank()
        },
        errorFor = {
            "Description can't be blank"
        }
    )
    val priority = TextFieldState(
        validator = {
            it in priorities
        },
        errorFor = {
            "Invalid priority"
        }
    )

    val dueDate = DataTextFieldState<LocalDate>(
        validator = {
            it != null
        },
        errorFor = {
            "Invalid date"
        }
    )


    private fun enableError() {
        name.isFocusedDirty = true
        description.isFocusedDirty = true
        priority.isFocusedDirty = true
        dueDate.isFocusedDirty = true
        name.enableShowErrors()
        description.enableShowErrors()
        priority.enableShowErrors()
        dueDate.enableShowErrors()
    }

    fun isValid(): Boolean {
        enableError()

        if (!name.isValid) {
            name.focusRequester.requestFocus()
            return false
        }
        if (!description.isValid) {
            description.focusRequester.requestFocus()
            return false
        }
        if (!priority.isValid) {
            priority.focusRequester.requestFocus()
            return false
        }
        if (!dueDate.isValid) {
            dueDate.focusRequester.requestFocus()
            return false
        }
        return true
    }

    fun reset() {
        name.reset()
        description.reset()
        priority.reset()
        dueDate.reset()
    }

}

fun TicketInputState.toTicketModel() = TicketModel(
    id = ticketId,
    name = name.text,
    description = description.text,
    priority = priority.text,
    dueDate = dueDate.data ?: LocalDate.now()
)