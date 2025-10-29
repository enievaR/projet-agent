package agent;

import dev.langchain4j.agent.tool.Tool;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DnDLoreTool {
    
    private static final String API_BASE = "https://www.dnd5eapi.co/api/2014";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DnDLoreTool() {
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Tool("Search for information about a D&D monster by name (ex: goblin, dragon, beholder)")
    public String searchMonster(String monsterName) {
        System.out.println("[TOOL CALLED] searchMonster with: " + monsterName);
        
        try {
            String cleanName = monsterName.toLowerCase()
                    .trim()
                    .replaceAll("\\s+", "-");
            
            String url = API_BASE + "/monsters/" + cleanName;
            System.out.println("[TOOL] URL called: " + url);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            
            System.out.println("[TOOL] Status code: " + response.statusCode());
            
            if (response.statusCode() != 200) {
                return "Monster '" + monsterName + "' not found in D&D bestiary.";
            }
            
            JsonNode monster = objectMapper.readTree(response.body());
            
            if (!monster.has("name")) {
                return "Monster '" + monsterName + "' not found.";
            }
            
            String result = formatMonsterDetails(monster);
            System.out.println("[TOOL] Result: " + result.substring(0, Math.min(100, result.length())) + "...");
            
            return result;
            
        } catch (Exception e) {
            System.err.println("[TOOL] Error: " + e.getMessage());
            return "Unable to retrieve info about '" + monsterName + "'. Use your D&D creativity.";
        }
    }

    @Tool("Search for a D&D spell by name (ex: fireball, magic-missile, cure-wounds)")
    public String searchSpell(String spellName) {
        System.out.println("[TOOL CALLED] searchSpell with: " + spellName);
        
        try {
            String cleanName = spellName.toLowerCase()
                    .trim()
                    .replaceAll("\\s+", "-");
            
            String url = API_BASE + "/spells/" + cleanName;
            System.out.println("[TOOL] URL called: " + url);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            
            System.out.println("[TOOL] Status code: " + response.statusCode());
            
            if (response.statusCode() != 200) {
                return "Spell '" + spellName + "' not found.";
            }
            
            JsonNode spell = objectMapper.readTree(response.body());
            
            if (!spell.has("name")) {
                return "Spell '" + spellName + "' not found.";
            }
            
            String result = formatSpellDetails(spell);
            System.out.println("[TOOL] Result OK");
            
            return result;
            
        } catch (Exception e) {
            System.err.println("[TOOL] Error: " + e.getMessage());
            return "Unable to retrieve info about spell '" + spellName + "'.";
        }
    }

    @Tool("Search for a D&D magic item by name (ex: bag-of-holding, vorpal-sword)")
    public String searchMagicItem(String itemName) {
        System.out.println("[TOOL CALLED] searchMagicItem with: " + itemName);
        
        try {
            String cleanName = itemName.toLowerCase()
                    .trim()
                    .replaceAll("\\s+", "-");
            
            String url = API_BASE + "/magic-items/" + cleanName;
            System.out.println("[TOOL] URL called: " + url);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            
            System.out.println("[TOOL] Status code: " + response.statusCode());
            
            if (response.statusCode() != 200) {
                return "Magic item '" + itemName + "' not found.";
            }
            
            JsonNode item = objectMapper.readTree(response.body());
            
            if (!item.has("name")) {
                return "Magic item '" + itemName + "' not found.";
            }
            
            String result = formatMagicItemDetails(item);
            System.out.println("[TOOL] Result OK");
            
            return result;
            
        } catch (Exception e) {
            System.err.println("[TOOL] Error: " + e.getMessage());
            return "Unable to retrieve info about item '" + itemName + "'.";
        }
    }

    private String formatMonsterDetails(JsonNode monster) {
        StringBuilder details = new StringBuilder();
        details.append("D&D MONSTER: ").append(monster.get("name").asText()).append("\n");
        details.append("Type: ").append(monster.get("type").asText()).append("\n");
        details.append("Size: ").append(monster.get("size").asText()).append("\n");
        
        if (monster.has("alignment")) {
            details.append("Alignment: ").append(monster.get("alignment").asText()).append("\n");
        }
        
        details.append("Challenge Rating: ").append(monster.get("challenge_rating").asText()).append("\n");
        details.append("Hit Points: ").append(monster.get("hit_points").asInt()).append("\n");
        
        if (monster.has("armor_class") && monster.get("armor_class").isArray() && monster.get("armor_class").size() > 0) {
            details.append("Armor Class: ").append(monster.get("armor_class").get(0).get("value").asInt()).append("\n");
        }
        
        return details.toString();
    }

    private String formatSpellDetails(JsonNode spell) {
        StringBuilder details = new StringBuilder();
        details.append("D&D SPELL: ").append(spell.get("name").asText()).append("\n");
        details.append("Level: ").append(spell.get("level").asInt()).append("\n");
        details.append("School: ").append(spell.get("school").get("name").asText()).append("\n");
        details.append("Casting Time: ").append(spell.get("casting_time").asText()).append("\n");
        details.append("Range: ").append(spell.get("range").asText()).append("\n");
        
        if (spell.has("desc") && spell.get("desc").isArray() && spell.get("desc").size() > 0) {
            details.append("Description: ");
            String desc = spell.get("desc").get(0).asText();
            details.append(desc.substring(0, Math.min(200, desc.length()))).append("...\n");
        }
        
        return details.toString();
    }

    private String formatMagicItemDetails(JsonNode item) {
        StringBuilder details = new StringBuilder();
        details.append("D&D MAGIC ITEM: ").append(item.get("name").asText()).append("\n");
        
        if (item.has("equipment_category")) {
            details.append("Type: ").append(item.get("equipment_category").get("name").asText()).append("\n");
        }
        
        if (item.has("rarity")) {
            details.append("Rarity: ").append(item.get("rarity").get("name").asText()).append("\n");
        }
        
        if (item.has("desc") && item.get("desc").isArray() && item.get("desc").size() > 0) {
            details.append("Description: ");
            String desc = item.get("desc").get(0).asText();
            details.append(desc.substring(0, Math.min(200, desc.length()))).append("...\n");
        }
        
        return details.toString();
    }
}