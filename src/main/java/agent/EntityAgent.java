package agent;

import api.OllamaClient;

public class EntityAgent {
    private final OllamaClient client;
    private final String model = "llama3";

    public EntityAgent(OllamaClient client) {
        this.client = client;
    }

    public String respond(String input) throws Exception {
        String prompt = "Génère une entité RPG (type, nom, stats, description) en JSON.\nRequête: " + input;
        return client.generate(model, prompt);
    }
}
