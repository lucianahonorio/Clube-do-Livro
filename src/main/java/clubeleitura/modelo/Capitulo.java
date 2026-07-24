package clubeleitura.modelo;

public class Capitulo {

    private final int numero;
    private final int totalPaginas;

    public Capitulo(int numero, int totalPaginas) {
        this.numero = numero;
        this.totalPaginas = totalPaginas;
    }

    public int getNumero() {
        return numero;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }
}
