package juliampdutra.com.gitub.to_do_list.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import juliampdutra.com.gitub.to_do_list.data.Tarefa
import kotlin.collections.find
import kotlin.let
import kotlin.text.isNotBlank
import kotlin.text.trim


@androidx.compose.runtime.Composable
fun FormularioTarefaScreen(
    viewModel: juliampdutra.com.gitub.to_do_list.viewmodel.TarefaViewModel,
    tarefaId: Int,
    onVoltar: () -> Unit
) {
    val tarefas by viewModel.tarefas.collectAsStateWithLifecycle()
    val tarefaExistente = androidx.compose.runtime.remember(tarefas, tarefaId) {
        tarefas.find { it.id == tarefaId }
    }

    FormularioTarefaContent(
        isEdicao = tarefaId != 0,
        tituloInicial = tarefaExistente?.titulo ?: "",
        descricaoInicial = tarefaExistente?.descricao ?: "",
        onSalvar = { titulo, descricao ->
            if (tarefaId == 0) {
                viewModel.inserir(
                    Tarefa(
                        titulo = titulo,
                        descricao = descricao
                    )
                )
            } else {
                tarefaExistente?.let {
                    viewModel.atualizar(it.copy(titulo = titulo, descricao = descricao))
                }
            }
            onVoltar()
        },
        onVoltar = onVoltar
    )
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@androidx.compose.runtime.Composable
fun FormularioTarefaContent(
    isEdicao: Boolean,
    tituloInicial: String,
    descricaoInicial: String,
    onSalvar: (titulo: String, descricao: String) -> Unit,
    onVoltar: () -> Unit
) {
    var titulo by androidx.compose.runtime.remember(tituloInicial) {
        androidx.compose.runtime.mutableStateOf(
            tituloInicial
        )
    }
    var descricao by androidx.compose.runtime.remember(descricaoInicial) {
        androidx.compose.runtime.mutableStateOf(
            descricaoInicial
        )
    }

    androidx.compose.material3.Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { androidx.compose.material3.Text(if (isEdicao) "Editar Tarefa" else "Nova Tarefa") },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = onVoltar) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        androidx.compose.foundation.layout.Column(
            modifier = androidx.compose.ui.Modifier.Companion
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            androidx.compose.material3.OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { androidx.compose.material3.Text("Título") },
                modifier = androidx.compose.ui.Modifier.Companion.fillMaxWidth(),
                singleLine = true
            )
            androidx.compose.material3.OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { androidx.compose.material3.Text("Descrição") },
                modifier = androidx.compose.ui.Modifier.Companion.fillMaxWidth(),
                minLines = 3
            )
            androidx.compose.material3.Button(
                onClick = { onSalvar(titulo.trim(), descricao.trim()) },
                modifier = androidx.compose.ui.Modifier.Companion.fillMaxWidth(),
                enabled = titulo.isNotBlank()
            ) {
                androidx.compose.material3.Text("Salvar")
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "Nova tarefa")
@androidx.compose.runtime.Composable
private fun FormularioTarefaContentNovaPreview() {
    FormularioTarefaContent(
        isEdicao = false,
        tituloInicial = "",
        descricaoInicial = "",
        onSalvar = { _, _ -> },
        onVoltar = {}
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "Editar tarefa")
@androidx.compose.runtime.Composable
private fun FormularioTarefaContentEditarPreview() {
    FormularioTarefaContent(
        isEdicao = true,
        tituloInicial = "Estudar Room",
        descricaoInicial = "Revisar anotações e DAO",
        onSalvar = { _, _ -> },
        onVoltar = {}
    )
}