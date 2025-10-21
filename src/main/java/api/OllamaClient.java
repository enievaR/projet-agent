package api;

import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Map;

public class OllamaClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String baseUrl;
    private final Gson gson = new Gson();

    public OllamaClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String generate(String model, String prompt) throws Exception {
        String endpoint = baseUrl + "/api/generate";

        JsonObject body = new JsonObject();
        body.addProperty("model", model);
        body.addProperty("prompt", prompt);
        body.addProperty("stream", false);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

        if (resp.statusCode() != 200) {
            throw new RuntimeException("Erreur Ollama : " + resp.statusCode() + "\n" + resp.body());
        }

        JsonObject result = gson.fromJson(resp.body(), JsonObject.class);
        return result.get("response").getAsString().trim();
    }

}
