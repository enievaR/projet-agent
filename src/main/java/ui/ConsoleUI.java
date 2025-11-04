/**
 * ConsoleUI.java
 * User interface for the D&D narrator agent.
 * author: Florian Mordohai with LLM assistance
 */
package ui;

import agent.MasterAgent;
import java.util.Scanner;

/**
 * Provides a console-based interface for interacting with the MasterAgent,
 * allowing users to input actions and receive responses in a Dungeons & Dragons adventure.
 *
 */
public class ConsoleUI {
    private final MasterAgent master = new MasterAgent(); // Principal agent

    /**
     * Start the console user interface.
     */
    public void start() {
        System.out.println("------------------------------------------");
        System.out.println("|     Narrator agent D&D - v1.0.0       |");
        System.out.println("------------------------------------------\n");
        
        // Start the adventure
        System.out.println("The narrator prepares your adventure...\n");
        String introduction = master.processInput("Start a Dungeons & Dragons adventure. Present the initial context and ask the player what they do."); //Initial prompt
        System.out.println(introduction);
        System.out.println("\n------------------------------------------");
        System.out.println("Type your actions or 'quit' to exit");
        System.out.println("------------------------------------------\n");

        // User input loop
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.print("Your action > ");
                if (!sc.hasNextLine()) {
                    System.out.println("\nInput ended. Goodbye!");
                    break;
                }
                String input = sc.nextLine().trim(); // read user input and trim whitespace
                
                if (input.isEmpty()) {
                    continue; // Ignore empty lines
                }
                
                if ("quit".equalsIgnoreCase(input)) {
                    System.out.println("\nYour adventure ends here. See you soon, brave adventurer!");
                    break;
                }
                
                try {
                    System.out.println(); // Blank line for readability
                    String out = master.processInput(input); // Process user input
                    System.out.println(out); // Display the response
                    System.out.println(); 
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}