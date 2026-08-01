package clubeleitura.modelo;

public class Comentario {

    private final String texto;
    private final Capitulo capitulo;
    private final TipoComentario tipo;
    private final Membro autor;

    public Comentario(String texto, Capitulo capitulo, TipoComentario tipo, Membro autor) {
        this.texto = texto;
        this.capitulo = capitulo;
        this.tipo = tipo;
        this.autor = autor;
    }

    public boolean isVisivelPara(Membro leitor) {
        if (leitor == autor) {
            return true;
        }

        Capitulo capituloAtual = leitor.getCapituloAtual();
        if (capituloAtual == null) {
            return false;
        }

        return capitulo.getNumero() <= capituloAtual.getNumero();
    }

    public String getTextoParaExibir(Membro leitor) {
        if (isVisivelPara(leitor)) {
            return texto;
        }
        return "[comentário oculto - spoiler do capítulo " + capitulo.getNumero() + "]";
    }

    public String getTexto() {
        return texto;
    }

    public Capitulo getCapitulo() {
        return capitulo;
    }

    public TipoComentario getTipo() {
        return tipo;
    }

    public Membro getAutor() {
        return autor;
    }
}