package types.caesar.brut_force.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Candidate(
    @SerialName("content")
    val content: Content?,
    @SerialName("finishReason")
    val finishReason: String?,
    @SerialName("index")
    val index: Int?,
    @SerialName("safetyRatings")
    val safetyRatings: List<SafetyRating?>?
)