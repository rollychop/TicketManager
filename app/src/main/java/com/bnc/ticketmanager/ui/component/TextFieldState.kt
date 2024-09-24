package com.bnc.ticketmanager.ui.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester

open class TextFieldState(
    private val validator: (String) -> Boolean = { true },
    private val errorFor: (String) -> String = { "" }
) {

    val focusRequester = FocusRequester()

    var text: String by mutableStateOf("")

    // was the TextField ever focused
    var isFocusedDirty: Boolean by mutableStateOf(false)
    var isFocused: Boolean by mutableStateOf(false)
    private var displayErrors: Boolean by mutableStateOf(false)

    open val isValid: Boolean
        get() = validator(text)

    fun onFocusChange(focused: Boolean) {
        isFocused = focused
        if (focused) isFocusedDirty = true
    }

    fun enableShowErrors() {
        // only show errors if the text was at least once focused
        if (isFocusedDirty) {
            displayErrors = true
        }
    }

    fun reset() {
        text = ""
        isFocused = false
        isFocusedDirty = false
        displayErrors = false
    }

    fun showErrors() = !isValid && displayErrors

    open fun getError(): String? {
        return if (showErrors()) {
            errorFor(text)
        } else {
            null
        }
    }
}


data class InputDate<T>(val text: String, val data: T)
class DataTextFieldState<T>(
    initialData: InputDate<T>? = null,
    private val validator: (T?) -> Boolean = { true },
    private val errorFor: (T?) -> String = { "" }
) : TextFieldState({ true }, { "" }) {
    init {
        initialData?.text?.let {
            text = it
        }
    }


    var data: T? by mutableStateOf(initialData?.data)
        private set


    override val isValid: Boolean
        get() = validator(data)

    override fun getError(): String? {
        return if (showErrors()) {
            errorFor(data)
        } else {
            null
        }
    }

    fun updateData(data: T, text: String) {
        this.data = data
        this.text = text
    }
}
