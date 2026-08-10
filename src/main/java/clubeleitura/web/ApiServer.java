package clubeleitura.web;

import clubeleitura.modelo.Grupo;
import clubeleitura.modelo.Membro;
import clubeleitura.modelo.SeedDeTeste;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Servidor minimo usando o HttpServer embutido do JDK (sem framework, sem
 * dependencia externa). Serve os mockups estaticos da Malu e expoe
 * /api/ranking com o cenario do SeedDeTeste, pra provar a ponte entre o
 * front e o back sem precisar reescrever a interface.
 */
public class ApiServer {

    private static final Path PASTA_MOCKUPS = Path.of("mockups").toAbsolutePath().normalize();

    public static void main(String[] args) throws IOException {
        int porta = 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(porta), 0);
        server.createContext("/api/ranking", ApiServer::responderRanking);
        server.createContext("/", ApiServer::servirArquivoEstatico);
        server.start();

        System.out.println("Servidor rodando em http://localhost:" + porta);
        System.out.println("API de ranking em http://localhost:" + porta + "/api/ranking");
    }

    private static void responderRanking(HttpExchange exchange) throws IOException {
        SeedDeTeste.Cenario cenario = SeedDeTeste.criarCenarioCompleto();

        String json = "{"
                + "\"clube\":" + grupoParaJson(cenario.getClube(), "páginas lidas") + ","
                + "\"desafio\":" + grupoParaJson(cenario.getDesafio(), "dias de streak")
                + "}";

        responderJson(exchange, json);
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
