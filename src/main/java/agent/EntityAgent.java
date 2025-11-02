/**
 * EntityAgent.java
 * 
 * Agent responsible for generating RPG entities like monsters, weapons, or NPCs.
 * Not in use yet, Made StoryAgent in priority.
 * author: Florian Mordohai with LLM assistance
 */


package agent;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class EntityAgent {
    private final ChatLanguageModel model;

    public EntityAgent() {
        this.model = OllamaChatModel.builder()
                .baseUrl(System.getenv("APP_URL"))
                .modelName(System.getenv("APP_MODEL"))
                .build();
    }

    public String generateEntity(String input) {
        String prompt = """
                Génère une entité RPG sous forme de JSON avec :
                - type (monstre, arme, PNJ…)
                - nom
                - rareté
                - description

                Exemple :
                {
                  "type": "arme",
                  "nom": "Épée de feu",
                  "rareté": "rare",
                  "description": "Forgée dans les volcans de Thalara."
                }

                Demande du joueur : %s
                """.formatted(input);
        return model.generate(prompt);
    }
}