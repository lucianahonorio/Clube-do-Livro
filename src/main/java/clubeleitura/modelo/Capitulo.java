package clubeleitura.modelo;

public class Capitulo {

    private final int numero;
    private final String titulo;
    private final int paginaInicio;
    private final int paginaFim;

    public Capitulo(int numero, String titulo, int paginaInicio, int paginaFim) {
        this.numero = numero;
        this.titulo = titulo;
        this.paginaInicio = paginaInicio;
        this.paginaFim = paginaFim;
    }

    public int getTotalPaginas() {
        return paginaFim - paginaInicio + 1;
    }

    public int getNumero() {
        return numero;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getPaginaInicio() {
        return paginaInicio;
    }

    public int getPaginaFim() {
        return paginaFim;
    }
}
