package clubeleitura.modelo;

import clubeleitura.excecao.CodigoGrupoInvalidoException;
import clubeleitura.excecao.PrazoDesafioExpiradoException;

import java.time.LocalDate;
import java.util.List;

public class TesteRanking {

        public static void main(String[] args) throws Exception {
                testarCodigoInvalido();
                testarClubeEOrdenacao();
                testarEmpateEGrupoVazio();
                testarDesafioEStreak();
                testarPrazoExpirado();
                testarSeedComClubeEDesafio();

                System.out.println("Todos os testes passaram.");
        }

        private static void testarCodigoInvalido() {
                boolean lancouExcecao = false;

                try {
                        new Clube("AB", "Codigo invalido");
                } catch (CodigoGrupoInvalidoException e) {
                        lancouExcecao = true;
                }

                assert lancouExcecao
                                : "Deveria rejeitar codigo com menos de 3 caracteres.";
        }

        private static void testarClubeEOrdenacao() throws Exception {
                Clube clube = new Clube("CLB01", "Clube de teste");
                Capitulo capitulo = new Capitulo(1, "Capitulo 1", 1, 30);

                Membro ana = new Membro("Ana");
                Membro bruno = new Membro("Bruno");

                ana.registrarProgresso(20, capitulo);
                bruno.registrarProgresso(10, capitulo);

                // Bruno entra primeiro para provar que o ranking realmente ordena.
                clube.adicionarMembro(bruno);
                clube.adicionarMembro(ana);
                clube.adicionarMembro(ana);

                assert clube.getMembros().size() == 2
                                : "O mesmo membro nao deveria ser adicionado duas vezes.";

                assert clube.calcularPontuacao(ana) == 20
                                : "A pontuacao de Ana deveria ser 20.";

                assert clube.calcularPontuacao(bruno) == 10
                                : "A pontuacao de Bruno deveria ser 10.";

                List<Membro> ranking = clube.getRanking();

                assert ranking.get(0) == ana
                                : "Ana deveria ocupar a primeira posicao.";

                assert ranking.get(1) == bruno
                                : "Bruno deveria ocupar a segunda posicao.";
        }

        private static void testarEmpateEGrupoVazio() throws Exception {
                Clube vazio = new Clube("VAZ01", "Grupo vazio");

                assert vazio.getRanking().isEmpty()
                                : "O ranking de um grupo vazio deveria estar vazio.";

                Clube clube = new Clube("EMP01", "Teste de empate");
                Membro bruno = new Membro("Bruno");
                Membro ana = new Membro("Ana");

                clube.adicionarMembro(bruno);
                clube.adicionarMembro(ana);

                List<Membro> ranking = clube.getRanking();

                assert ranking.get(0) == ana
                                : "Em um empate, Ana deveria aparecer antes de Bruno.";

                assert clube.calcularPontuacao(ana) == 0
                                : "Membro sem registros deveria ter zero pontos.";
        }

        private static void testarDesafioEStreak() throws Exception {
                LocalDate hoje = LocalDate.now();

                Desafio desafio = new Desafio(
                                "DES01",
                                "Desafio de teste",
                                hoje.plusDays(1),
                                100);

                Capitulo capitulo = new Capitulo(1, "Capitulo 1", 1, 10);
                Membro ana = new Membro("Ana");
                Membro bruno = new Membro("Bruno");

                desafio.adicionarMembro(bruno);
                desafio.adicionarMembro(ana);

                desafio.registrarLeitura(ana, 1, capitulo, hoje.minusDays(1));
                desafio.registrarLeitura(ana, 1, capitulo, hoje);
                desafio.registrarLeitura(bruno, 1, capitulo, hoje);

                assert desafio.calcularPontuacao(ana) == 2
                                : "Ana deveria possuir streak de 2 dias.";

                assert desafio.calcularPontuacao(bruno) == 1
                                : "Bruno deveria possuir streak de 1 dia.";

                assert desafio.getRanking().get(0) == ana
                                : "Ana deveria liderar o desafio.";
        }

        private static void testarPrazoExpirado() throws Exception {
                LocalDate prazo = LocalDate.now();

                Desafio desafio = new Desafio(
                                "PRZ01",
                                "Teste de prazo",
                                prazo,
                                100);

                Membro membro = new Membro("Ana");
                Capitulo capitulo = new Capitulo(1, "Capitulo 1", 1, 10);
                desafio.adicionarMembro(membro);

                boolean lancouExcecao = false;

                try {
                        desafio.registrarLeitura(
                                        membro,
                                        1,
                                        capitulo,
                                        prazo.plusDays(1));
                } catch (PrazoDesafioExpiradoException e) {
                        lancouExcecao = true;
                }

                assert lancouExcecao
                                : "Deveria rejeitar uma leitura depois do prazo.";
        }

        private static void testarSeedComClubeEDesafio() {
                SeedDeTeste.Cenario cenario = SeedDeTeste.criarCenarioCompleto();

                assert cenario.getDesafio() != null
                                : "O cenario deveria disponibilizar um desafio.";

                assert cenario.getDesafio().getLivroAtual() == cenario.getLivro()
                                : "Clube e desafio deveriam utilizar o mesmo livro.";

                assert cenario.getDesafio().getMembros().equals(cenario.getMembros())
                                : "Clube e desafio deveriam utilizar os mesmos membros.";

                assert !cenario.getDesafio().getRanking().isEmpty()
                                : "O ranking do desafio nao deveria estar vazio.";
        }
}
