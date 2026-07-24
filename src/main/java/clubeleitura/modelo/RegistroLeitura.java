package clubeleitura.modelo;

import java.time.LocalDate;

public class RegistroLeitura {

    private final LocalDate data;
    private final int paginasLidas;
    private final Capitulo capitulo;

    public RegistroLeitura(LocalDate data, int paginasLidas, Capitulo capitulo) {
        this.data = data;
        this.paginasLidas = paginasLidas;
        this.capitulo = capitulo;
    }

    public LocalDate getData() {
        return data;
    }

    public int getPaginasLidas() {
        return paginasLidas;
    }

    public Capitulo getCapitulo() {
        return capitulo;
    }
}
