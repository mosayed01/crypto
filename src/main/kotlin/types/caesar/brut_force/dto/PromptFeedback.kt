package types.caesar.brut_force.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromptFeedback(
    @SerialName("safetyRatings")
    val safetyRatings: List<SafetyRating>?
)