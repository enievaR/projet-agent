package agent;

public class MasterAgent {

    private final IntentAnalyzer intentAnalyzer = new IntentAnalyzer();
    private final StoryAgent storyAgent = new StoryAgent();
    private final EntityAgent entityAgent = new EntityAgent();

    public String processInput(String input) {
        IntentAnalyzer.IntentType type = intentAnalyzer.analyzeIntent(input);

        return switch (type) {
            case CONTINUE_STORY -> storyAgent.narrate(input);
            case CREATE_ENTITY -> entityAgent.generateEntity(input);
            default -> "Je ne comprends pas la demande.";
        };
    }
}

