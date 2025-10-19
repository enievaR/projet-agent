package ui;

import agent.MasterAgent;
import java.util.Scanner;

public class ConsoleUI {
    private final MasterAgent master = new MasterAgent();

    public void start() {
        System.out.println("Bienvenue — agent RPG. Tape 'quit' pour sortir.");

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                if (!sc.hasNextLine()) {
                    System.out.println("Entrée fermée (aucune saisie détectée). Fin du programme.");
                    break;
                }
                String input = sc.nextLine();
                if ("quit".equalsIgnoreCase(input.trim())) {
                    System.out.println("Au revoir !");
                    break;
                }
                try {
                    String out = master.processInput(input);
                    System.out.println(out);
                } catch (Exception e) {
                    System.err.println("Erreur : " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
        }
    }
}
