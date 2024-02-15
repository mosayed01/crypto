package types.caesar

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import types.caesar.brut_force.CaesarBrutForceScreen
import java.io.File
import java.io.FileWriter

object CaesarCipherScreen : Screen {
    private fun readResolve(): Any = CaesarCipherScreen

    @Preview
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { CaesarCipherScreenModel() }
        val state by screenModel.state.collectAsState()
        val scaffoldState = rememberScaffoldState()
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()
        var showFilePicker by remember { mutableStateOf(false) }

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.background,
                    title = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Caesar Cipher", style = MaterialTheme.typography.h6)
                        }
                    }
                )
            },
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = state.text,
                        onValueChange = screenModel::onTextChange,
                        label = {
                            Text("text")
                        },
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    OutlinedTextField(
                        value = state.shift,
                        onValueChange = screenModel::onShiftChange,
                        label = {
                            Text("key default 5")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.padding(start = 8.dp)
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
                Button(
                    onClick = {
                        showFilePicker = true
                    }
                ) {
                    Text("Peck from text file")
                }
                Button(
                    onClick = {
                        if (state.text.isNotEmpty()) {
                            FileWriter(File("saved.txt"), true).use {
                                it.append("${state.text} with key: ${state.shift.ifEmpty { "5" }}\n")
                                it.close()
                            }
                        } else {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("text cannot be empty")
                            }
                        }
                    }
                ) {
                    Text("Save")
                }
                Button(
                    onClick = {
                        navigator.push(CaesarBrutForceScreen)
                    },
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    Text("Brute Force Page")
                }
            }
        }

        FilePicker(show = showFilePicker, fileExtensions = listOf("txt")) { platformFile ->
            showFilePicker = false
            (platformFile?.platformFile as File?)?.readText()?.let {
                screenModel.onTextChange(it)
            }
        }

        DisposableEffect(Unit) {
            val job = coroutineScope.launch {
                screenModel.channel.collectLatest { event ->
                    when (event) {
                        is CaesarEvent.Error -> scaffoldState.snackbarHostState.showSnackbar(event.message)
                    }
                }
            }
            onDispose {
                job.cancel()
            }
        }
    }
}
