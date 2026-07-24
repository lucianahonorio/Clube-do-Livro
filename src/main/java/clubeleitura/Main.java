package clubeleitura;

import clubeleitura.excecao.CapituloInvalidoException;
import clubeleitura.excecao.CodigoGrupoInvalidoException;
import clubeleitura.excecao.NotaInvalidaException;
import clubeleitura.excecao.PrazoDesafioExpiradoException;
import clubeleitura.modelo.Capitulo;
import clubeleitura.modelo.Clube;
import clubeleitura.modelo.Desafio;
import clubeleitura.modelo.Grupo;
import clubeleitura.modelo.Livro;
import clubeleitura.modelo.Membro;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Clube de Leitura Gamificado ===\n");

        Livro livro = new Livro("Dom Casmurro", "Machado de Assis");
        Capitulo capitulo1 = new Capitulo(1, 20);
        Capitulo capitulo2 = new Capitulo(2, 15);
        livro.adicionarCapitulo(capitulo1);
        livro.adicionarCapitulo(capitulo2);

        Membro ana = new Membro("Ana");
        Membro bruno = new Membro("Bruno");

        try {
            Clube clube = new Clube("CLB01", "Leitores Assiduos");
            clube.setLivroAtual(livro);
            clube.adicionarMembro(ana);
            clube.adicionarMembro(bruno);

            Desafio desafio = new Desafio("DES01", "Desafio 10 Dias", LocalDate.now().plusDays(10), 200);
            desafio.setLivroAtual(livro);
            desafio.adicionarMembro(ana);
            desafio.adicionarMembro(bruno);

            registrarLeiturasDeExemplo(ana, bruno, capitulo1);

            System.out.println("--- Tentativas invalidas (exibindo o tratamento de excecoes) ---");
            forcarCapituloInvalido(ana, capitulo1);
            forcarPrazoExpirado(desafio, ana, capitulo1);
            forcarNotaInvalida(livro);

            System.out.println("\n--- Ranking do Clube (polimorfismo dinamico) ---");
            imprimirRanking(clube, List.of(ana, bruno));

            System.out.println("\n--- Ranking do Desafio (polimorfismo dinamico) ---");
            imprimirRanking(desafio, List.of(ana, bruno));

        } catch (CodigoGrupoInvalidoException e) {
            System.out.println("Nao foi possivel criar o grupo: " + e.getMessage());
        }

        System.out.println("\n--- Codigo de grupo invalido (exibindo o tratamento de excecoes) ---");
        forcarCodigoGrupoInvalido();
    }

    private static void registrarLeiturasDeExemplo(Membro ana, Membro bruno, Capitulo capitulo) {
        try {
            for (int diasAtras = 4; diasAtras >= 0; diasAtras--) {
                LocalDate data = LocalDate.now().minusDays(diasAtras);
                ana.registrarLeitura(5 + diasAtras, capitulo, data);
            }
            bruno.registrarLeitura(10, capitulo);
        } catch (CapituloInvalidoException e) {
            System.out.println("Erro ao registrar leitura de exemplo: " + e.getMessage());
        }
    }

    private static void forcarCapituloInvalido(Membro membro, Capitulo capitulo) {
        try {
            membro.registrarLeitura(999, capitulo);
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

    private static void forcarNotaInvalida(Livro livro) {
        try {
            livro.avaliar(15);
        } catch (NotaInvalidaException e) {
            System.out.println("[NotaInvalidaException] " + e.getMessage());
        }
    }

    private static void forcarCodigoGrupoInvalido() {
        try {
            new Clube("AB", "Clube com codigo curto demais");
        } catch (CodigoGrupoInvalidoException e) {
            System.out.println("[CodigoGrupoInvalidoException] " + e.getMessage());
        }
    }

    private static void imprimirRanking(Grupo grupo, List<Membro> membros) {
        for (Membro membro : membros) {
            double pontuacao = grupo.calcularRanking(membro);
            System.out.printf("%s: %.1f pontos%n", membro.getNome(), pontuacao);
        }
    }
}
