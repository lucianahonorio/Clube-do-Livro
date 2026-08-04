package clubeleitura.modelo;

import clubeleitura.excecao.CapituloInvalidoException;
import clubeleitura.excecao.CodigoGrupoInvalidoException;
import clubeleitura.excecao.PrazoDesafioExpiradoException;

import java.time.LocalDate;

public class Desafio extends Grupo {

    private final LocalDate prazoFinal;
    private final int metaPaginas;

    public Desafio(String codigo, String nome, LocalDate prazoFinal, int metaPaginas)
            throws CodigoGrupoInvalidoException {
        super(codigo, nome);
        if (prazoFinal == null) {
            throw new IllegalArgumentException("O prazo final nao pode ser nulo.");
        }

        if (metaPaginas <= 0) {
            throw new IllegalArgumentException("A meta de paginas deve ser maior que zero.");
        }
        this.prazoFinal = prazoFinal;
        this.metaPaginas = metaPaginas;
    }

    @Override
    public double calcularPontuacao(Membro membro) {
        return membro.calcularStreak();
    }

    public RegistroProgresso registrarLeitura(Membro membro, int paginas, Capitulo capitulo, LocalDate data)
            throws PrazoDesafioExpiradoException, CapituloInvalidoException {
        if (membro == null) {
            throw new IllegalArgumentException("O membro nao pode ser nulo.");
        }
        if (!getMembros().contains(membro)) {
            throw new IllegalArgumentException("O membro nao pertence ao desafio.");
        }
        if (data == null) {
            throw new IllegalArgumentException("A data da leitura nao pode ser nula.");
        }

        if (data.isAfter(prazoFinal)) {
            throw new PrazoDesafioExpiradoException(
                    "O prazo do desafio '" + getNome() + "' expirou em " + prazoFinal
                            + ", nao e possivel registrar leitura em " + data + ".");
        }
        return membro.registrarProgresso(paginas, capitulo, data, prazoFinal);
    }

    public LocalDate getPrazoFinal() {
        return prazoFinal;
    }

    public int getMetaPaginas() {
        return metaPaginas;
    }
}
