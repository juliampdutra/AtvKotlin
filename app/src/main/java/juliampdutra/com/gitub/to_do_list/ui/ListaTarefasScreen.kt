package juliampdutra.com.gitub.to_do_list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import juliampdutra.com.gitub.to_do_list.data.Tarefa
import kotlin.text.isNotBlank

@androidx.compose.runtime.Composable
fun ListaTarefasScreen(
    viewModel: juliampdutra.com.gitub.to_do_list.viewmodel.TarefaViewModel,
    onNovaTarefa: () -> Unit,
    onEditarTarefa: (Int) -> Unit
) {
    val tarefas by viewModel.tarefas.collectAsStateWithLifecycle()

    ListaTarefasContent(
        tarefas = tarefas,
        onNovaTarefa = onNovaTarefa,
        onEditarTarefa = onEditarTarefa,
        onCheckedChange = { tarefa, concluida ->
            viewModel.atualizar(tarefa.copy(concluida = concluida))
        },
        onDeletar = { tarefa -> viewModel.deletar(tarefa) }
    )
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@androidx.compose.runtime.Composable
fun ListaTarefasContent(
    tarefas: List<Tarefa>,
    onNovaTarefa: () -> Unit,
    onEditarTarefa: (Int) -> Unit,
    onCheckedChange: (juliampdutra.com.gitub.to_do_list.data.Tarefa, Boolean) -> Unit,
    onDeletar: (juliampdutra.com.gitub.to_do_list.data.Tarefa) -> Unit
) {
    androidx.compose.material3.Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(title = { androidx.compose.material3.Text("Minhas Tarefas") })
        },
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(onClick = onNovaTarefa) {
                androidx.compose.material3.Icon(
                    androidx.compose.material.icons.Icons.Default.Add,
                    contentDescription = "Nova tarefa"
                )
            }
        }
    ) { padding ->
        if (tarefas.isEmpty()) {
            androidx.compose.foundation.layout.Box(
                modifier = androidx.compose.ui.Modifier.Companion
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Companion.Center
            ) {
                androidx.compose.material3.Text("Nenhuma tarefa cadastrada.")
            }
        } else {
            androidx.compose.foundation.lazy.LazyColumn(
                modifier = androidx.compose.ui.Modifier.Companion
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
            ) {
                items(tarefas, key = { it.id }) { tarefa ->
                    TarefaItem(
                        tarefa = tarefa,
                        onCheckedChange = { concluida -> onCheckedChange(tarefa, concluida) },
                        onEditar = { onEditarTarefa(tarefa.id) },
                        onDeletar = { onDeletar(tarefa) }
                    )
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun TarefaItem(
    tarefa: juliampdutra.com.gitub.to_do_list.data.Tarefa,
    onCheckedChange: (Boolean) -> Unit,
    onEditar: () -> Unit,
    onDeletar: () -> Unit
) {
    androidx.compose.material3.Card(
        modifier = androidx.compose.ui.Modifier.Companion
            .fillMaxWidth()
            .clickable(onClick = onEditar)
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = androidx.compose.ui.Modifier.Companion.padding(
                horizontal = 8.dp,
                vertical = 12.dp
            ),
            verticalAlignment = androidx.compose.ui.Alignment.Companion.CenterVertically
        ) {
            androidx.compose.material3.Checkbox(
                checked = tarefa.concluida,
                onCheckedChange = onCheckedChange
            )
            androidx.compose.foundation.layout.Spacer(
                modifier = androidx.compose.ui.Modifier.Companion.width(
                    8.dp
                )
            )
            androidx.compose.foundation.layout.Column(
                modifier = androidx.compose.ui.Modifier.Companion.weight(
                    1f
                )
            ) {
                androidx.compose.material3.Text(
                    text = tarefa.titulo,
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    textDecoration = if (tarefa.concluida) androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough else androidx.compose.ui.text.style.TextDecoration.Companion.None
                )
                if (tarefa.descricao.isNotBlank()) {
                    androidx.compose.material3.Text(
                        text = tarefa.descricao,
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
                    )
                }
            }
            androidx.compose.material3.IconButton(onClick = onDeletar) {
                androidx.compose.material3.Icon(
                    androidx.compose.material.icons.Icons.Default.Delete,
                    contentDescription = "Deletar tarefa"
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "Lista com tarefas")
@androidx.compose.runtime.Composable
private fun ListaTarefasContentPreview() {
    ListaTarefasContent(
        tarefas = listOf(
            Tarefa(
                id = 1,
                titulo = "Estudar Room",
                descricao = "Revisar anotações e DAO",
                concluida = false
            ),
            Tarefa(
                id = 2,
                titulo = "Enviar atividade",
                descricao = "Upload no portal da FIAP",
                concluida = true
            )
        ),
        onNovaTarefa = {},
        onEditarTarefa = {},
        onCheckedChange = { _, _ -> },
        onDeletar = {}
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "Lista vazia")
@androidx.compose.runtime.Composable
private fun ListaTarefasContentVaziaPreview() {
    ListaTarefasContent(
        tarefas = emptyList(),
        onNovaTarefa = {},
        onEditarTarefa = {},
        onCheckedChange = { _, _ -> },
        onDeletar = {}
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "Item pendente")
@androidx.compose.runtime.Composable
private fun TarefaItemPreview() {
    TarefaItem(
        tarefa = Tarefa(
            id = 1,
            titulo = "Estudar Room",
            descricao = "Revisar anotações e DAO",
            concluida = false
        ),
        onCheckedChange = {},
        onEditar = {},
        onDeletar = {}
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "Item concluído")
@androidx.compose.runtime.Composable
private fun TarefaItemConcluidaPreview() {
    TarefaItem(
        tarefa = Tarefa(
            id = 2,
            titulo = "Enviar atividade",
            descricao = "Upload no portal da FIAP",
            concluida = true
        ),
        onCheckedChange = {},
        onEditar = {},
        onDeletar = {}
    )
}