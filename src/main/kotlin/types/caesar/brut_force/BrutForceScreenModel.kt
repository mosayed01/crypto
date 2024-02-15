package types.caesar.brut_force

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import types.caesar.brut_force.dto.GeminiResponse
import types.caesar.decrypt

class BrutForceScreenModel : StateScreenModel<BrutForceState>(BrutForceState()) {
    private var client: HttpClient? = null

    init {
        client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    fun decodeWord() {
        mutableState.update { it.copy(isLoading = true) }
        screenModelScope.launch {
            val encodedWord = state.value.encodedText
            for (key in 0..<26) {
                val deferred = screenModelScope.async(Dispatchers.IO) {
                    val word = decrypt(encodedWord, key)
                    Pair(doesWordHasMeaning(word), word)
                }
                val (hasMeaning, word) = deferred.await()
                if (hasMeaning) {
                    addWord(word)
                    break
                }
            }
        }
    }

    private fun addWord(word: String) {
        mutableState.update { it.copy(possibleDecodedWord = word, isLoading = false) }
    }

    fun onTextChange(text: String) {
        mutableState.update { it.copy(encodedText = text) }
    }

    @OptIn(InternalAPI::class)
    private suspend fun doesWordHasMeaning(word: String): Boolean {
        val result = screenModelScope.async(Dispatchers.IO) {
            val response = client!!.post(
                urlString = URL,
                block = {
                    body = """
                        {
                            "contents": [
                                {
                                    "parts": [
                                        {
                                            "text": "just answer true or false if the word (${word}) has meaning focus in letters only"
                                        }
                                    ]
                                }
                            ]
                        }
                    """.trimIndent()
                }
            )
            if (response.status == HttpStatusCode.OK) {
                val geminiResponse: GeminiResponse = response.body<GeminiResponse>()
                val hasMeaning = geminiResponse.candidates?.firstOrNull()
                    ?.content?.parts?.firstOrNull()?.text?.contains("true", ignoreCase = true)
                return@async hasMeaning == true
            }
            false
        }
        return result.await()
    }


    override fun onDispose() {
        client = null
        super.onDispose()
    }

    companion object {
        private const val URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=AIzaSyBF3VpRSc4qlSlU5JrxATY6JC1A8T3ZA9s"
    }
}

data class BrutForceState(
    val encodedText: String = "",
    val key: Int = 0,
    val possibleDecodedWord: String = "",
    val isLoading: Boolean = false
)