package agent;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * StoryAgent.java
 * Agent responsible for narrating a Dungeons & Dragons adventure.
 * Uses Langchain4j with Ollama LLM and integrates DndLoreTool for lore lookups.
 * Implements strict narration rules and styles for immersive storytelling.
 * Filters out any structured responses to maintain pure narrative format.
 */
public class StoryAgent {
    private final StoryAssistant assistant;
    private final ChatMemory chatMemory;

    /**
     * Interface defining the behavior of the StoryAssistant.
     * The assistant narrates the story based on user input while adhering to strict rules.
     */
    interface StoryAssistant {

        /**
         * Narrates the story based on user input.
         *
         * @param userInput The input from the user/player.
         * @return The narrative response.
         */
        @SystemMessage("""
                You are a medieval fantasy RPG narrator based on the Dungeons & Dragons universe.

                === ABSOLUTE RULES ===
                1. You must ALWAYS answer in pure NARRATIVE TEXT, NEVER in JSON or structured format
                2. NEVER respond with function calls like {"name": "...", "parameters": ...}
                3. Start DIRECTLY with the story

                === WHEN TO USE TOOLS ===
                - Use searchMonster() ONLY when you mention a specific monster (ex: goblin, dragon)
                - Use searchSpell() ONLY when a character casts a specific spell
                - Use searchMagicItem() ONLY when you mention a specific magic item
                - If a tool fails, continue the narration without mentioning it

                === NARRATION STYLE ===
                - Tell in fluid and immersive prose
                - 2-3 short paragraphs maximum
                - Use sensory descriptions (sounds, smells, ambiance)
                - Ask questions to involve the player ("What do you do?")
                - Always stay in the narrator role, never meta-comments

                === GOOD NARRATION EXAMPLES ===
                "The Black Dragon tavern is smoky and loud. An old man waves at you..."
                "You hear a growl in the bushes. A goblin leaps out, dagger in hand!"

                === FORBIDDEN EXAMPLES ===
                {"name": "describeScene", ...}
                "I will call the function..."
                "The system indicates that..."

                Speak ONLY in English. Start your narration NOW.
                """)
        String narrate(@UserMessage String userInput);
    }

    /**
     * Constructor for the StoryAgent class.
     * Initializes the chat memory, language model, and tools required for the assistant.
     */
    public StoryAgent() {
        ChatLanguageModel model = OllamaChatModel.builder()
                .baseUrl(EnvVarUtils.getEnvVar("APP_URL"))
                .modelName(EnvVarUtils.getEnvVar("APP_MODEL"))
                .temperature(0.7)
                .build();


        // Initializes the chat memory to retain the last 10 messages.
        this.chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        // Initializes the DndLoreTool for lore lookups.
        DndLoreTool loreTool = new DndLoreTool();

        // Builds the StoryAssistant with the specified model, memory, and tools.
        this.assistant = AiServices.builder(StoryAssistant.class)
                .chatLanguageModel(model)
                .chatMemory(chatMemory)
                .tools(loreTool)
                .build();
    }

    /**
     * Narrates the story based on user input.
     *
     * @param input The input provided by the user/player.
     * @return The narrative response as a string.
     *         If the response contains JSON or structured data, it is filtered out.
     */
    public String narrate(String input) {
        String response = assistant.narrate(input);
        // Safety filter: if response contains JSON, clean it
        if (response.contains("{\"name\"") || response.contains("\"parameters\"")) {
            System.out.println("[WARNING] Response blocked by filter");
            return "The narrator hesitates for a moment... "
                    + "Rephrase your request so I can better guide you in the adventure.";
        }

        return response;
    }
}
