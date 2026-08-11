package clubeleitura.web;

import clubeleitura.excecao.CapituloInvalidoException;
import clubeleitura.modelo.Capitulo;
import clubeleitura.modelo.Grupo;
import clubeleitura.modelo.Membro;
import clubeleitura.modelo.SeedDeTeste;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servidor minimo usando o HttpServer embutido do JDK (sem framework, sem
 * dependencia externa). Serve os mockups estaticos da Malu e expoe
 * /api/ranking e /api/progresso sobre um unico cenario do SeedDeTeste
 * mantido em memoria pelo tempo de vida do processo, pra provar a ponte
 * entre o front e o back sem precisar reescrever a interface.
 */
public class ApiServer {

    private static final Path PASTA_MOCKUPS = Path.of("mockups").toAbsolutePath().normalize();

    private static final SeedDeTeste.Cenario CENARIO = SeedDeTeste.criarCenarioCompleto();

    public static void main(String[] args) throws IOException {
        int porta = 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(porta), 0);
        server.createContext("/api/ranking", ApiServer::responderRanking);
        server.createContext("/api/progresso", ApiServer::responderRegistrarLeitura);
        server.createContext("/", ApiServer::servirArquivoEstatico);
        server.start();

        System.out.println("Servidor rodando em http://localhost:" + porta);
        System.out.println("API de ranking em http://localhost:" + porta + "/api/ranking");
        System.out.println("API de registro de leitura em http://localhost:" + porta + "/api/progresso (POST)");
    }

    private static void responderRanking(HttpExchange exchange) throws IOException {
        String json = "{"
                + "\"clube\":" + grupoParaJson(CENARIO.getClube(), "páginas lidas") + ","
                + "\"desafio\":" + grupoParaJson(CENARIO.getDesafio(), "dias de streak")
                + "}";

        responderJson(exchange, json);
    }

    private static void responderRegistrarLeitura(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            responderErro(exchange, 405, "Use POST para registrar leitura.");
            return;
        }

        String corpo = lerCorpo(exchange);
        String nomeMembro = campoTexto(corpo, "membro");
        Integer numeroCapitulo = campoInteiro(corpo, "capitulo");
        Integer paginas = campoInteiro(corpo, "paginas");
        String comentario = campoTexto(corpo, "comentario");

        if (nomeMembro == null || numeroCapitulo == null || paginas == null) {
            responderErro(exchange, 400, "Informe membro, capitulo e paginas.");
            return;
        }

        Membro membro = buscarMembroPorNome(nomeMembro);
        Capitulo capitulo = buscarCapituloPorNumero(numeroCapitulo);
        if (membro == null || capitulo == null) {
            responderErro(exchange, 404, "Membro ou capitulo nao encontrado.");
            return;
        }

        try {
            membro.registrarProgresso(paginas, capitulo, comentario == null || comentario.isBlank() ? null : comentario);
        } catch (CapituloInvalidoException e) {
            responderErro(exchange, 400, e.getMessage());
            return;
        }

        String json = "{"
                + "\"ok\":true,"
                + "\"membro\":\"" + escapar(membro.getNome()) + "\","
                + "\"capituloAtual\":" + membro.getCapituloAtual().getNumero() + ","
                + "\"streak\":" + membro.calcularStreak() + ","
                + "\"paginasTotaisClube\":" + CENARIO.getClube().calcularPontuacao(membro)
                + "}";
        responderJson(exchange, json);
    }

    private static Membro buscarMembroPorNome(String nome) {
        for (Membro membro : CENARIO.getMembros()) {
            if (membro.getNome().equalsIgnoreCase(nome)) {
                return membro;
            }
        }
        return null;
    }

    private static Capitulo buscarCapituloPorNumero(int numero) {
        for (Capitulo capitulo : CENARIO.getLivro().getCapitulos()) {
            if (capitulo.getNumero() == numero) {
                return capitulo;
            }
        }
        return null;
    }

    private static String lerCorpo(HttpExchange exchange) throws IOException {
        try (InputStream entrada = exchange.getRequestBody();
                ByteArrayOutputStream saida = new ByteArrayOutputStream()) {
            entrada.transferTo(saida);
            return saida.toString(StandardCharsets.UTF_8);
        }
    }

    private static String campoTexto(String json, String chave) {
        Matcher m = Pattern.compile("\"" + chave + "\"\\s*:\\s*\"([^\"]*)\"").matcher(json);
        return m.find() ? m.group(1) : null;
    }

    private static Integer campoInteiro(String json, String chave) {
        Matcher m = Pattern.compile("\"" + chave + "\"\\s*:\\s*(-?\\d+)").matcher(json);
        return m.find() ? Integer.valueOf(m.group(1)) : null;
    }

    private static void responderErro(HttpExchange exchange, int status, String mensagem) throws IOException {
        String json = "{\"ok\":false,\"erro\":\"" + escapar(mensagem) + "\"}";
        byte[] corpo = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(status, corpo.length);
        try (OutputStream saida = exchange.getResponseBody()) {
            saida.write(corpo);
        }
    }

    private static String grupoParaJson(Grupo grupo, String criterio) {
        List<Membro> ranking = grupo.getRanking();

        StringBuilder rankingJson = new StringBuilder("[");
        for (int i = 0; i < ranking.size(); i++) {
            Membro membro = ranking.get(i);
            if (i > 0) {
                rankingJson.append(",");
            }
            rankingJson.append("{\"nome\":\"").append(escapar(membro.getNome())).append("\",")
                    .append("\"pontos\":").append(grupo.calcularPontuacao(membro)).append("}");
        }
        rankingJson.append("]");

        return "{\"nome\":\"" + escapar(grupo.getNome()) + "\","
                + "\"criterio\":\"" + escapar(criterio) + "\","
                + "\"ranking\":" + rankingJson + "}";
    }

    private static String escapar(String texto) {
        return texto == null ? "" : texto.replace("\"", "\\\"");
    }

    private static void responderJson(HttpExchange exchange, String json) throws IOException {
        byte[] corpo = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, corpo.length);
        try (OutputStream saida = exchange.getResponseBody()) {
            saida.write(corpo);
        }
    }

    private static void servirArquivoEstatico(HttpExchange exchange) throws IOException {
        String caminhoPedido = exchange.getRequestURI().getPath();
        if (caminhoPedido.equals("/")) {
            caminhoPedido = "/index.html";
        }

        Path arquivo = PASTA_MOCKUPS.resolve(caminhoPedido.substring(1)).normalize();

        if (!arquivo.startsWith(PASTA_MOCKUPS) || !Files.exists(arquivo) || Files.isDirectory(arquivo)) {
            responderNaoEncontrado(exchange);
            return;
        }

        byte[] conteudo = Files.readAllBytes(arquivo);
        exchange.getResponseHeaders().add("Content-Type", tipoDeConteudo(arquivo));
        exchange.sendResponseHeaders(200, conteudo.length);
        try (OutputStream saida = exchange.getResponseBody()) {
            saida.write(conteudo);
        }
    }

    private static void responderNaoEncontrado(HttpExchange exchange) throws IOException {
        byte[] corpo = "404 - arquivo nao encontrado".getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(404, corpo.length);
        try (OutputStream saida = exchange.getResponseBody()) {
            saida.write(corpo);
        }
    }

    private static String tipoDeConteudo(Path arquivo) {
        String nome = arquivo.getFileName().toString();
        if (nome.endsWith(".html")) {
            return "text/html; charset=utf-8";
        }
        if (nome.endsWith(".css")) {
            return "text/css; charset=utf-8";
        }
        if (nome.endsWith(".js")) {
            return "application/javascript; charset=utf-8";
        }
        return "application/octet-stream";
    }
}
