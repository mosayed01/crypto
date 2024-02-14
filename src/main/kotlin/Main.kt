import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import types.caesar.CaesarCipherScreen

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Crypto") {
        MaterialTheme {
            Navigator(CaesarCipherScreen())
        }
    }
}
