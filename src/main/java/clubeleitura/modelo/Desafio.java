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
    public double calcularRanking(Membro membro) {
        int totalPaginas = 0;
        for (RegistroLeitura registro : membro.getRegistros()) {
            totalPaginas += registro.getPaginasLidas();
        }

        double percentual = (totalPaginas * 100.0) / metaPaginas;
        return Math.min(percentual, 100.0);
    }

    public RegistroLeitura registrarLeitura(Membro membro, int paginas, Capitulo capitulo, LocalDate data)
            throws PrazoDesafioExpiradoException, CapituloInvalidoException {
        if (data.isAfter(prazoFinal)) {
            throw new PrazoDesafioExpiradoException(
                    "O prazo do desafio '" + getNome() + "' expirou em " + prazoFinal
                            + ", nao e possivel registrar leitura em " + data + ".");
        }
        return membro.registrarLeitura(paginas, capitulo, data);
    }

    public LocalDate getPrazoFinal() {
        return prazoFinal;
    }

    public int getMetaPaginas() {
        return metaPaginas;
    }
}
