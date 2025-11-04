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

/**
 * The EntityAgent class is responsible for generating RPG entities such as
 * monsters, weapons, or NPCs in JSON format. It uses a ChatLanguageModel
 * to process input and generate the desired output.
 */
public class EntityAgent {
    private final ChatLanguageModel model;

    /**
     * Constructs an EntityAgent and initializes the ChatLanguageModel
     * with configuration values retrieved from environment variables.
     */
    public EntityAgent() {
        this.model = OllamaChatModel.builder()
                .baseUrl(System.getenv("APP_URL"))
                .modelName(System.getenv("APP_MODEL"))
                .build();
    }

    /**
     * Generates an RPG entity in JSON format based on the player's input.
     * The generated JSON includes the entity's type, name, rarity, and description.
     *
     * @param input The player's input describing the desired entity.
     * @return A JSON string representing the generated RPG entity.
     */
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