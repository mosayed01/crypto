package types.caesar.brut_force.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SafetyRating(
    @SerialName("category")
    val category: String?,
    @SerialName("probability")
    val probability: String?
)