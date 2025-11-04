package agent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.Tool;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.jetbrains.annotations.NotNull;


/**
 * DndLoreTool.java
 * Tool for retrieving lore information from the D&D 5E API.
 * Provides methods to search for monsters, spells, and magic items.
 * Uses Java HttpClient for API requests and Jackson for JSON parsing.
 * Handles errors gracefully and returns formatted strings.
 * author: Meryem Mellagui & Florian Mordohai with LLM assistance
 */
public class DndLoreTool {

    /**
     * Base URL for the D&D 5E API
     * This is the root URL for all API requests.
     */
    private static final String API_BASE = "https://www.dnd5eapi.co/api/2014"; 
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    /**
     * Constructor initializes HttpClient and ObjectMapper.
     */
    public DndLoreTool() { // Constructor initializes HttpClient and ObjectMapper
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        this.objectMapper = new ObjectMapper();
    }


    /**
     * Search for a D&D monster by name.
     *
     * @param monsterName The name of the monster to search for.
     * @return A formatted string containing the monster's details,
     *         or an error message if not found.
     */
    @Tool("Search for information about a D&D monster by name (ex: goblin, dragon, beholder)")
    public String searchMonster(String monsterName) {
        try {
            String cleanName = monsterName.toLowerCase() // Normalize the monster name
                    .trim()
                    .replaceAll("\\s+", "-");
            
            String url = API_BASE + "/monsters/" + cleanName; // Construct the API URL
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            

            /*
             * Send the HTTP request and handle the response.
             */
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());

            /*
             * Error handling
             */
            if (response.statusCode() != 200) {
                return "Monster '" + monsterName + "' not found in D&D bestiary.";
            }
            
            JsonNode monster = objectMapper.readTree(response.body());
            
            if (!monster.has("name")) {
                return "Monster '" + monsterName + "' not found.";
            }

            return formatMonsterDetails(monster);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            /*
             * General exception handling gives the agent the ability to continue the story.
             */
            return "Unable to retrieve info about '" + monsterName + "'. Use your D&D creativity.";
        }
    }

    /**
     * Search for a D&D spell by name.
     *
     * @param spellName The name of the spell to search for.
     * @return A formatted string containing the spell's details, or an error message if not found.
     */
    @Tool("Search for a D&D spell by name (ex: fireball, magic-missile, cure-wounds)")
    public String searchSpell(String spellName) {
        try {
            String cleanName = spellName.toLowerCase() // Normalize the spell name
                    .trim()
                    .replaceAll("\\s+", "-"); 
            
            String url = API_BASE + "/spells/" + cleanName;
            
            HttpRequest request = HttpRequest.newBuilder() // Build the HTTP request
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            

            /*
             * Errors handling
             */
            if (response.statusCode() != 200) {
                return "Spell '" + spellName + "' not found.";
            }
            
            JsonNode spell = objectMapper.readTree(response.body());
            
            if (!spell.has("name")) {
                return "Spell '" + spellName + "' not found.";
            }
            return formatSpellDetails(spell);
        

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            /*
             * General exception handling gives the agent the ability to continue the story.
             */
            return "Unable to retrieve info about spell '" + spellName + "'.";
        }
    }

    /**
     * Search for a D&D magic item by name.
     *
     * @param itemName The name of the magic item to search for.
     * @return A formatted string containing the item's details, or an error message if not found.
     */
    @Tool("Search for a D&D magic item by name (ex: bag-of-holding, vorpal-sword)")
    public String searchMagicItem(String itemName) {
        
        try {
            String cleanName = itemName.toLowerCase() // Normalize the item name
                    .trim()
                    .replaceAll("\\s+", "-");
            
            String url = API_BASE + "/magic-items/" + cleanName;
            
            HttpRequest request = HttpRequest.newBuilder() // Build the HTTP request
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            
            /*
             * Errors handling
             */
            if (response.statusCode() != 200) {
                return "Magic item '" + itemName + "' not found.";
            }
            
            JsonNode item = objectMapper.readTree(response.body());
            
            if (!item.has("name")) {
                return "Magic item '" + itemName + "' not found.";
            }

            return formatMagicItemDetails(item);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            /*
             * General exception handling gives the agent the ability to continue the story.
             */
            return "Unable to retrieve info about item '" + itemName + "'.";
        }
    }


    /**
     * Format monster details into a readable string.
     *
     * @param monster The JSON node containing monster details.
     * @return A formatted string with the monster's details.
     */
    private String formatMonsterDetails(JsonNode monster) {

        /*
         * Build the details string
         */
        StringBuilder details = new StringBuilder();
        details.append("D&D MONSTER: ").append(monster.get("name").asText()).append("\n");
        details.append("Type: ").append(monster.get("type").asText()).append("\n");
        details.append("Size: ").append(monster.get("size").asText()).append("\n");

        // Optional fields
        if (monster.has("alignment")) {
            details.append("Alignment: ").append(monster.get("alignment").asText()).append("\n");
        }
        // Required fields
        details.append("Challenge Rating: ").append(monster.get("challenge_rating")
                .asText()).append("\n");
        details.append("Hit Points: ").append(monster.get("hit_points").asInt()).append("\n");

        // Armor class might be an array
        if (monster.has("armor_class") && monster.get("armor_class").isArray()
                && !monster.get("armor_class").isEmpty()
        ) {
            details.append("Armor Class: ").append(monster.get("armor_class").get(0).get("value")
                    .asInt()).append("\n");
        }
        
        return details.toString();
    }

    /**
     * Format spell details into a readable string.
     *
     * @param spell The JSON node containing spell details.
     * @return A formatted string with the spell's details.
     */
    private String formatSpellDetails(JsonNode spell) {
        /*
         * Build the details string
         */
        StringBuilder details = new StringBuilder();
        details.append("D&D SPELL: ").append(spell.get("name").asText()).append("\n");
        details.append("Level: ").append(spell.get("level").asInt()).append("\n");
        details.append("School: ").append(spell.get("school").get("name").asText()).append("\n");
        details.append("Casting Time: ").append(spell.get("casting_time").asText()).append("\n");
        details.append("Range: ").append(spell.get("range").asText()).append("\n");

        // Optional fields
        return getOptionalFields(spell, details);
    }

    /**
     * Format magic item details into a readable string.
     *
     * @param item The JSON node containing magic item details.
     * @return A formatted string with the item's details.
     */
    private String formatMagicItemDetails(JsonNode item) {
        /*
         * Build the details string
         */
        StringBuilder details = new StringBuilder();
        details.append("D&D MAGIC ITEM: ").append(item.get("name").asText()).append("\n");

        // Optional fields
        if (item.has("equipment_category")) {
            details.append("Type: ").append(item.get("equipment_category").get("name")
                    .asText()).append("\n");
        }

        // Optional fields
        if (item.has("rarity")) {
            details.append("Rarity: ").append(item.get("rarity").get("name").asText()).append("\n");
        }
        // Description
        return getOptionalFields(item, details);
    }

    @NotNull
    private String getOptionalFields(JsonNode spell, StringBuilder details) {
        if (spell.has("desc") && spell.get("desc").isArray() && !spell.get("desc").isEmpty()) {
            details.append("Description: ");
            String desc = spell.get("desc").get(0).asText();
            details.append(desc, 0, Math.min(200, desc.length())).append("...\n");
        }

        return details.toString();
    }
}