package clubeleitura.modelo;

import clubeleitura.excecao.CodigoGrupoInvalidoException;

public class Clube extends Grupo {

    public Clube(String codigo, String nome) throws CodigoGrupoInvalidoException {
        super(codigo, nome);
    }

    @Override
    public double calcularPontuacao(Membro membro) {
        int totalPaginas = 0;
        for (RegistroProgresso registro : membro.getRegistros()) {
            totalPaginas += registro.getPaginasLidas();
        }
        return totalPaginas;
    }
}
