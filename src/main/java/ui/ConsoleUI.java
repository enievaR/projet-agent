package ui;

import agent.MasterAgent;
import java.util.Scanner;

public class ConsoleUI {
    private final MasterAgent master = new MasterAgent();

    public void start() {
        System.out.println("------------------------------------------");
        System.out.println("|     Agent Narrateur D&D - v1.0.0       |");
        System.out.println("------------------------------------------\n");
        
        // L'agent démarre l'aventure
        System.out.println("Le narrateur prépare votre aventure...\n");
        String introduction = master.processInput("Démarre une aventure de Donjons & Dragons. Présente le contexte initial et demande au joueur ce qu'il fait.");
        System.out.println(introduction);
        System.out.println("\n------------------------------------------");
        System.out.println("Tapez vos actions ou 'quit' pour quitter");
        System.out.println("------------------------------------------\n");

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.print("Votre action > ");
                if (!sc.hasNextLine()) {
                    System.out.println("\nEntrée terminée. Au revoir !");
                    break;
                }
                String input = sc.nextLine().trim();
                
                if (input.isEmpty()) {
                    continue; // Ignore les lignes vides
                }
                
                if ("quit".equalsIgnoreCase(input)) {
                    System.out.println("\nVotre aventure se termine ici. À bientôt, brave aventurier !");
                    break;
                }
                
                try {
                    System.out.println(); // Ligne vide pour aération
                    String out = master.processInput(input);
                    System.out.println(out);
                    System.out.println(); // Ligne vide après la réponse
                } catch (Exception e) {
                    System.err.println("Erreur : " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
        }
    }
}