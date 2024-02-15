package types.caesar.brut_force.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Part(
    @SerialName("text")
    val text: String?
)