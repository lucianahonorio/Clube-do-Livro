package clubeleitura.modelo;

import clubeleitura.excecao.NotaInvalidaException;

public class Avaliacao {

    private final Membro membro;
    private final Livro livro;
    private final double nota;
    private final String personagemFavorito;
    private final String personagemOdiado;
    private final String personagemIdentificacao;

    public Avaliacao(Membro membro, Livro livro, double nota, String personagemFavorito,
            String personagemOdiado, String personagemIdentificacao) throws NotaInvalidaException {
        if (nota < 0 || nota > 10) {
            throw new NotaInvalidaException(
                    "Nota invalida (" + nota + "). A nota deve estar entre 0 e 10.");
        }
        this.membro = membro;
        this.livro = livro;
        this.nota = nota;
        this.personagemFavorito = personagemFavorito;
        this.personagemOdiado = personagemOdiado;
        this.personagemIdentificacao = personagemIdentificacao;
    }

    public Membro getMembro() {
        return membro;
    }

    public Livro getLivro() {
        return livro;
    }

    public double getNota() {
        return nota;
    }

    public String getPersonagemFavorito() {
        return personagemFavorito;
    }

    public String getPersonagemOdiado() {
        return personagemOdiado;
    }

    public String getPersonagemIdentificacao() {
        return personagemIdentificacao;
    }
}