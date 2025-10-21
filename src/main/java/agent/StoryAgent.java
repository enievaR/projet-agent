package agent;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class StoryAgent {
    private final ChatLanguageModel model;

    public StoryAgent() {
        this.model = OllamaChatModel.builder()
                .baseUrl(System.getenv("APP_URL"))
                .modelName(System.getenv("APP_MODEL")) // ou "llama3"
                .build();
    }

    public String narrate(String input) {
        String prompt = """
                Tu es un narrateur de RPG médiéval fantastique.
                Raconte la suite de l’aventure de façon immersive et cohérente.
                Sois descriptif mais concis.

                Joueur : %s
                Narration :
                """.formatted(input);
        return model.generate(prompt);
    }
}
