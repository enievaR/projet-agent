package agent;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class StoryAgentTest {

    @Test
    void narratesStoryForValidInput() {
        var mockAssistant = Mockito.mock(StoryAgent.StoryAssistant.class);
        var input = "Describe the tavern scene";
        var expectedResponse = "The Black Dragon tavern is smoky and loud. An old man waves at you...";

        Mockito.when(mockAssistant.narrate(input)).thenReturn(expectedResponse);

        var storyAgent = instantiateWithoutConstructor(StoryAgent.class);
        injectAssistant(storyAgent, mockAssistant);

        var result = storyAgent.narrate(input);

        Assertions.assertEquals(expectedResponse, result);
        Mockito.verify(mockAssistant, Mockito.times(1)).narrate(input);
    }

    @Test
    void blocksStructuredResponseContainingJson() {
        var mockAssistant = Mockito.mock(StoryAgent.StoryAssistant.class);
        var input = "Describe the dragon";
        var structuredResponse = "{\"name\": \"describeScene\", \"parameters\": {\"scene\": \"dragon\"}}";

        Mockito.when(mockAssistant.narrate(input)).thenReturn(structuredResponse);

        var storyAgent = instantiateWithoutConstructor(StoryAgent.class);
        injectAssistant(storyAgent, mockAssistant);

        var result = storyAgent.narrate(input);

        Assertions.assertEquals("The narrator hesitates for a moment... Rephrase your request so I can better guide you in the adventure.", result);
        Mockito.verify(mockAssistant, Mockito.times(1)).narrate(input);
    }

    @Test
    void blocksStructuredResponseContainingParameters() {
        var mockAssistant = Mockito.mock(StoryAgent.StoryAssistant.class);
        var input = "Describe the magic item";
        var structuredResponse = "The system indicates that \"parameters\" are missing.";

        Mockito.when(mockAssistant.narrate(input)).thenReturn(structuredResponse);

        var storyAgent = instantiateWithoutConstructor(StoryAgent.class);
        injectAssistant(storyAgent, mockAssistant);

        var result = storyAgent.narrate(input);

        Assertions.assertEquals("The narrator hesitates for a moment... Rephrase your request so I can better guide you in the adventure.", result);
        Mockito.verify(mockAssistant, Mockito.times(1)).narrate(input);
    }

    @Test
    void handlesNullInputGracefully() {
        var mockAssistant = Mockito.mock(StoryAgent.StoryAssistant.class);

        Mockito.when(mockAssistant.narrate(null)).thenThrow(new NullPointerException("Input cannot be null"));

        var storyAgent = instantiateWithoutConstructor(StoryAgent.class);
        injectAssistant(storyAgent, mockAssistant);

        Assertions.assertThrows(NullPointerException.class, () -> storyAgent.narrate(null));
        Mockito.verify(mockAssistant, Mockito.times(1)).narrate(null);
    }

    @Test
    void handlesEmptyInputGracefully() {
        var mockAssistant = Mockito.mock(StoryAgent.StoryAssistant.class);
        var input = "";
        var expectedResponse = "The narrator hesitates for a moment... Rephrase your request so I can better guide you in the adventure.";

        Mockito.when(mockAssistant.narrate(input)).thenReturn(expectedResponse);

        var storyAgent = instantiateWithoutConstructor(StoryAgent.class);
        injectAssistant(storyAgent, mockAssistant);

        var result = storyAgent.narrate(input);

        Assertions.assertEquals(expectedResponse, result);
        Mockito.verify(mockAssistant, Mockito.times(1)).narrate(input);
    }

    private static StoryAgent instantiateWithoutConstructor(Class<StoryAgent> clazz) {
        try {
            Constructor<?> objCtor = Object.class.getDeclaredConstructor();
            ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
            @SuppressWarnings("unchecked")
            Constructor<StoryAgent> customCtor = (Constructor<StoryAgent>) rf.newConstructorForSerialization(clazz, objCtor);
            customCtor.setAccessible(true);
            return customCtor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate StoryAgent without constructor", e);
        }
    }

    private static void injectAssistant(StoryAgent storyAgent, StoryAgent.StoryAssistant assistant) {
        try {
            Field assistantField = StoryAgent.class.getDeclaredField("assistant");
            assistantField.setAccessible(true);
            assistantField.set(storyAgent, assistant);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject assistant", e);
        }
    }

}