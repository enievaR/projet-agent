package agent;

public class IntentAnalyzer {

    public IntentTypeEnum analyzeIntent(String input) {
        String s = input.toLowerCase();
        
        if (s.contains("create") || s.contains("generate")) {
            return IntentTypeEnum.CREATE_ENTITY;
        }
        
        if (s.contains("tell") || s.contains("attack") || s.contains("explore") || 
            s.contains("start") || s.contains("begin")) {
            return IntentTypeEnum.CONTINUE_STORY;
        }
        
        // By default, consider it narration
        return IntentTypeEnum.CONTINUE_STORY;
    }
}