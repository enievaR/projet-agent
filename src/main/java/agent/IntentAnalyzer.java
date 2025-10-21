package agent;

public class IntentAnalyzer {

    public enum IntentType { CONTINUE_STORY, CREATE_ENTITY, UNKNOWN }

    public IntentType analyzeIntent(String input) {
        String s = input.toLowerCase();
        if (s.contains("crée") || s.contains("génère")) return IntentType.CREATE_ENTITY;
        if (s.contains("raconte") || s.contains("attaque") || s.contains("explore")) return IntentType.CONTINUE_STORY;
        return IntentType.UNKNOWN;
    }
}
