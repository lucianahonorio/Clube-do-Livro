package clubeleitura.modelo;

import clubeleitura.excecao.CapituloInvalidoException;
import clubeleitura.excecao.CodigoGrupoInvalidoException;
import clubeleitura.excecao.NotaInvalidaException;
import clubeleitura.excecao.PrazoDesafioExpiradoException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class SeedDeTeste {

    private SeedDeTeste() {
    }

    public static Cenario criarCenarioCompleto() {
        try {
            Livro livro = criarLivro();

            Clube clube = new Clube("CLB001", "Clube do Livro Teste");
            clube.setLivroAtual(livro);

            Membro luciana = new Membro("Luciana");
            Membro sofia = new Membro("Sofia");
            Membro malu = new Membro("Malu");
            Membro clarice = new Membro("Clarice");

            clube.adicionarMembro(luciana);
            clube.adicionarMembro(sofia);
            clube.adicionarMembro(malu);
            clube.adicionarMembro(clarice);

            List<Capitulo> capitulos = livro.getCapitulos();

            LocalDate hoje = LocalDate.now();

            luciana.registrarProgresso(20, capitulos.get(0), hoje.minusDays(9), null);
            luciana.registrarProgresso(20, capitulos.get(1), hoje.minusDays(8), null);
            luciana.registrarProgresso(20, capitulos.get(2), hoje.minusDays(7), null);
            luciana.registrarProgresso(20, capitulos.get(3), hoje.minusDays(6), null);
            luciana.registrarProgresso(20, capitulos.get(4), hoje.minusDays(5), null);
            luciana.registrarProgresso(20, capitulos.get(5), hoje.minusDays(4), null);
            luciana.registrarProgresso(20, capitulos.get(6), hoje.minusDays(3), null);
            luciana.registrarProgresso(20, capitulos.get(7), hoje.minusDays(2), null);
            luciana.registrarProgresso(20, capitulos.get(8), hoje.minusDays(1), null);
            luciana.registrarProgresso(20, capitulos.get(9), hoje, null);

            sofia.registrarProgresso(20, capitulos.get(0), hoje.minusDays(4), null);
            sofia.registrarProgresso(20, capitulos.get(1), hoje.minusDays(3), null);
            sofia.registrarProgresso(15, capitulos.get(2), hoje.minusDays(2), null);
            sofia.registrarProgresso(20, capitulos.get(3), hoje.minusDays(1), null);
            sofia.registrarProgresso(10, capitulos.get(4), hoje, null);

            malu.registrarProgresso(20, capitulos.get(0), hoje.minusDays(1), null);
            malu.registrarProgresso(8, capitulos.get(1), hoje, null);

            List<Comentario> comentarios = new ArrayList<>();
            comentarios.add(new Comentario(
                    "Gostei muito do jeito que a autora apresentou o Ryan no capítulo 1.",
                    capitulos.get(0), TipoComentario.COMUM, malu));
            comentarios.add(new Comentario(
                    "Esse foi meu capítulo favorito até agora, a tensão entre os dois tá demais.",
                    capitulos.get(4), TipoComentario.MOMENTO_FAVORITO, sofia));
            comentarios.add(new Comentario(
                    "Não esperava essa reviravolta no capítulo 8, mudou tudo!",
                    capitulos.get(7), TipoComentario.COMUM, luciana));
            comentarios.add(new Comentario(
                    "O final me deixou emocionada, capítulo perfeito pra fechar o livro.",
                    capitulos.get(9), TipoComentario.MOMENTO_FAVORITO, luciana));

            List<Avaliacao> avaliacoes = new ArrayList<>();
            avaliacoes.add(new Avaliacao(luciana, livro, 9.5, "Ryan", "Não tem vilão claro", "Personagem secundária"));

            return new Cenario(clube, livro, List.of(luciana, sofia, malu, clarice), comentarios, avaliacoes);
        } catch (CodigoGrupoInvalidoException | CapituloInvalidoException
                | PrazoDesafioExpiradoException | NotaInvalidaException e) {
            throw new RuntimeException("Falha ao montar o cenario de teste.", e);
        }
    }

    private static Livro criarLivro() {
        Livro livro = new Livro("Binding 13", "Chloe Walsh");
        for (int i = 1; i <= 10; i++) {
            int inicio = (i - 1) * 20 + 1;
            int fim = i * 20;
            livro.adicionarCapitulo(new Capitulo(i, "Capítulo " + i, inicio, fim));
        }
        return livro;
    }

    public static class Cenario {

        private final Clube clube;
        private final Livro livro;
        private final List<Membro> membros;
        private final List<Comentario> comentarios;
        private final List<Avaliacao> avaliacoes;

        public Cenario(Clube clube, Livro livro, List<Membro> membros,
                List<Comentario> comentarios, List<Avaliacao> avaliacoes) {
            this.clube = clube;
            this.livro = livro;
            this.membros = membros;
            this.comentarios = comentarios;
            this.avaliacoes = avaliacoes;
        }

        public Clube getClube() {
            return clube;
        }

        public Livro getLivro() {
            return livro;
        }

        public List<Membro> getMembros() {
            return membros;
        }

        public List<Comentario> getComentarios() {
            return comentarios;
        }

        public List<Avaliacao> getAvaliacoes() {
            return avaliacoes;
        }
    }
}