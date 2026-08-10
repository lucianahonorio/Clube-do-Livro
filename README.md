# Clube de Leitura 

> Projeto desenvolvido para a disciplina de **Programação Orientada a Objetos (POO)**.  
> Uma plataforma desenvolvida em Java para engajar leitores através de clubes, desafios, registro de progresso, streaks e missões gamificadas.

---

## 🎯 Sobre o Projeto

O **Clube de Leitura* é uma aplicação focada em transformar a leitura em uma experiência coletiva e interativa. Os usuários podem criar ou entrar em grupos de leitura, registrar o número de páginas lidas diariamente, interagir com capítulos via comentários protegidos contra spoilers e subir no ranking do grupo através de metas e hábitos contínuos (streaks).

---

## 🛠️ Funcionalidades do MVP

Já implementado no núcleo do backend:

* **Grupos e Desafios:** Suporte a diferentes modalidades de grupos de leitura (Clubes contínuos, com ranking por páginas lidas, e Desafios com prazo, com ranking por streak).
* **Gestão de Leituras:** Cadastro de membros, livros e capítulos.
* **Registro Diário e Streaks:** Apontamento diário de páginas lidas e acompanhamento de dias consecutivos de leitura.
* **Ranking Gamificado:** Classificação dos membros ordenada automaticamente, com critério diferente para Clube e Desafio.
* **Sistema Anti-Spoiler:** Comentários por capítulo com ocultação automática para quem ainda não chegou naquele ponto da leitura.
* **Avaliação do Livro:** Nota final (0 a 10) e indicação de personagens (favorito, odiado e identificação).

Ainda a implementar nesta entrega do MVP:

* **Encontros e Missões:** Agendamento de encontros de discussão e sistema de missões por metas concluídas.
* **Interface gráfica funcional:** já existe um protótipo visual estático (pasta `mockups`), mas ainda não conectado ao backend em Java.

---

## 🏗️ Arquitetura & Conceitos de POO

A aplicação utiliza os princípios fundamentais da Orientação a Objetos para garantir a manutenibilidade e escalabilidade do código:

* **Abstração e Herança:** A classe base `Grupo` é estendida por `Clube` e `Desafio`, reaproveitando estruturas comuns e especializando comportamentos.
* **Polimorfismo:** O cálculo de ranking e pontuação varia de acordo com o tipo do grupo (`Clube` foca em hábitos contínuos; `Desafio` foca em metas e prazos).
* **Encapsulamento:** Proteção do estado interno das entidades (`Membro`, `Livro`, `Capitulo`, etc.) com atributos privados e métodos de acesso controlados.
* **Tratamento de Exceções Customizadas:** Captura e tratamento de erros de negócio específicos (`CapituloInvalidoException`, `PrazoDesafioExpiradoException`, `NotaInvalidaException`, `CodigoGrupoInvalidoException`).
* **Estrutura de Coleções:** Uso estratégico de `Collections` (`List`, `Set`) para gerenciamento de membros, leituras e cálculo de streaks.

---

## 🚀 Visão de Futuro (Próximas Atualizações)

Para versões futuras da plataforma, estão previstos os seguintes incrementos:
* Upload real de fotos como prova visual de leitura diária.
* Feed interativo em tempo real com curtidas e respostas em comentários.
* Integração direta via API com a plataforma do Google Meet.
* Interface gráfica avançada construída em JavaFX ou Swing.

---

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

O `Main` roda um cenário fixo de demonstração: cria membros, um livro, um clube e um desafio, registra leituras, calcula streak e ranking, testa comentários com spoiler e avaliação, e força os quatro erros de negócio (`CapituloInvalidoException`, `PrazoDesafioExpiradoException`, `NotaInvalidaException`, `CodigoGrupoInvalidoException`) para mostrar o tratamento de exceções funcionando.

Além do `Main`, o projeto tem duas classes de verificação que também rodam via linha de comando:

```bash
java -cp out clubeleitura.FluxoCompletoTest
```
Prova o fluxo completo de ponta a ponta (criar grupo, entrar no grupo, registrar progresso, comentar, ver ranking, terminar o livro e avaliar), imprimindo PASS ou FAIL para cada verificação.

```bash
java -ea -cp out clubeleitura.modelo.TesteRanking
```
Cobre casos de ranking, empate, streak e prazo expirado usando `assert` (por isso precisa da flag `-ea` para as verificações rodarem de fato).

## 👩‍💻 Desenvolvedoras

* [Clarice](https://github.com/usuario-clarice)
* [Luciana](https://github.com/usuario-luciana)
* [Malu Quintela](https://github.com/usuario-malu)
* [Sofia](https://github.com/usuario-sofia)