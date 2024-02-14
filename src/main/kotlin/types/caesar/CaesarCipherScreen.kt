package types.caesar

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.flow.collectLatest

class CaesarCipherScreen : Screen {

    @Preview
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { CaesarCipherScreenModel() }
        val state by screenModel.state.collectAsState()
        val scaffoldState = rememberScaffoldState()

        Scaffold(
            scaffoldState = scaffoldState,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.75f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = state.text,
                        onValueChange = screenModel::onTextChange,
                        label = {
                            Text("text")
                        }
                    )
                    OutlinedTextField(
                        value = state.shift,
                        onValueChange = screenModel::onShiftChange,
                        label = {
                            Text("key default 5")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )

                }
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = screenModel::encode
                    ) {
                        Text("Encode")
                    }
                    Button(
                        onClick = screenModel::decode
                    ) {
                        Text("Decode")
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            screenModel.channel.collectLatest { event ->
                when (event) {
                    is CaesarEvent.Error -> scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }
}
