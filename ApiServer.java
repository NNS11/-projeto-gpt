import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.time.Duration;

public class ApiServer {

    static String API_KEY = "SUA_API_KEY_AQUI";

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // CHAT IA REAL
        server.createContext("/chat", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {

                String body = new String(exchange.getRequestBody().readAllBytes());
                String prompt = body.replace("{\"msg\":\"", "").replace("\"}", "");

                String json = "{"
                        + "\"prompt\":\"" + prompt + "\","
                        + "\"model_id\":\"gpt-3.5-turbo\","
                        + "\"key\":\"" + API_KEY + "\""
                        + "}";

                String response = callAPI("https://modelslab.com/api/v6/chat", json);

                send(exchange, response);
            }
        });

        // IMAGEM
        server.createContext("/generate", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {

                String body = new String(exchange.getRequestBody().readAllBytes());
                String prompt = body.replace("{\"prompt\":\"", "").replace("\"}", "");

                String json = "{"
                        + "\"prompt\":\"" + prompt + "\","
                        + "\"model_id\":\"gpt-image-2-t2i\","
                        + "\"size\":\"1024x1024\","
                        + "\"key\":\"" + API_KEY + "\""
                        + "}";

                String response = callAPI("https://modelslab.com/api/v7/images/text-to-image", json);

                send(exchange, response);
            }
        });

        server.start();
        System.out.println("🔥 Rodando em http://localhost:8080");
    }

    static String callAPI(String url, String json) throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    static void send(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
                    }
