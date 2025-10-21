package agent;

public class MasterAgent {

    private final IntentAnalyzer intentAnalyzer = new IntentAnalyzer();
    private final StoryAgent storyAgent = new StoryAgent();
    private final EntityAgent entityAgent = new EntityAgent();

    public String processInput(String input) {
        IntentType type = intentAnalyzer.analyzeIntent(input);
        return switch (type) {
            case CONTINUE_STORY -> storyAgent.narrate(input);
            case CREATE_ENTITY -> entityAgent.generateEntity(input);
            default -> "Je ne comprends pas la demande.";
        };
    }

    enum IntentType { CONTINUE_STORY, CREATE_ENTITY, UNKNOWN }

    static class IntentAnalyzer {
        IntentType analyzeIntent(String input) {
            String s = input.toLowerCase();
            if (s.contains("crée") || s.contains("génère")) return IntentType.CREATE_ENTITY;
            if (s.contains("raconte") || s.contains("attaque") || s.contains("explore"))
                return IntentType.CONTINUE_STORY;
            return IntentType.UNKNOWN;
        }
    }
}
