package agent;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class EntityAgent {
    private final ChatLanguageModel model;

    public EntityAgent() {
        this.model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("llama3")
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