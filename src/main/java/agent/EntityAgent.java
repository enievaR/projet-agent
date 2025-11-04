/**
 * EntityAgent.java
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
 * TODO : FINISH THIS CLASS AND REMOVE IT FROM THE EXCLUDE.XML FILE OF SPOTBUGS WHEN READY
 */
public class EntityAgent {
    private final ChatLanguageModel model;

    /**
     * Constructs an EntityAgent and initializes the ChatLanguageModel
     * with configuration values retrieved from environment variables.
     */
    public EntityAgent() {
        this.model = OllamaChatModel.builder()
                .baseUrl(EnvVarUtils.getEnvVar("APP_URL"))
                .modelName(EnvVarUtils.getEnvVar("APP_MODEL"))
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
            Génère une entité RPG sous forme de JSON avec :%n
            - type (monstre, arme, PNJ…)%n
            - nom%n
            - rareté%n
            - description%n
            %n
            Exemple :%n
            {%n
              "type": "arme",%n
              "nom": "Épée de feu",%n
              "rareté": "rare",%n
              "description": "Forgée dans les volcans de Thalara."%n
            }%n
            %n
            Demande du joueur : %s
            """.formatted(input);
        return model.generate(prompt);
    }
}