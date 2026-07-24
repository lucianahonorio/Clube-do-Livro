package clubeleitura.modelo;

import clubeleitura.excecao.CodigoGrupoInvalidoException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.TreeSet;

public class Clube extends Grupo {

    private static final int PONTOS_POR_DIA_DE_STREAK = 10;

    public Clube(String codigo, String nome) throws CodigoGrupoInvalidoException {
        super(codigo, nome);
    }

    @Override
    public double calcularRanking(Membro membro) {
        int totalPaginas = 0;
        for (RegistroLeitura registro : membro.getRegistros()) {
            totalPaginas += registro.getPaginasLidas();
        }
        return totalPaginas + (calcularStreak(membro) * PONTOS_POR_DIA_DE_STREAK);
    }

    public int calcularStreak(Membro membro) {
        TreeSet<LocalDate> datasUnicas = new TreeSet<>(Comparator.reverseOrder());
        for (RegistroLeitura registro : membro.getRegistros()) {
            datasUnicas.add(registro.getData());
        }

        int streak = 0;
        LocalDate diaAnterior = null;
        for (LocalDate data : datasUnicas) {
            if (diaAnterior == null || diaAnterior.minusDays(1).equals(data)) {
                streak++;
                diaAnterior = data;
            } else {
                break;
            }
        }
        return streak;
    }
}
