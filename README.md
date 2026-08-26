# To-Do List

Aplicativo Android de lista de tarefas desenvolvido para a atividade de Sistemas de Informação da FIAP. Permite listar, criar, editar, concluir e excluir tarefas, com persistência local dos dados.

## Tecnologias

- Kotlin
- Jetpack Compose (Material 3)
- Room (SQLite)
- Kotlin Coroutines + Flow
- ViewModel
- Navigation Compose

## Arquitetura (MVVM)

```
app/src/main/java/juliampdutra/com/gitub/to_do_list/
├── data/          # Entity, DAO e configuração do banco (Room)
├── repository/    # Abstrai o acesso a dados para a ViewModel
├── viewmodel/     # Estado observável (StateFlow) e regras de apresentação
├── ui/            # Telas em Jetpack Compose
└── navigation/    # Rotas e navegação entre telas
```

### TarefaRepository

Fica em `repository/TarefaRepositoy.kt`. É a camada intermediária entre a `TarefaViewModel` e o `TarefaDao`: expõe a lista de tarefas como `Flow<List<Tarefa>>` e disponibiliza as operações de inserir, atualizar e deletar, delegando cada uma ao DAO.

### TarefaViewModel

Fica em `viewmodel/TarefaViewModel.kt`. Consome o `TarefaRepository` e transforma o `Flow` em um `StateFlow` (`tarefas`), que a UI observa. Expõe as funções `inserir`, `atualizar` e `deletar`, cada uma disparada em `viewModelScope.launch` para rodar em coroutine. Como não há Hilt/Koin, a instância é criada por uma `Factory` manual (`TarefaViewModel.factory(context)`), que monta o banco, o repository e a própria ViewModel.

### ListaTarefasScreen

Fica em `ui/ListaTarefasScreen.kt`. Observa `viewModel.tarefas` via `collectAsStateWithLifecycle()` e recompõe a tela sempre que a lista muda. Exibe as tarefas em `LazyColumn`; cada item tem um `Checkbox` (marca/desmarca conclusão), um botão de deletar, e um clique que abre a edição. Um `FloatingActionButton` aciona o cadastro de nova tarefa.

### FormularioTarefaScreen

Fica em `ui/FormularioTarefaScreen.kt`. Recebe um `tarefaId`: se for `0`, está em modo cadastro (campos vazios); se for diferente de `0`, busca a tarefa correspondente na lista da ViewModel e preenche os campos com os dados existentes (modo edição). Ao salvar, decide entre `inserir` ou `atualizar` com base nesse mesmo `tarefaId`.

### AppNavigation

Fica em `navigation/AppNavigation.kt`. Usa `NavHost` com duas rotas:
- `"lista"` — tela inicial
- `"formulario/{tarefaId}"` — recebe o ID da tarefa pela própria rota (`0` para nova tarefa, ou o ID real para edição)

Ambas as telas compartilham a mesma instância de `TarefaViewModel`, recebida como parâmetro.

### MainActivity

Cria a `TarefaViewModel` usando sua `Factory` (via `viewModel(factory = ...)`) e inicia o app chamando `AppNavigation(viewModel = viewModel)` dentro do `TodolistTheme`. Não usa mais a tela de exemplo gerada pelo template do Android Studio.

## Como executar

1. Abra a pasta do projeto no Android Studio e aguarde a sincronização do Gradle.
2. Selecione um emulador (API 24+) ou conecte um dispositivo físico.
3. Clique em **Run ▶**.

## Testes

```bash
./gradlew test                  # testes unitários
./gradlew connectedAndroidTest  # testes instrumentados (TarefaDaoTest), requer emulador/dispositivo
```

## Evidências

As evidências de execução (telas de listagem, cadastro, edição, conclusão, exclusão e navegação) estão em [`docs/evidencias`](docs/evidencias).
