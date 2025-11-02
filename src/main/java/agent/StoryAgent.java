package agent;


/**
 * StoryAgent.java
 * Agent responsible for narrating a Dungeons & Dragons adventure.
 * Uses Langchain4j with Ollama LLM and integrates DnDLoreTool for lore lookups.
 * Implements strict narration rules and styles for immersive storytelling.
 * Filters out any structured responses to maintain pure narrative format.
 * Author: Meryem Mellagui & Florian Mordohai with LLM assistance
 */
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * StoryAgent class that narrates a Dungeons & Dragons adventure.
 * It uses a ChatLanguageModel with Ollama and integrates the DnDLoreTool for lore lookups.
 * The agent follows strict narration rules to ensure immersive storytelling.
 *
 */
public class StoryAgent {
    private final StoryAssistant assistant;
    private final ChatMemory chatMemory;
    interface StoryAssistant {

        /**
         * Narrates the story based on user input.
         * @param userInput The input from the user/player.
         * @return The narrative response.
         */
        @SystemMessage("""
                You are a medieval fantasy RPG narrator based on the Dungeons & Dragons universe.

                === ABSOLUTE RULES ===
                1. You must ALWAYS respond in pure NARRATIVE TEXT, NEVER in JSON or structured format
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

    // Constructor
    public StoryAgent() {
        ChatLanguageModel model = OllamaChatModel.builder()
                .baseUrl(System.getenv("APP_URL"))
                .modelName(System.getenv("APP_MODEL"))
                .temperature(0.7)
                .build();


        // Initializes the chat memory to retain the last 10 messages.
        this.chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        // Initializes the DnDLoreTool for lore lookups.
        DnDLoreTool loreTool = new DnDLoreTool();

        // Builds the StoryAssistant with the specified model, memory, and tools.
        this.assistant = AiServices.builder(StoryAssistant.class)
                .chatLanguageModel(model)
                .chatMemory(chatMemory)
                .tools(loreTool)
                .build();
    }

    /**
     * Narrates the story based on user input.
     * @param input User input to narrate the story.
     * @return
     */
    public String narrate(String input) {
        String response = assistant.narrate(input);
        // Safety filter: if response contains JSON, clean it
        if (response.contains("{\"name\"") || response.contains("\"parameters\"")) { // crude check for structured response
            System.out.println("[WARNING] Response blocked by filter");
            return "The narrator hesitates for a moment... Rephrase your request so I can better guide you in the adventure.";
        }

        return response;
    }
}
