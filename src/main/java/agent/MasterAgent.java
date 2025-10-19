package agent;

import api.OllamaClient;

public class MasterAgent {
    private final IntentAnalyzer intentAnalyzer = new IntentAnalyzer();
    private final StoryAgent storyAgent;
    private final EntityAgent entityAgent;

    public MasterAgent() {
        OllamaClient client = new OllamaClient("http://localhost:11434");
        this.storyAgent = new StoryAgent(client);
        this.entityAgent = new EntityAgent(client);
    }

    public String processInput(String userInput) throws Exception {
        IntentAnalyzer.IntentType intent = intentAnalyzer.analyzeIntent(userInput);
        switch (intent) {
            case CONTINUE_STORY:
                return storyAgent.respond(userInput);
            case CREATE_ENTITY:
                return entityAgent.respond(userInput);
            default:
                return "Je n'ai pas compris la commande. Peux-tu reformuler ?";
        }
    }
}
