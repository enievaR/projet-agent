package agent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntentAnalyzerTest {

    private final IntentAnalyzer analyzer = new IntentAnalyzer();
    @Test
    void returnsCreateEntityForInputWithCreateKeyword() {
        assertEquals(IntentTypeEnum.CREATE_ENTITY, analyzer.analyzeIntent("Please create a new character."));
    }

    @Test
    void returnsCreateEntityForInputWithGenerateKeyword() {
        assertEquals(IntentTypeEnum.CREATE_ENTITY, analyzer.analyzeIntent("Generate a new sword."));
    }

    @Test
    void returnsContinueStoryForInputWithStoryKeywords() {
        assertEquals(IntentTypeEnum.CONTINUE_STORY, analyzer.analyzeIntent("Tell me what happens next."));
        assertEquals(IntentTypeEnum.CONTINUE_STORY, analyzer.analyzeIntent("Attack the dragon!"));
        assertEquals(IntentTypeEnum.CONTINUE_STORY, analyzer.analyzeIntent("Explore the dungeon."));
        assertEquals(IntentTypeEnum.CONTINUE_STORY, analyzer.analyzeIntent("Start the adventure."));
        assertEquals(IntentTypeEnum.CONTINUE_STORY, analyzer.analyzeIntent("Begin the quest."));
    }

    @Test
    void returnsContinueStoryForUnrecognizedInput() {
        assertEquals(IntentTypeEnum.CONTINUE_STORY, analyzer.analyzeIntent("What is the meaning of life?"));
    }

    @Test
    void handlesEmptyInputGracefully() {
        assertEquals(IntentTypeEnum.CONTINUE_STORY, analyzer.analyzeIntent(""));
    }

    @Test
    void handlesNullInputGracefully() {
        assertThrows(NullPointerException.class, () -> analyzer.analyzeIntent(null));
    }

}