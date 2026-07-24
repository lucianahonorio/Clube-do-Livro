package clubeleitura.modelo;

import clubeleitura.excecao.NotaInvalidaException;

import java.util.ArrayList;
import java.util.List;

public class Livro {

    private final String titulo;
    private final String autor;
    private final List<Capitulo> capitulos = new ArrayList<>();
    private Integer notaFinal;

    public Livro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
    }

    public void adicionarCapitulo(Capitulo capitulo) {
        capitulos.add(capitulo);
    }

    public void avaliar(int nota) throws NotaInvalidaException {
        if (nota < 0 || nota > 10) {
            throw new NotaInvalidaException(
                    "Nota " + nota + " invalida para o livro '" + titulo + "'. Use um valor entre 0 e 10.");
        }
        this.notaFinal = nota;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public List<Capitulo> getCapitulos() {
        return capitulos;
    }

    public Integer getNotaFinal() {
        return notaFinal;
    }
}
