// java
package agent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class MasterAgentTest {

    @Test
    void delegatesToStoryAgentForContinueStoryIntent() throws Exception {
        var mockStoryAgent = Mockito.mock(StoryAgent.class);
        var mockEntityAgent = Mockito.mock(EntityAgent.class);
        var mockIntentAnalyzer = Mockito.mock(IntentAnalyzer.class);
        var input = "Continue the story";

        Mockito.when(mockIntentAnalyzer.analyzeIntent(input)).thenReturn(IntentTypeEnum.CONTINUE_STORY);
        Mockito.when(mockStoryAgent.narrate(input)).thenReturn("Story continued.");

        MasterAgent masterAgent = instantiateWithoutConstructor(MasterAgent.class);
        injectDependencies(masterAgent, mockIntentAnalyzer, mockStoryAgent, mockEntityAgent);

        var result = masterAgent.processInput(input);

        Assertions.assertEquals("Story continued.", result);
        Mockito.verify(mockStoryAgent, Mockito.times(1)).narrate(input);
        Mockito.verify(mockEntityAgent, Mockito.never()).generateEntity(Mockito.anyString());
    }

    @Test
    void delegatesToEntityAgentForCreateEntityIntent() throws Exception {
        var mockStoryAgent = Mockito.mock(StoryAgent.class);
        var mockEntityAgent = Mockito.mock(EntityAgent.class);
        var mockIntentAnalyzer = Mockito.mock(IntentAnalyzer.class);
        var input = "Create a new character";

        Mockito.when(mockIntentAnalyzer.analyzeIntent(input)).thenReturn(IntentTypeEnum.CREATE_ENTITY);
        Mockito.when(mockEntityAgent.generateEntity(input)).thenReturn("Entity created.");

        MasterAgent masterAgent = instantiateWithoutConstructor(MasterAgent.class);
        injectDependencies(masterAgent, mockIntentAnalyzer, mockStoryAgent, mockEntityAgent);

        var result = masterAgent.processInput(input);

        Assertions.assertEquals("Entity created.", result);
        Mockito.verify(mockEntityAgent, Mockito.times(1)).generateEntity(input);
        Mockito.verify(mockStoryAgent, Mockito.never()).narrate(Mockito.anyString());
    }

    @Test
    void returnsFallbackMessageForUnknownIntent() throws Exception {
        var mockStoryAgent = Mockito.mock(StoryAgent.class);
        var mockEntityAgent = Mockito.mock(EntityAgent.class);
        var mockIntentAnalyzer = Mockito.mock(IntentAnalyzer.class);
        var input = "Unknown input";

        Mockito.when(mockIntentAnalyzer.analyzeIntent(input)).thenReturn(IntentTypeEnum.UNKNOWN);

        MasterAgent masterAgent = instantiateWithoutConstructor(MasterAgent.class);
        injectDependencies(masterAgent, mockIntentAnalyzer, mockStoryAgent, mockEntityAgent);

        var result = masterAgent.processInput(input);

        Assertions.assertEquals("I did not understand your request.", result);
        Mockito.verify(mockStoryAgent, Mockito.never()).narrate(Mockito.anyString());
        Mockito.verify(mockEntityAgent, Mockito.never()).generateEntity(Mockito.anyString());
    }

    @Test
    void handlesNullInputGracefully() throws Exception {
        var mockStoryAgent = Mockito.mock(StoryAgent.class);
        var mockEntityAgent = Mockito.mock(EntityAgent.class);
        var mockIntentAnalyzer = Mockito.mock(IntentAnalyzer.class);

        Mockito.when(mockIntentAnalyzer.analyzeIntent(null)).thenThrow(new NullPointerException("Input cannot be null"));

        MasterAgent masterAgent = instantiateWithoutConstructor(MasterAgent.class);
        injectDependencies(masterAgent, mockIntentAnalyzer, mockStoryAgent, mockEntityAgent);

        Assertions.assertThrows(NullPointerException.class, () -> masterAgent.processInput(null));
        Mockito.verify(mockStoryAgent, Mockito.never()).narrate(Mockito.anyString());
        Mockito.verify(mockEntityAgent, Mockito.never()).generateEntity(Mockito.anyString());
    }

    // --- helpers ---

    private static <T> T instantiateWithoutConstructor(Class<T> clazz) throws Exception {
        Constructor<?> objCtor = Object.class.getDeclaredConstructor();
        ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
        @SuppressWarnings("unchecked")
        Constructor<T> customCtor = (Constructor<T>) rf.newConstructorForSerialization(clazz, objCtor);
        customCtor.setAccessible(true);
        return customCtor.newInstance();
    }

    private static void injectDependencies(MasterAgent masterAgent, IntentAnalyzer intentAnalyzer, StoryAgent storyAgent, EntityAgent entityAgent) throws Exception {
        Field intentField = MasterAgent.class.getDeclaredField("intentAnalyzer");
        Field storyField = MasterAgent.class.getDeclaredField("storyAgent");
        Field entityField = MasterAgent.class.getDeclaredField("entityAgent");

        intentField.setAccessible(true);
        storyField.setAccessible(true);
        entityField.setAccessible(true);

        intentField.set(masterAgent, intentAnalyzer);
        storyField.set(masterAgent, storyAgent);
        entityField.set(masterAgent, entityAgent);
    }
}
