package agent;

import org.junit.jupiter.api.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.http.HttpClient;

class DndLoreToolTest {

    private DndLoreTool dndLoreTool;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        dndLoreTool = new DndLoreTool();
        objectMapper = new ObjectMapper();
    }


    @Test
    @DisplayName("Returns formatted details for a valid monster JSON")
    void returnsFormattedDetailsForValidMonster() throws Exception {
        JsonNode monster = objectMapper.readTree("""
            {
                "name": "Goblin",
                "type": "Humanoid",
                "size": "Small",
                "alignment": "Neutral Evil",
                "challenge_rating": "1/4",
                "hit_points": 7,
                "armor_class": [{"value": 15}]
            }
        """);

        Method m = DndLoreTool.class.getDeclaredMethod("formatMonsterDetails", JsonNode.class);
        m.setAccessible(true);
        String result = (String) m.invoke(dndLoreTool, monster);

        Assertions.assertTrue(result.contains("D&D MONSTER: Goblin"));
        Assertions.assertTrue(result.contains("Type: Humanoid"));
        Assertions.assertTrue(result.contains("Size: Small"));
        Assertions.assertTrue(result.contains("Alignment: Neutral Evil"));
        Assertions.assertTrue(result.contains("Challenge Rating: 1/4"));
        Assertions.assertTrue(result.contains("Hit Points: 7"));
        Assertions.assertTrue(result.contains("Armor Class: 15"));
    }

    @Test
    @DisplayName("Handles missing optional fields gracefully")
    void handlesMissingOptionalFieldsForMonsterDetails() throws Exception {
        JsonNode monster = objectMapper.readTree("""
            {
                "name": "Goblin",
                "type": "Humanoid",
                "size": "Small",
                "challenge_rating": "1/4",
                "hit_points": 7
            }
        """);

        Method m = DndLoreTool.class.getDeclaredMethod("formatMonsterDetails", JsonNode.class);
        m.setAccessible(true);
        String result = (String) m.invoke(dndLoreTool, monster);

        Assertions.assertTrue(result.contains("D&D MONSTER: Goblin"));
        Assertions.assertTrue(result.contains("Type: Humanoid"));
        Assertions.assertTrue(result.contains("Size: Small"));
        Assertions.assertFalse(result.contains("Alignment:"));
        Assertions.assertTrue(result.contains("Challenge Rating: 1/4"));
        Assertions.assertTrue(result.contains("Hit Points: 7"));
        Assertions.assertFalse(result.contains("Armor Class:"));
    }



    @Test
    @DisplayName("Returns formatted details for a valid spell JSON")
    void returnsFormattedDetailsForValidSpell() throws Exception {
        JsonNode spell = objectMapper.readTree("""
            {
                "name": "Fireball",
                "level": 3,
                "school": {"name": "Evocation"},
                "casting_time": "1 action",
                "range": "150 feet",
                "desc": ["A bright streak flashes from your pointing finger..."]
            }
        """);

        Method m = DndLoreTool.class.getDeclaredMethod("formatSpellDetails", JsonNode.class);
        m.setAccessible(true);
        String result = (String) m.invoke(dndLoreTool, spell);

        Assertions.assertTrue(result.contains("D&D SPELL: Fireball"));
        Assertions.assertTrue(result.contains("Level: 3"));
        Assertions.assertTrue(result.contains("School: Evocation"));
        Assertions.assertTrue(result.contains("Casting Time: 1 action"));
        Assertions.assertTrue(result.contains("Range: 150 feet"));
        Assertions.assertTrue(result.contains("Description: A bright streak flashes from your pointing finger..."));
    }

    @Test
    @DisplayName("Handles missing optional fields gracefully")
    void handlesMissingOptionalFieldsForSpells() throws Exception {
        JsonNode spell = objectMapper.readTree("""
            {
                "name": "Fireball",
                "level": 3,
                "school": {"name": "Evocation"},
                "casting_time": "1 action",
                "range": "150 feet"
            }
        """);

        Method m = DndLoreTool.class.getDeclaredMethod("formatSpellDetails", JsonNode.class);
        m.setAccessible(true);
        String result = (String) m.invoke(dndLoreTool, spell);

        Assertions.assertTrue(result.contains("D&D SPELL: Fireball"));
        Assertions.assertTrue(result.contains("Level: 3"));
        Assertions.assertTrue(result.contains("School: Evocation"));
        Assertions.assertTrue(result.contains("Casting Time: 1 action"));
        Assertions.assertTrue(result.contains("Range: 150 feet"));
        Assertions.assertFalse(result.contains("Description:"));
    }



    @Test
    @DisplayName("Returns formatted details for a valid magic item JSON")
    void returnsFormattedDetailsForValidMagicItem() throws Exception {
        JsonNode item = objectMapper.readTree("""
            {
                "name": "Bag of Holding",
                "equipment_category": {"name": "Wondrous Item"},
                "rarity": {"name": "Uncommon"},
                "desc": ["This bag has an interior space considerably larger than its outside dimensions..."]
            }
        """);

        Method m = DndLoreTool.class.getDeclaredMethod("formatMagicItemDetails", JsonNode.class);
        m.setAccessible(true);
        String result = (String) m.invoke(dndLoreTool, item);


        Assertions.assertTrue(result.contains("D&D MAGIC ITEM: Bag of Holding"));
        Assertions.assertTrue(result.contains("Type: Wondrous Item"));
        Assertions.assertTrue(result.contains("Rarity: Uncommon"));
        Assertions.assertTrue(result.contains("Description: This bag has an interior space considerably larger than its outside dimensions..."));
    }

    @Test
    @DisplayName("Handles missing optional fields gracefully")
    void handlesMissingOptionalFieldsForMagitItems() throws Exception {
        JsonNode item = objectMapper.readTree("""
            {
                "name": "Bag of Holding"
            }
        """);

        Method m = DndLoreTool.class.getDeclaredMethod("formatMagicItemDetails", JsonNode.class);
        m.setAccessible(true);
        String result = (String) m.invoke(dndLoreTool, item);

        Assertions.assertTrue(result.contains("D&D MAGIC ITEM: Bag of Holding"));
        Assertions.assertFalse(result.contains("Type:"));
        Assertions.assertFalse(result.contains("Rarity:"));
        Assertions.assertFalse(result.contains("Description:"));
    }

    @Test
    void constructorInitializesHttpClientAndObjectMapper() throws Exception {
        DndLoreTool tool = new DndLoreTool();

        Field httpClientField = DndLoreTool.class.getDeclaredField("httpClient");
        httpClientField.setAccessible(true);
        HttpClient httpClient = (HttpClient) httpClientField.get(tool);
        Assertions.assertNotNull(httpClient, "httpClient should be initialized");
        Assertions.assertEquals(HttpClient.Redirect.NORMAL, httpClient.followRedirects(), "HttpClient should follow redirects with NORMAL policy");

        Field objectMapperField = DndLoreTool.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        ObjectMapper objectMapper = (ObjectMapper) objectMapperField.get(tool);
        Assertions.assertNotNull(objectMapper, "objectMapper should be initialized");
    }
}
