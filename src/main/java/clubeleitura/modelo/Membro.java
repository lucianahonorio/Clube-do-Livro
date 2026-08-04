package clubeleitura.modelo;

import clubeleitura.excecao.CapituloInvalidoException;
import clubeleitura.excecao.PrazoDesafioExpiradoException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class Membro {

    private final String nome;
    private final List<RegistroProgresso> registros = new ArrayList<>();

    public Membro(String nome) {
        this.nome = nome;
    }

    public RegistroProgresso registrarProgresso(int paginas, Capitulo capitulo) throws CapituloInvalidoException {
        return criarRegistro(paginas, capitulo, LocalDate.now(), null);
    }

    public RegistroProgresso registrarProgresso(int paginas, Capitulo capitulo, String comentario)
            throws CapituloInvalidoException {
        return criarRegistro(paginas, capitulo, LocalDate.now(), comentario);
    }

    public RegistroProgresso registrarProgresso(int paginas, Capitulo capitulo, LocalDate data, LocalDate prazoLimite)
            throws CapituloInvalidoException, PrazoDesafioExpiradoException {
        verificarPrazo(data, prazoLimite);
        return criarRegistro(paginas, capitulo, data, null);
    }

    public RegistroProgresso registrarProgresso(
            int paginas, Capitulo capitulo, LocalDate data, LocalDate prazoLimite, String comentario)
            throws CapituloInvalidoException, PrazoDesafioExpiradoException {
        verificarPrazo(data, prazoLimite);
        return criarRegistro(paginas, capitulo, data, comentario);
    }

    private void verificarPrazo(LocalDate data, LocalDate prazoLimite) throws PrazoDesafioExpiradoException {
        if (prazoLimite != null && data.isAfter(prazoLimite)) {
            throw new PrazoDesafioExpiradoException(
                    "Prazo expirado em " + prazoLimite + ", nao e possivel registrar progresso em " + data + ".");
        }
    }

    private RegistroProgresso criarRegistro(int paginas, Capitulo capitulo, LocalDate data, String comentario)
            throws CapituloInvalidoException {
        if (capitulo == null) {
            throw new CapituloInvalidoException("Nenhum capitulo informado para o registro de progresso.");
        }
        if (paginas <= 0 || paginas > capitulo.getTotalPaginas()) {
            throw new CapituloInvalidoException(
                    "Paginas invalidas (" + paginas + ") para o capitulo " + capitulo.getNumero()
                            + ", que possui " + capitulo.getTotalPaginas() + " paginas.");
        }

        RegistroProgresso registro = new RegistroProgresso(data, paginas, capitulo, comentario);
        registros.add(registro);
        return registro;
    }

    public int calcularStreak() {
        TreeSet<LocalDate> datasUnicas = new TreeSet<>(Comparator.reverseOrder());
        for (RegistroProgresso registro : registros) {
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

    public RegistroProgresso getUltimoRegistro() {
        RegistroProgresso ultimo = null;
        for (RegistroProgresso registro : registros) {
            if (ultimo == null || !registro.getData().isBefore(ultimo.getData())) {
                ultimo = registro;
            }
        }
        return ultimo;
    }

    public Capitulo getCapituloAtual() {
        RegistroProgresso ultimo = getUltimoRegistro();
        return ultimo == null ? null : ultimo.getCapitulo();
    }

    public String getNome() {
        return nome;
    }

    public List<RegistroProgresso> getRegistros() {
        return registros;
    }
}
