package clubeleitura.modelo;

import clubeleitura.excecao.CodigoGrupoInvalidoException;

import java.util.ArrayList;
import java.util.List;

public abstract class Grupo {

    private final String codigo;
    private final String nome;
    private final List<Membro> membros = new ArrayList<>();
    private Livro livroAtual;

    protected Grupo(String codigo, String nome) throws CodigoGrupoInvalidoException {
        if (codigo == null || codigo.trim().length() < 3) {
            throw new CodigoGrupoInvalidoException(
                    "Codigo de grupo invalido: '" + codigo + "'. Use ao menos 3 caracteres.");
        }
        this.codigo = codigo;
        this.nome = nome;
    }

    public void adicionarMembro(Membro membro) {
        if (!membros.contains(membro)) {
            membros.add(membro);
        }
    }

    public abstract double calcularPontuacao(Membro membro);

    public double calcularRanking(Membro membro) {
        return calcularPontuacao(membro);
    }

    public List<Membro> getRanking() {
        return List.of();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public List<Membro> getMembros() {
        return membros;
    }

    public Livro getLivroAtual() {
        return livroAtual;
    }

    public void setLivroAtual(Livro livroAtual) {
        this.livroAtual = livroAtual;
    }
}
