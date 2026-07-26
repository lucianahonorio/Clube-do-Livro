package clubeleitura.modelo;

import java.time.LocalDate;

public class RegistroProgresso {

    private final LocalDate data;
    private final int paginasLidas;
    private final Capitulo capitulo;
    private final String comentario;

    public RegistroProgresso(LocalDate data, int paginasLidas, Capitulo capitulo, String comentario) {
        this.data = data;
        this.paginasLidas = paginasLidas;
        this.capitulo = capitulo;
        this.comentario = comentario;
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

    public String getComentario() {
        return comentario;
    }
}
