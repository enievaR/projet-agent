package agent;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public class StoryAgent {
    
    interface StoryAssistant {
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

    private final StoryAssistant assistant;

    public StoryAgent() {
        ChatLanguageModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("llama3.1")
                .temperature(0.7)
                .build();

        DnDLoreTool loreTool = new DnDLoreTool();

        this.assistant = AiServices.builder(StoryAssistant.class)
                .chatLanguageModel(model)
                .tools(loreTool)
                .build();
    }

    public String narrate(String input) {
        String response = assistant.narrate(input);
        
        // Safety filter: if response contains JSON, clean it
        if (response.contains("{\"name\"") || response.contains("\"parameters\"")) {
            return "The narrator hesitates for a moment... Rephrase your request so I can better guide you in the adventure.";
        }
        
        return response;
    }
}
