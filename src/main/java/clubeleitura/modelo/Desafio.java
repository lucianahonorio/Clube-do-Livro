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
        this.prazoFinal = prazoFinal;
        this.metaPaginas = metaPaginas;
    }

    @Override
    public double calcularPontuacao(Membro membro) {
        return membro.calcularStreak();
    }

    public RegistroProgresso registrarLeitura(Membro membro, int paginas, Capitulo capitulo, LocalDate data)
            throws PrazoDesafioExpiradoException, CapituloInvalidoException {
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
