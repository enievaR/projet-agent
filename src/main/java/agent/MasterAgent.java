/**
 * MasterAgent class that coordinates between different specialized agents
 * to process user inputs and generate appropriate responses.
 * Delegates tasks to StoryAgent for storytelling and EntityAgent for entity generation.
 * Author: Florian Mordohai with LLM assistance
 */

package agent;

public class MasterAgent {

    private final IntentAnalyzer intentAnalyzer = new IntentAnalyzer(); // Analyze user intent
    private final StoryAgent storyAgent = new StoryAgent(); // Storytelling agent
    private final EntityAgent entityAgent = new EntityAgent(); // Entity generation agent

    public String processInput(String input) {
        IntentTypeEnum type = intentAnalyzer.analyzeIntent(input); // Determine intent type

        return switch (type) { // Delegate to appropriate agent based on intent
            case CONTINUE_STORY -> storyAgent.narrate(input);
            case CREATE_ENTITY -> entityAgent.generateEntity(input);
            default -> "I did not understand your request."; // Fallback for unrecognized intents
        };
    }
}