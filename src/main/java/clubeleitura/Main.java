package clubeleitura;

import clubeleitura.excecao.CapituloInvalidoException;
import clubeleitura.excecao.CodigoGrupoInvalidoException;
import clubeleitura.excecao.NotaInvalidaException;
import clubeleitura.excecao.PrazoDesafioExpiradoException;
import clubeleitura.modelo.Avaliacao;
import clubeleitura.modelo.Capitulo;
import clubeleitura.modelo.Clube;
import clubeleitura.modelo.Comentario;
import clubeleitura.modelo.Desafio;
import clubeleitura.modelo.Grupo;
import clubeleitura.modelo.Livro;
import clubeleitura.modelo.Membro;
import clubeleitura.modelo.SeedDeTeste;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Clube de Leitura Gamificado ===\n");

        Livro livro = new Livro("Dom Casmurro", "Machado de Assis");
        Capitulo capitulo1 = new Capitulo(1, "Capitulo 1", 1, 20);
        Capitulo capitulo2 = new Capitulo(2, "Capitulo 2", 21, 35);
        livro.adicionarCapitulo(capitulo1);
        livro.adicionarCapitulo(capitulo2);

        Membro ana = new Membro("Ana");
        Membro bruno = new Membro("Bruno");

        try {
            Clube clube = new Clube("CLB01", "Leitores Assiduos");
            clube.setLivroAtual(livro);
            clube.adicionarMembro(bruno);
            clube.adicionarMembro(ana);

            Desafio desafio = new Desafio("DES01", "Desafio 10 Dias", LocalDate.now().plusDays(10), 200);
            desafio.setLivroAtual(livro);
            desafio.adicionarMembro(ana);
            desafio.adicionarMembro(bruno);

            registrarLeiturasDeExemplo(ana, bruno, capitulo1);

            System.out.println("--- Tentativas invalidas (exibindo o tratamento de excecoes) ---");
            forcarCapituloInvalido(ana, capitulo1);
            forcarPrazoExpirado(desafio, ana, capitulo1);
            forcarCodigoGrupoInvalido();

            System.out.println("\n--- Ranking do Clube (polimorfismo dinamico) ---");
            imprimirRanking(clube);

            System.out.println("\n--- Ranking do Desafio (polimorfismo dinamico) ---");
            imprimirRanking(desafio);

            System.out.println("\n--- Streak e progresso atual (bloco Membro) ---");
            imprimirProgresso(ana);
            imprimirProgresso(bruno);

            System.out.println("\n--- Comentarios, spoiler e avaliacao (bloco Luciana) ---");
            testarComentariosEAvaliacao();

        } catch (CodigoGrupoInvalidoException e) {
            System.out.println("Nao foi possivel criar o grupo: " + e.getMessage());
        }
    }

    private static void registrarLeiturasDeExemplo(Membro ana, Membro bruno, Capitulo capitulo) {
        try {
            for (int diasAtras = 4; diasAtras >= 0; diasAtras--) {
                LocalDate data = LocalDate.now().minusDays(diasAtras);
                ana.registrarProgresso(5 + diasAtras, capitulo, data, null);
            }
            bruno.registrarProgresso(10, capitulo, "Comecei devagar, mas vou acelerar.");
        } catch (CapituloInvalidoException | PrazoDesafioExpiradoException e) {
            System.out.println("Erro ao registrar leitura de exemplo: " + e.getMessage());
        }
    }

    private static void forcarCapituloInvalido(Membro membro, Capitulo capitulo) {
        try {
            membro.registrarProgresso(999, capitulo);
        } catch (CapituloInvalidoException e) {
            System.out.println("[CapituloInvalidoException] " + e.getMessage());
        }
    }

    private static void forcarPrazoExpirado(Desafio desafio, Membro membro, Capitulo capitulo) {
        try {
            desafio.registrarLeitura(membro, 5, capitulo, LocalDate.now().plusDays(20));
        } catch (PrazoDesafioExpiradoException | CapituloInvalidoException e) {
            System.out.println("[PrazoDesafioExpiradoException] " + e.getMessage());
        }
    }

    private static void forcarCodigoGrupoInvalido() {
        try {
            new Clube("AB", "Clube com codigo curto demais");
        } catch (CodigoGrupoInvalidoException e) {
            System.out.println("[CodigoGrupoInvalidoException] " + e.getMessage());
        }
    }

    private static void imprimirRanking(Grupo grupo) {
        for (Membro membro : grupo.getRanking()) {
            double pontuacao = grupo.calcularPontuacao(membro);
            System.out.printf("%s: %.1f pontos%n", membro.getNome(), pontuacao);
        }
    }

    private static void imprimirProgresso(Membro membro) {
        Capitulo capituloAtual = membro.getCapituloAtual();
        String nomeCapitulo = capituloAtual == null ? "nenhum" : capituloAtual.getTitulo();
        System.out.printf("%s: streak de %d dia(s), capitulo atual: %s%n",
                membro.getNome(), membro.calcularStreak(), nomeCapitulo);
    }

    private static void testarComentariosEAvaliacao() {
        SeedDeTeste.Cenario cenario = SeedDeTeste.criarCenarioCompleto();

        System.out.println("Ranking do clube (Binding 13):");
        imprimirRanking(cenario.getClube());

        System.out.println("\nRanking do desafio (Binding 13):");
        imprimirRanking(cenario.getDesafio());

        Membro malu = cenario.getMembros().get(2);
        System.out.println("\nSpoiler na visao da Malu (leu ate o capitulo 2):");
        for (Comentario comentario : cenario.getComentarios()) {
            System.out.println("Cap " + comentario.getCapitulo().getNumero() + ": "
                    + comentario.getTextoParaExibir(malu));
        }

        Membro clarice = cenario.getMembros().get(3);
        System.out.println("\nSpoiler na visao da Clarice (sem nenhum registro):");
        for (Comentario comentario : cenario.getComentarios()) {
            System.out.println("Cap " + comentario.getCapitulo().getNumero() + ": "
                    + comentario.getTextoParaExibir(clarice));
        }

        System.out.println("\nAvaliacoes:");
        for (Avaliacao avaliacao : cenario.getAvaliacoes()) {
            System.out.printf("%s deu nota %.1f para %s (favorito: %s)%n",
                    avaliacao.getMembro().getNome(), avaliacao.getNota(),
                    avaliacao.getLivro().getTitulo(), avaliacao.getPersonagemFavorito());
        }

        forcarNotaInvalida(cenario);
    }

    private static void forcarNotaInvalida(SeedDeTeste.Cenario cenario) {
        try {
            new Avaliacao(cenario.getMembros().get(0), cenario.getLivro(), 15,
                    "Johnny", "Ninguem", "Personagem secundaria");
        } catch (NotaInvalidaException e) {
            System.out.println("[NotaInvalidaException] " + e.getMessage());
        }
    }
}
