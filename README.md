# Clube de Leitura 

> Projeto desenvolvido para a disciplina de **Programação Orientada a Objetos (POO)**.  
> Uma plataforma desenvolvida em Java para engajar leitores através de clubes, desafios, registro de progresso, streaks e missões gamificadas.

---

## Índice

**Sobre o projeto**
* [Sobre o Projeto](#sobre-o-projeto)
* [Funcionalidades do MVP](#funcionalidades-do-mvp)
* [Arquitetura & Conceitos de POO](#arquitetura-conceitos-de-poo)
* [Visão de Futuro](#visao-de-futuro)
* [Como Executar](#como-executar)
* [Desenvolvedoras](#desenvolvedoras)

**Relatório do Projeto (Anexo I)**
* [1. Capa e Identificação](#1-capa-e-identificacao)
* [2. Introdução](#2-introducao)
* [3. Modelagem do Problema](#3-modelagem-do-problema)
* [4. Ferramentas Utilizadas](#4-ferramentas-utilizadas)
* [5. Resultados e Considerações Finais](#5-resultados-e-consideracoes-finais)

---

<a id="sobre-o-projeto"></a>
## 🎯 Sobre o Projeto

O **Clube de Leitura* é uma aplicação focada em transformar a leitura em uma experiência coletiva e interativa. Os usuários podem criar ou entrar em grupos de leitura, registrar o número de páginas lidas diariamente, interagir com capítulos via comentários protegidos contra spoilers e subir no ranking do grupo através de metas e hábitos contínuos (streaks).

---

<a id="funcionalidades-do-mvp"></a>
## 🛠️ Funcionalidades do MVP

Já implementado no núcleo do backend:

* **Grupos e Desafios:** Suporte a diferentes modalidades de grupos de leitura (Clubes contínuos e Desafios com prazo).
* **Gestão de Leituras:** Cadastro de membros, livros e capítulos.
* **Registro Diário e Streaks:** Apontamento diário de páginas lidas e acompanhamento de dias consecutivos de leitura.
* **Ranking Gamificado:** Classificação dos membros com base no engajamento e regras de cada grupo.
* **Avaliação do Livro:** Nota final do livro.

Ainda a implementar nesta entrega do MVP:

* **Sistema Anti-Spoiler:** Ocultação automática de comentários de capítulos à frente do progresso atual do leitor.
* **Encontros e Missões:** Agendamento de encontros de discussão e sistema de missões por metas concluídas.
* **Avaliação do Livro (completa):** Momentos favoritos e indicação de personagens (favorito, odiado e identificação).

---

<a id="arquitetura-conceitos-de-poo"></a>
## 🏗️ Arquitetura & Conceitos de POO

A aplicação utiliza os princípios fundamentais da Orientação a Objetos para garantir a manutenibilidade e escalabilidade do código:

* **Abstração e Herança:** A classe base `Grupo` é estendida por `Clube` e `Desafio`, reaproveitando estruturas comuns e especializando comportamentos.
* **Polimorfismo:** O cálculo de ranking e pontuação varia de acordo com o tipo do grupo (`Clube` foca em hábitos contínuos; `Desafio` foca em metas e prazos).
* **Encapsulamento:** Proteção do estado interno das entidades (`Membro`, `Livro`, `Capitulo`, etc.) com atributos privados e métodos de acesso controlados.
* **Tratamento de Exceções Customizadas:** Captura e tratamento de erros de negócio específicos (`CapituloInvalidoException`, `PrazoDesafioExpiradoException`, `NotaInvalidaException`, `CodigoGrupoInvalidoException`).
* **Estrutura de Coleções:** Uso estratégico de `Collections` (`List`, `Set`) para gerenciamento de membros, leituras e cálculo de streaks.

---

<a id="visao-de-futuro"></a>
## 🚀 Visão de Futuro (Próximas Atualizações)

Para versões futuras da plataforma, estão previstos os seguintes incrementos:
* Upload real de fotos como prova visual de leitura diária.
* Feed interativo em tempo real com curtidas e respostas em comentários.
* Integração direta via API com a plataforma do Google Meet.
* Interface gráfica avançada construída em JavaFX ou Swing.

---

<a id="como-executar"></a>
## Como Executar

Pré-requisito: JDK 17+ instalado.

Compilar:
```bash
find src/main/java -name "*.java" | xargs javac -d out
```

Rodar:
```bash
java -cp out clubeleitura.Main
```

O `Main` roda um cenário fixo de demonstração: cria membros, um livro, um clube e um desafio, registra leituras, calcula streak e ranking, e força os quatro erros de negócio (`CapituloInvalidoException`, `PrazoDesafioExpiradoException`, `NotaInvalidaException`, `CodigoGrupoInvalidoException`) para mostrar o tratamento de exceções funcionando.

<a id="desenvolvedoras"></a>
## 👩‍💻 Desenvolvedoras

* [Clarice](https://github.com/usuario-clarice)
* [Luciana](https://github.com/usuario-luciana)
* [Malu Quintela](https://github.com/usuario-malu)
* [Sofia](https://github.com/usuario-sofia)

---

## Relatório do Projeto

<a id="1-capa-e-identificacao"></a>
## 1. Capa e Identificação

**Projeto:** Clube de Leitura
**Disciplina:** Programação Orientada a Objetos (POO)
**Equipe:** Clarice, Luciana, Malu Quintela, Sofia
**Repositório:** https://github.com/lucianahonorio/Clube-do-Livro

<a id="2-introducao"></a>
## 2. Introdução

Clube de leitura é uma dinâmica simples de organizar e difícil de manter. O grupo combina ler um livro em conjunto, mas cada pessoa lê no seu ritmo, as conversas sobre a leitura acontecem espalhadas em grupos de WhatsApp ou presencialmente, e não existe nenhum controle real de quem está lendo com regularidade e quem parou no segundo capítulo. Dois problemas aparecem direto nesse formato. O primeiro é o spoiler: alguém comenta um plot twist do capítulo 8 num grupo onde tem gente ainda no capítulo 2, e a experiência de leitura dessa pessoa é estragada. O segundo é a falta de incentivo para manter o hábito, sem nenhum tipo de acompanhamento de progresso ou reconhecimento de quem está lendo com constância, é fácil o grupo esfriar depois da primeira semana.

O projeto Clube de Leitura ataca esses dois problemas com um sistema orientado a objetos em Java. A ideia central é modelar o clube de leitura como um sistema que sabe, a qualquer momento, até onde cada membro leu, e que usa essa informação de duas formas concretas: filtrar automaticamente o que cada pessoa pode ver nos comentários (ninguém lê spoiler de capítulo que ainda não chegou) e transformar o progresso em pontuação, criando um ranking dentro do grupo.

O sistema também separa dois jeitos diferentes de ler em grupo, porque nem todo clube funciona igual. Existe o `Clube`, pensado para leitura contínua sem prazo definido, onde o que importa é o volume de páginas lidas ao longo do tempo. E existe o `Desafio`, que tem prazo final e meta de páginas, e onde o que conta pro ranking é a constância (quantos dias seguidos a pessoa leu, o streak), já que num desafio com prazo apertado manter o hábito todo dia é mais importante do que ler muito de uma vez só. Ao final da leitura, o sistema também permite registrar uma avaliação do livro, com nota e indicação de personagens favoritos, para fechar o ciclo daquela leitura.

<a id="3-modelagem-do-problema"></a>
## 3. Modelagem do Problema

[INSERIR DIAGRAMA UML AQUI]

O sistema é organizado em torno de nove classes de domínio mais um enum, divididas em dois pacotes: `clubeleitura.modelo`, com as entidades, e `clubeleitura.excecao`, com as exceções de negócio.

**Hierarquia de grupos.** No topo da modelagem está `Grupo`, uma classe abstrata que representa qualquer tipo de grupo de leitura. Ela guarda o que é comum a todo grupo (código, nome, lista de membros e o livro atual) e define um método abstrato, `calcularPontuacao(Membro)`, que cada tipo concreto de grupo é obrigado a implementar do seu próprio jeito. `Clube` e `Desafio` estendem `Grupo` e cada um dá um sentido diferente para essa pontuação: `Clube` soma o total de páginas lidas pelo membro, `Desafio` devolve o streak do membro. O ponto interessante dessa modelagem é que `Grupo` também implementa `getRanking()`, que ordena a lista de membros usando `Comparator` chamando `calcularPontuacao(membro)` para cada um, mas sem saber se está lidando com um `Clube` ou um `Desafio`. Essa é a demonstração de polimorfismo dinâmico do projeto: o mesmo método `getRanking()`, escrito uma única vez na classe base, produz um ranking correto e com critério diferente dependendo do tipo real do objeto em tempo de execução. `Desafio` ainda carrega estado próprio que `Clube` não tem (prazo final e meta de páginas) e um método próprio, `registrarLeitura`, que confere se a data está dentro do prazo antes de aceitar o registro, lançando `PrazoDesafioExpiradoException` quando não está.

**Membro e o registro de progresso.** `Membro` é o leitor. Internamente ele guarda uma lista de `RegistroProgresso`, e é essa lista que sustenta praticamente todo o resto do sistema. Cada `RegistroProgresso` é um objeto imutável (todos os atributos são `final`) que representa "nesse dia, esse membro leu tantas páginas desse capítulo, com esse comentário opcional". A partir dessa lista, `Membro` calcula duas coisas: o streak, contando dias consecutivos de registro (usando um `TreeSet<LocalDate>` para eliminar datas repetidas e ordenar automaticamente, e depois um laço simples que quebra a contagem assim que encontra um buraco na sequência de dias), e o capítulo atual, que é o capítulo do registro mais recente. `Membro.registrarProgresso` tem quatro sobrecargas diferentes (com ou sem comentário, com ou sem verificação de prazo), o que é a demonstração de polimorfismo estático do projeto: o nome do método é o mesmo, mas o compilador escolhe a versão certa de acordo com os argumentos passados, seja um registro simples de um `Clube` ou um registro com verificação de prazo vindo de um `Desafio`.

**Livro e capítulo.** `Livro` guarda título, autor e uma lista de `Capitulo`. Cada `Capitulo` tem número, título e um intervalo de páginas (início e fim), que serve tanto para validar se a quantidade de páginas de um registro faz sentido para aquele capítulo quanto para comparar a posição de dois capítulos entre si, o que é usado na regra de spoiler.

**Comentário e a regra de spoiler.** `Comentario` liga um texto a um capítulo, a um autor (`Membro`) e a um tipo (`TipoComentario`, um enum com `COMUM` e `MOMENTO_FAVORITO`). A decisão de design aqui foi colocar a regra de spoiler dentro da própria classe `Comentario`, no método `isVisivelPara(Membro leitor)`: ele compara o número do capítulo do comentário com o capítulo atual do leitor (obtido direto do `Membro`) e só libera a visualização se o leitor já tiver chegado lá, ou se for o próprio autor do comentário. O método `getTextoParaExibir` usa essa checagem para devolver o texto real ou uma mensagem padrão de "oculto". Deixar essa lógica dentro da entidade, em vez de numa camada de tela, significa que qualquer parte do sistema que precisar exibir um comentário aplica a regra automaticamente, sem repetir código.

**Avaliação.** `Avaliacao` fecha o ciclo de leitura de um livro: nota de 0 a 10 e três campos de texto livre para personagem favorito, odiado e de identificação. A validação da nota acontece dentro do próprio construtor, que lança `NotaInvalidaException` se o valor estiver fora do intervalo, então não existe como criar uma `Avaliacao` com nota inválida em nenhum ponto do sistema.

**Encapsulamento.** Praticamente todos os atributos das classes de domínio são privados, acessados só por getters, e boa parte dos objetos (`RegistroProgresso`, `Capitulo`) são imutáveis depois de criados. `Grupo` reforça isso devolvendo cópias somente-leitura de `getMembros()` e `getRanking()` (via `List.copyOf`), para que quem chama esses métodos não consiga alterar a lista interna do grupo por fora.

**Exceções customizadas.** O sistema usa quatro exceções de negócio, todas checked (estendem `Exception`, forçando quem chama a tratar o erro explicitamente em vez de deixar passar direto): `CodigoGrupoInvalidoException` (código de grupo com menos de 3 caracteres), `CapituloInvalidoException` (capítulo nulo ou quantidade de páginas fora do intervalo do capítulo), `PrazoDesafioExpiradoException` (registro de leitura depois do prazo final de um desafio) e `NotaInvalidaException` (nota de avaliação fora de 0 a 10).

**Coleções.** Além do `List<Membro>` em `Grupo`, do `List<Capitulo>` em `Livro` e do `List<RegistroProgresso>` em `Membro`, o projeto usa `TreeSet` para o cálculo de streak e `Comparator` (tanto para ordenar o `TreeSet` quanto para montar o ranking em `getRanking()`, com desempate por nome usando `Comparator.thenComparing`).

<a id="4-ferramentas-utilizadas"></a>
## 4. Ferramentas Utilizadas

O projeto foi escrito em Java (JDK 17), sem framework de aplicação: não há Spring, nem Maven ou Gradle configurados. A compilação e a execução são feitas direto pela linha de comando, com `javac` compilando todos os arquivos de `src/main/java` e `java` rodando a classe desejada (as instruções completas estão na seção "Como Executar" deste README). Essa escolha manteve o setup simples para os quatro integrantes, ao custo de não ter gerenciamento automático de dependências nem um framework de testes de verdade.

A estrutura de pacotes segue `clubeleitura` (pacote raiz, com a classe `Main` e classes de execução manual de teste), `clubeleitura.modelo` (as entidades de domínio) e `clubeleitura.excecao` (as quatro exceções customizadas).

Como não há JUnit configurado, a verificação do sistema foi feita com classes Java comuns que têm um método `main` e imprimem o resultado no console: `Main` roda um cenário de demonstração geral, `FluxoCompletoTest` prova o fluxo de ponta a ponta (criar grupo, entrar no grupo, registrar progresso, comentar, ver ranking, terminar o livro e avaliar) imprimindo PASS ou FAIL para cada verificação, e `TesteRanking` usa `assert` para cobrir casos de ranking, empate, streak e prazo expirado.

Por fim, o repositório tem uma pasta `mockups` com um protótipo visual estático em HTML e CSS puro (sem framework de front-end), representando as telas principais da aplicação (início, ranking, capítulo). Esse protótipo ainda não está conectado ao backend em Java, é uma referência visual de como a interface deve funcionar quando for implementada de fato.

<a id="5-resultados-e-consideracoes-finais"></a>
## 5. Resultados e Considerações Finais

O que foi entregue nesta etapa é o núcleo de domínio completo do sistema, rodando via linha de comando: criação de grupos (`Clube` e `Desafio`) com validação de código, entrada de membros, registro diário de progresso de leitura, cálculo de streak, ranking que muda de critério dependendo do tipo de grupo, comentários com spoiler automaticamente escondido de quem está atrasado na leitura, e avaliação final do livro com validação de nota. Os sete conceitos de POO pedidos pela disciplina aparecem em pontos concretos do código e não como decoração: herança (`Grupo` → `Clube`/`Desafio`), polimorfismo dinâmico (`calcularPontuacao` sobrescrito, chamado através da referência da superclasse em `getRanking()`), polimorfismo estático (as sobrecargas de `registrarProgresso`), encapsulamento (atributos privados, objetos imutáveis, listas devolvidas como cópia), coleções (`List`, `TreeSet`, `Comparator`), tratamento de exceções (as quatro exceções de negócio, cada uma nascendo de uma regra real do domínio) e as estruturas básicas de controle usadas no cálculo de streak e na montagem do ranking.

A divisão do trabalho por bloco de classe (cada integrante dona de um conjunto de entidades, do desenho até a apresentação) funcionou bem para dar autonomia, mas trouxe um risco real: como os blocos dependem uns dos outros (o ranking de `Grupo` depende de `Membro`, o spoiler de `Comentario` depende do capítulo atual calculado em `Membro`), uma mudança de assinatura de método em um bloco quebra silenciosamente quem já estava usando aquele método. Isso aconteceu mais de uma vez durante o desenvolvimento, por exemplo quando a classe de registro de progresso foi renomeada e teve o cálculo de streak movido de lugar, e exigiu ajustar as outras classes que dependiam dela para o projeto voltar a compilar. A lição prática foi perceber, na prática, por que times maiores combinam contratos de interface antes de escrever a lógica interna.

A ausência de um framework de teste também pesou mais do que parecia no início. Sem JUnit, cada verificação virou uma classe `main` separada com prints manuais de PASS e FAIL, o que funciona mas é mais trabalhoso de manter e mais fácil de esquecer de rodar depois de uma mudança. Foi justamente escrevendo um teste de ponta a ponta mais formal, cobrindo o fluxo completo em vez de só ler a saída visual do console, que apareceu um bug real: quando um membro tinha dois registros de progresso na mesma data, o método que calculava "qual é o capítulo atual" ficava preso no primeiro registro daquele dia em vez de considerar o mais recente. O bug só ficou visível porque o teste comparava um resultado esperado contra o real, o que reforçou a importância de testar com verificação explícita e não só rodando o programa e lendo a tela.

Como aprendizado de Java e do paradigma orientado a objetos, o ponto mais claro foi sentir na prática a diferença entre polimorfismo estático e dinâmico, que até então eram só definição de livro. Ver o mesmo `getRanking()` funcionando certo pra `Clube` e pra `Desafio` sem nenhum `if` de tipo no meio do caminho deixou concreto por que a disciplina insiste tanto nesse conceito. Outro ponto de amadurecimento foi entender que regra de negócio (como a de spoiler) fica mais fácil de testar e de confiar quando mora dentro da entidade do domínio, em vez de espalhada pela camada que vai exibir a informação.

Como sugestão para a disciplina, teria ajudado ter algum material ou aula tocando especificamente em como organizar um projeto Java multi-pessoa sem framework (sem Maven ou Gradle), porque foi um ponto que consumiu mais tempo do time do que o esperado e que provavelmente se repete em outros grupos que optam pelo mesmo caminho.