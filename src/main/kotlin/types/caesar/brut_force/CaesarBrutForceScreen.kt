package types.caesar.brut_force

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object CaesarBrutForceScreen : Screen {
    private fun readResolve(): Any = CaesarBrutForceScreen

    @Preview
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { BrutForceScreenModel() }
        val state by screenModel.state.collectAsState()
        val scaffoldState = rememberScaffoldState()
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.background,
                    navigationIcon = {
                        Icon(
                            Icons.Default.ArrowBack, contentDescription = null,
                            modifier = Modifier.clickable { navigator.canPop.takeIf { it }?.run { navigator.pop() } },
                        )
                    },
                    title = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Caesar Cipher", style = MaterialTheme.typography.h6)
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                OutlinedTextField(
                    value = state.encodedText,
                    onValueChange = screenModel::onTextChange,
                    label = {
                        Text("text")
                    },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Button(
                    onClick = {
                        screenModel.decodeWord()
                    },
                    modifier = Modifier.padding(top = 42.dp),
                    enabled = state.isLoading.not()
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text("Brute Force word")
                    }
                }
                state.possibleDecodedWord.takeIf { it.isNotEmpty() }?.let { word ->
                    Text(word)
                }
            }
        }
    }
}
