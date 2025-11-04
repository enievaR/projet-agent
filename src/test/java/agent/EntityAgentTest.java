// src/test/java/agent/EntityAgentTest.java
package agent;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityAgentTest {

    @Test
    public void generateEntity_returnsEmptyStringForEmptyInput() throws Exception {
        try (MockedStatic<EnvVarUtils> envMock = Mockito.mockStatic(EnvVarUtils.class)) {
            envMock.when(() -> EnvVarUtils.getEnvVar("APP_URL")).thenReturn("http://localhost");
            envMock.when(() -> EnvVarUtils.getEnvVar("APP_MODEL")).thenReturn("test-model");

            EntityAgent agent = new EntityAgent();

            ChatLanguageModel mockModel = Mockito.mock(ChatLanguageModel.class);

            String input = "";
            String expectedPrompt = """
                    Génère une entité RPG sous forme de JSON avec :
                    - type (monstre, arme, PNJ…)
                    - nom
                    - rareté
                    - description

                    Exemple :
                    {
                      "type": "arme",
                      "nom": "Épée de feu",
                      "rareté": "rare",
                      "description": "Forgée dans les volcans de Thalara."
                    }

                    Demande du joueur : %s
                    """.formatted(input);

            String fakeResponse = "";
            Mockito.when(mockModel.generate(expectedPrompt)).thenReturn(fakeResponse);

            Field modelField = EntityAgent.class.getDeclaredField("model");
            modelField.setAccessible(true);
            modelField.set(agent, mockModel);

            String result = agent.generateEntity(input);

            assertEquals(fakeResponse, result);
            Mockito.verify(mockModel).generate(expectedPrompt);
        }
    }

    @Test
    public void generateEntity_handlesNullInputGracefully() throws Exception {
        try (MockedStatic<EnvVarUtils> envMock = Mockito.mockStatic(EnvVarUtils.class)) {
            envMock.when(() -> EnvVarUtils.getEnvVar("APP_URL")).thenReturn("http://localhost");
            envMock.when(() -> EnvVarUtils.getEnvVar("APP_MODEL")).thenReturn("test-model");

            EntityAgent agent = new EntityAgent();

            ChatLanguageModel mockModel = Mockito.mock(ChatLanguageModel.class);

            String input = null;
            String expectedPrompt = """
                    Génère une entité RPG sous forme de JSON avec :
                    - type (monstre, arme, PNJ…)
                    - nom
                    - rareté
                    - description

                    Exemple :
                    {
                      "type": "arme",
                      "nom": "Épée de feu",
                      "rareté": "rare",
                      "description": "Forgée dans les volcans de Thalara."
                    }

                    Demande du joueur : %s
                    """.formatted(input);

            String fakeResponse = "";
            Mockito.when(mockModel.generate(expectedPrompt)).thenReturn(fakeResponse);

            Field modelField = EntityAgent.class.getDeclaredField("model");
            modelField.setAccessible(true);
            modelField.set(agent, mockModel);

            String result = agent.generateEntity(input);

            assertEquals(fakeResponse, result);
            Mockito.verify(mockModel).generate(expectedPrompt);
        }
    }

    @Test
    public void generateEntity_handlesSpecialCharactersInInput() throws Exception {
        try (MockedStatic<EnvVarUtils> envMock = Mockito.mockStatic(EnvVarUtils.class)) {
            envMock.when(() -> EnvVarUtils.getEnvVar("APP_URL")).thenReturn("http://localhost");
            envMock.when(() -> EnvVarUtils.getEnvVar("APP_MODEL")).thenReturn("test-model");

            EntityAgent agent = new EntityAgent();

            ChatLanguageModel mockModel = Mockito.mock(ChatLanguageModel.class);

            String input = "Un dragon de glace avec des @#&*!";
            String expectedPrompt = """
                    Génère une entité RPG sous forme de JSON avec :
                    - type (monstre, arme, PNJ…)
                    - nom
                    - rareté
                    - description

                    Exemple :
                    {
                      "type": "arme",
                      "nom": "Épée de feu",
                      "rareté": "rare",
                      "description": "Forgée dans les volcans de Thalara."
                    }

                    Demande du joueur : %s
                    """.formatted(input);

            String fakeResponse = "{\"type\":\"monstre\",\"nom\":\"Dragon\",\"rareté\":\"épique\",\"description\":\"Un dragon de glace avec des @#&*!\"}";
            Mockito.when(mockModel.generate(expectedPrompt)).thenReturn(fakeResponse);

            Field modelField = EntityAgent.class.getDeclaredField("model");
            modelField.setAccessible(true);
            modelField.set(agent, mockModel);

            String result = agent.generateEntity(input);

            assertEquals(fakeResponse, result);
            Mockito.verify(mockModel).generate(expectedPrompt);
        }
    }

}
