package types.caesar

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CaesarCipherScreenModel : StateScreenModel<CaesarState>(CaesarState()) {
    private val _channel = Channel<CaesarEvent>()
    val channel = _channel.consumeAsFlow()

    fun onTextChange(text: String) {
        mutableState.update { it.copy(text = text) }
    }

    fun onShiftChange(shift: String) {
        if (shift.toIntOrNull() != null || shift.isEmpty()) {
            mutableState.update { it.copy(shift = shift) }
        }
    }

    fun encode() {
        if (state.value.text.isEmpty()) {
            screenModelScope.launch {
                _channel.send(CaesarEvent.Error("please enter text"))
            }
            return
        }
        mutableState.update { it.copy(text = encrypt(it.text, it.shift.toIntOrNull() ?: 5)) }
    }

    fun decode() {
        if (state.value.text.isEmpty()) {
            screenModelScope.launch {
                _channel.send(CaesarEvent.Error("please enter text"))
            }
            return
        }
        mutableState.update { it.copy(text = decrypt(it.text, it.shift.toIntOrNull() ?: 5)) }
    }

    private fun encrypt(text: String, shift: Int): String {
        val result = StringBuilder()

        for (char in text) {
            if (char.isLetter()) {
                val base = if (char.isUpperCase()) 'A' else 'a'
                val shiftedChar = base + ((char - base + shift) % 26 + 26) % 26
                result.append(shiftedChar)
            } else {
                result.append(char)
            }
        }

        return result.toString()
    }

    private fun decrypt(text: String, shift: Int): String {
        val result = StringBuilder()

        for (char in text) {
            if (char.isLetter()) {
                val base = if (char.isUpperCase()) 'A' else 'a'
                val shiftedChar = base + ((char - base - shift) % 26 + 26) % 26
                result.append(shiftedChar)
            } else {
                result.append(char)
            }
        }

        return result.toString()
    }
}

data class CaesarState(
    val text: String = "",
    val shift: String = "",
)

sealed interface CaesarEvent {
    data class Error(val message: String) : CaesarEvent
}