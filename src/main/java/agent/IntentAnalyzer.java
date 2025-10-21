package agent;

public class IntentAnalyzer {


    public IntentTypeEnum analyzeIntent(String input) {
        String s = input.toLowerCase();
        if (s.contains("crée") || s.contains("génère")) return IntentTypeEnum.CREATE_ENTITY;
        if (s.contains("raconte") || s.contains("attaque") || s.contains("explore")) return IntentTypeEnum.CONTINUE_STORY;
        return IntentTypeEnum.UNKNOWN;
    }
}
