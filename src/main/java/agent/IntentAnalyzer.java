package agent;

public class IntentAnalyzer {
    public enum IntentType { CONTINUE_STORY, CREATE_ENTITY, UNKNOWN }

    public IntentType analyzeIntent(String input) {
        String s = input.toLowerCase();
        if (s.contains("crée") || s.contains("créer") || s.contains("génère")) return IntentType.CREATE_ENTITY;
        if (s.contains("attaque") || s.contains("explore") || s.contains("continue") || s.contains("raconte")) return IntentType.CONTINUE_STORY;
        // fallback
        return IntentType.UNKNOWN;
    }
}

