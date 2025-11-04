package agent;

/**
 * IntentAnalyzer.java
 * Analyzes user input to determine the intent type for routing to appropriate agents.
 * author: Florian Mordohai with LLM assistance
 */
public class IntentAnalyzer {

    /**
     * Analyzes the given user input to determine the intent type.
     * The method normalizes the input to lowercase and checks for specific keywords
     * to classify the intent as either entity creation or story continuation.
     * If no specific keywords are found, it defaults to story continuation.
     *
     * @param input The user input as a String.
     * @return The determined intent type as an IntentTypeEnum value.
     *         Possible values are CREATE_ENTITY or CONTINUE_STORY.
     */
    public IntentTypeEnum analyzeIntent(String input) {
        String s = input.toLowerCase(); // Normalize input for analysis
        
        if (s.contains("create") || s.contains("generate")) { // Keywords for entity creation
            return IntentTypeEnum.CREATE_ENTITY;
        }
        
        if (s.contains("tell") || s.contains("attack") || s.contains("explore")
                || s.contains("start") || s.contains("begin")) {
            return IntentTypeEnum.CONTINUE_STORY;
        }
        
        // By default, consider it narration
        return IntentTypeEnum.CONTINUE_STORY;
    }
}