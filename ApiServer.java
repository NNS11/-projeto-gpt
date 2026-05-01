import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.time.Duration;

public class ApiServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/generate", exchange -> {

            if ("POST".equals(exchange.getRequestMethod())) {

                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes());

                String prompt = body.replace("{\"prompt\":\"", "").replace("\"}", "");

                String json = "{"
                        + "\"prompt\":\"" + prompt + "\","
                        + "\"model_id\":\"gpt-image-2-t2i\","
                        + "\"size\":\"1024x1024\","
                        + "\"key\":\"SUA_API_KEY_AQUI\""
                        + "}";

                HttpClient client = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(10))
                        .build();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://modelslab.com/api/v7/images/text-to-image"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.body().length());

                OutputStream os = exchange.getResponseBody();
                os.write(response.body().getBytes());
                os.close();
            }
        });

        server.start();
        System.out.println("🔥 Servidor rodando em http://localhost:8080");
    }
}
