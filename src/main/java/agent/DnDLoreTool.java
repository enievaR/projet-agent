/**
 * DnDLoreTool.java
 * Tool for retrieving lore information from the D&D 5E API.
 * Provides methods to search for monsters, spells, and magic items.
 * Uses Java HttpClient for API requests and Jackson for JSON parsing.
 * Handles errors gracefully and returns formatted strings.
 * author: Meryem Mellagui & Florian Mordohai with LLM assistance
 */

package agent;

import dev.langchain4j.agent.tool.Tool;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DnDLoreTool {

    /**
     * Base URL for the D&D 5E API
     * This is the root URL for all API requests.
     */
    private static final String API_BASE = "https://www.dnd5eapi.co/api/2014"; 
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DnDLoreTool() { // Constructor initializes HttpClient and ObjectMapper
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        this.objectMapper = new ObjectMapper();
    }


    /**
     * Search for a D&D monster by name.
     * @param monsterName
     * @return
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
            

            /**
             * Send the HTTP request and handle the response.
             */
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());

            /**
             * Error handling
             */
            if (response.statusCode() != 200) {
                return "Monster '" + monsterName + "' not found in D&D bestiary.";
            }
            
            JsonNode monster = objectMapper.readTree(response.body());
            
            if (!monster.has("name")) {
                return "Monster '" + monsterName + "' not found.";
            }
            
            String result = formatMonsterDetails(monster);
            
            return result;

        } catch (Exception e) {
            /**
             * General exception handling gives the agent the ability to continue the story.
             */
            return "Unable to retrieve info about '" + monsterName + "'. Use your D&D creativity.";
        }
    }

    /**
     * Search for a D&D spell by name.
     * @param spellName
     * @return
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
            

            /**
             * Errors handling
             */
            if (response.statusCode() != 200) {
                return "Spell '" + spellName + "' not found.";
            }
            
            JsonNode spell = objectMapper.readTree(response.body());
            
            if (!spell.has("name")) {
                return "Spell '" + spellName + "' not found.";
            }
            
            String result = formatSpellDetails(spell);
            
            return result;
        
            /**
             * General exception handling gives the agent the ability to continue the story.
             */
        } catch (Exception e) {
            return "Unable to retrieve info about spell '" + spellName + "'.";
        }
    }

    /**
     * Search for a D&D magic item by name.
     * @param itemName
     * @return
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
            
            String result = formatMagicItemDetails(item);
            
            return result;
            /**
            * General exception handling gives the agent the ability to continue the story.
            */
        } catch (Exception e) {
            return "Unable to retrieve info about item '" + itemName + "'.";
        }
    }


    /**
     * Format monster details into a readable string.
     * @param monster
     * @return
     */
    private String formatMonsterDetails(JsonNode monster) {

        /**
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
        details.append("Challenge Rating: ").append(monster.get("challenge_rating").asText()).append("\n");
        details.append("Hit Points: ").append(monster.get("hit_points").asInt()).append("\n");

        // Armor class might be an array
        if (monster.has("armor_class") && monster.get("armor_class").isArray() && monster.get("armor_class").size() > 0) {
            details.append("Armor Class: ").append(monster.get("armor_class").get(0).get("value").asInt()).append("\n");
        }
        
        return details.toString();
    }

    /**
     * Format spell details into a readable string.
     * @param spell
     * @return
     */
    private String formatSpellDetails(JsonNode spell) {
        /**
         * Build the details string
         */
        StringBuilder details = new StringBuilder();
        details.append("D&D SPELL: ").append(spell.get("name").asText()).append("\n");
        details.append("Level: ").append(spell.get("level").asInt()).append("\n");
        details.append("School: ").append(spell.get("school").get("name").asText()).append("\n");
        details.append("Casting Time: ").append(spell.get("casting_time").asText()).append("\n");
        details.append("Range: ").append(spell.get("range").asText()).append("\n");

        // Optional fields
        if (spell.has("desc") && spell.get("desc").isArray() && spell.get("desc").size() > 0) {
            details.append("Description: ");
            String desc = spell.get("desc").get(0).asText();
            details.append(desc.substring(0, Math.min(200, desc.length()))).append("...\n");
        }
        
        return details.toString();
    }

    /**
     * Format magic item details into a readable string.
     * @param item
     * @return
     */
    private String formatMagicItemDetails(JsonNode item) {
        /**
         * Build the details string
         */
        StringBuilder details = new StringBuilder();
        details.append("D&D MAGIC ITEM: ").append(item.get("name").asText()).append("\n");

        // Optional fields
        if (item.has("equipment_category")) {
            details.append("Type: ").append(item.get("equipment_category").get("name").asText()).append("\n");
        }

        // Optional fields
        if (item.has("rarity")) {
            details.append("Rarity: ").append(item.get("rarity").get("name").asText()).append("\n");
        }
        // Description
        if (item.has("desc") && item.get("desc").isArray() && item.get("desc").size() > 0) {
            details.append("Description: ");
            String desc = item.get("desc").get(0).asText();
            details.append(desc.substring(0, Math.min(200, desc.length()))).append("...\n");
        }
        
        return details.toString();
    }
}