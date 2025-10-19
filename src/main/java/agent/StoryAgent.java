package agent;

import api.OllamaClient;

public class StoryAgent {
    private final OllamaClient client;
    private final String model = "llama3"; // ou llama3

    public StoryAgent(OllamaClient client) {
        this.client = client;
    }

    public String respond(String input) throws Exception {
        String prompt = "Tu es un narrateur RPG. Répond de manière immersive au joueur.\nJoueur: " + input + "\nNarration:";
        String raw = client.generate(model, prompt);
        // si nécessaire, parser le JSON. Ici on return la chaîne brute.
        return raw;
    }
}

