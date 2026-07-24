package clubeleitura.modelo;

import clubeleitura.excecao.CapituloInvalidoException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Membro {

    private final String nome;
    private final List<RegistroLeitura> registros = new ArrayList<>();

    public Membro(String nome) {
        this.nome = nome;
    }

    public RegistroLeitura registrarLeitura(int paginas, Capitulo capitulo) throws CapituloInvalidoException {
        return registrarLeitura(paginas, capitulo, LocalDate.now());
    }

    public RegistroLeitura registrarLeitura(int paginas, Capitulo capitulo, LocalDate data)
            throws CapituloInvalidoException {
        if (capitulo == null) {
            throw new CapituloInvalidoException("Nenhum capitulo informado para o registro de leitura.");
        }
        if (paginas <= 0 || paginas > capitulo.getTotalPaginas()) {
            throw new CapituloInvalidoException(
                    "Paginas invalidas (" + paginas + ") para o capitulo " + capitulo.getNumero()
                            + ", que possui " + capitulo.getTotalPaginas() + " paginas.");
        }

        RegistroLeitura registro = new RegistroLeitura(data, paginas, capitulo);
        registros.add(registro);
        return registro;
    }

    public String getNome() {
        return nome;
    }

    public List<RegistroLeitura> getRegistros() {
        return registros;
    }
}
