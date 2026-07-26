package clubeleitura.modelo;

import java.util.ArrayList;
import java.util.List;

public class Livro {

    private final String titulo;
    private final String autor;
    private final List<Capitulo> capitulos = new ArrayList<>();

    public Livro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
    }

    public void adicionarCapitulo(Capitulo capitulo) {
        capitulos.add(capitulo);
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
}
