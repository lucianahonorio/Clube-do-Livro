package clubeleitura;

import clubeleitura.excecao.CapituloInvalidoException;
import clubeleitura.excecao.CodigoGrupoInvalidoException;
import clubeleitura.excecao.NotaInvalidaException;
import clubeleitura.excecao.PrazoDesafioExpiradoException;
import clubeleitura.modelo.Avaliacao;
import clubeleitura.modelo.Capitulo;
import clubeleitura.modelo.Clube;
import clubeleitura.modelo.Comentario;
import clubeleitura.modelo.Livro;
import clubeleitura.modelo.Membro;
import clubeleitura.modelo.TipoComentario;

import java.time.LocalDate;
import java.util.List;

/**
 * Prova de ponta a ponta do fluxo do MVP: criar grupo, entrar no grupo,
 * registrar progresso, comentar (com spoiler), ver ranking, terminar o
 * livro e avaliar. Cada etapa e verificada separadamente, sem depender
 * de leitura visual da saida do Main.
 */
public class FluxoCompletoTest {

    private static int passou = 0;
    private static int falhou = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("=== Fluxo completo: criar grupo -> entrar -> registrar -> comentar -> ranking -> terminar -> avaliar ===\n");

        Livro livro = criarLivro();

        System.out.println("1) Criar grupo");
        Clube clube = criarGrupo();

        System.out.println("\n2) Entrar no grupo");
        Membro sofia = new Membro("Sofia");
        Membro malu = new Membro("Malu");
        entrarNoGrupo(clube, sofia, malu);

        System.out.println("\n3) Registrar progresso");
        registrarProgresso(sofia, malu, livro);

        System.out.println("\n4) Comentar (com regra de spoiler)");
        comentar(sofia, malu, livro);

        System.out.println("\n5) Ver ranking");
        verRanking(clube, sofia, malu);

        System.out.println("\n6) Terminar o livro");
        terminarLivro(sofia, livro);

        System.out.println("\n7) Avaliar");
        avaliar(sofia, livro);

        System.out.println();
        System.out.println(passou + " passaram, " + falhou + " falharam.");
        if (falhou > 0) {
            System.exit(1);
        }
    }

    private static void checar(String nome, boolean condicao) {
        if (condicao) {
            passou++;
            System.out.println("  PASS - " + nome);
        } else {
            falhou++;
            System.out.println("  FAIL - " + nome);
        }
    }

    private static Livro criarLivro() {
        Livro livro = new Livro("O Pequeno Principe", "Antoine de Saint-Exupery");
        livro.adicionarCapitulo(new Capitulo(1, "Capitulo 1", 1, 10));
        livro.adicionarCapitulo(new Capitulo(2, "Capitulo 2", 11, 20));
        livro.adicionarCapitulo(new Capitulo(3, "Capitulo 3", 21, 25));
        return livro;
    }

    private static Clube criarGrupo() throws CodigoGrupoInvalidoException {
        Clube clube = new Clube("CLB-E2E", "Clube do fluxo completo");
        checar("grupo foi criado com o codigo esperado", "CLB-E2E".equals(clube.getCodigo()));
        checar("grupo comeca sem membros", clube.getMembros().isEmpty());
        return clube;
    }

    private static void entrarNoGrupo(Clube clube, Membro sofia, Membro malu) {
        clube.adicionarMembro(sofia);
        clube.adicionarMembro(malu);
        checar("os dois membros entraram no grupo", clube.getMembros().size() == 2);
        checar("entrar de novo com o mesmo membro nao duplica", () -> {
            clube.adicionarMembro(sofia);
            return clube.getMembros().size() == 2;
        });
    }

    private static void registrarProgresso(Membro sofia, Membro malu, Livro livro)
            throws CapituloInvalidoException, PrazoDesafioExpiradoException {
        Capitulo capitulo1 = livro.getCapitulos().get(0);
        Capitulo capitulo2 = livro.getCapitulos().get(1);
        LocalDate hoje = LocalDate.now();

        sofia.registrarProgresso(10, capitulo1, hoje.minusDays(1), null);
        sofia.registrarProgresso(10, capitulo2, hoje, null);
        malu.registrarProgresso(10, capitulo1, hoje, null);

        checar("Sofia tem 2 registros de progresso", sofia.getRegistros().size() == 2);
        checar("Sofia esta no capitulo 2 apos os registros", sofia.getCapituloAtual().getNumero() == 2);
        checar("Malu ainda esta no capitulo 1", malu.getCapituloAtual().getNumero() == 1);
    }

    private static Comentario comentar(Membro sofia, Membro malu, Livro livro) {
        Capitulo capitulo2 = livro.getCapitulos().get(1);
        Comentario comentario = new Comentario(
                "Capitulo 2 revela algo importante sobre a rosa.",
                capitulo2, TipoComentario.COMUM, sofia);

        checar("autora sempre ve o proprio comentario", comentario.isVisivelPara(sofia));
        checar("Malu (ainda no capitulo 1) NAO ve o comentario do capitulo 2 - spoiler",
                !comentario.isVisivelPara(malu));
        checar("texto exibido pra Malu vem mascarado",
                comentario.getTextoParaExibir(malu).contains("oculto"));
        return comentario;
    }

    private static void verRanking(Clube clube, Membro sofia, Membro malu) {
        List<Membro> ranking = clube.getRanking();
        checar("ranking tem os 2 membros do grupo", ranking.size() == 2);
        checar("Sofia (mais paginas lidas) aparece em 1o no ranking", ranking.get(0) == sofia);
        checar("pontuacao de Sofia no ranking bate com calcularPontuacao",
                clube.calcularPontuacao(sofia) >= clube.calcularPontuacao(malu));
    }

    private static void terminarLivro(Membro sofia, Livro livro)
            throws CapituloInvalidoException, PrazoDesafioExpiradoException {
        Capitulo ultimoCapitulo = livro.getCapitulos().get(livro.getCapitulos().size() - 1);
        sofia.registrarProgresso(ultimoCapitulo.getTotalPaginas(), ultimoCapitulo, LocalDate.now(), null);

        checar("Sofia chegou ao ultimo capitulo do livro",
                sofia.getCapituloAtual().getNumero() == ultimoCapitulo.getNumero());
    }

    private static void avaliar(Membro sofia, Livro livro) {
        boolean avaliacaoValidaFuncionou;
        try {
            Avaliacao avaliacao = new Avaliacao(sofia, livro, 9.0, "O Principe", "Ninguem", "A raposa");
            avaliacaoValidaFuncionou = avaliacao.getNota() == 9.0 && avaliacao.getLivro() == livro;
        } catch (NotaInvalidaException e) {
            avaliacaoValidaFuncionou = false;
        }
        checar("avaliacao com nota valida (0-10) e aceita", avaliacaoValidaFuncionou);

        boolean notaInvalidaFoiRejeitada = false;
        try {
            new Avaliacao(sofia, livro, 15, "O Principe", "Ninguem", "A raposa");
        } catch (NotaInvalidaException e) {
            notaInvalidaFoiRejeitada = true;
        }
        checar("avaliacao com nota fora de 0-10 lanca NotaInvalidaException", notaInvalidaFoiRejeitada);
    }

    private interface Condicao {
        boolean avaliar();
    }

    private static void checar(String nome, Condicao condicao) {
        checar(nome, condicao.avaliar());
    }
}
