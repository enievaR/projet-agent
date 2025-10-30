package agent;

/**
 * IntentAnalyzer.java
 * Analyzes user input to determine the intent type for routing to appropriate agents.
 * author: Florian Mordohai with LLM assistance
 */
public class IntentAnalyzer {

    public IntentTypeEnum analyzeIntent(String input) {
        String s = input.toLowerCase(); // Normalize input for analysis
        
        if (s.contains("create") || s.contains("generate")) { // Keywords for entity creation
            return IntentTypeEnum.CREATE_ENTITY;
        }
        
        if (s.contains("tell") || s.contains("attack") || s.contains("explore") || // Keywords for story continuation
            s.contains("start") || s.contains("begin")) {
            return IntentTypeEnum.CONTINUE_STORY;
        }
        
        // By default, consider it narration
        return IntentTypeEnum.CONTINUE_STORY;
    }
}