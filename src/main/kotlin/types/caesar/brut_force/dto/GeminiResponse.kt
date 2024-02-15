package types.caesar.brut_force.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponse(
    @SerialName("candidates")
    val candidates: List<Candidate>?,
    @SerialName("promptFeedback")
    val promptFeedback: PromptFeedback?
)