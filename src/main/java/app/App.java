/**
 * Main application entry point.
 * Initializes and starts the console user interface.
 * author: Florian Mordohai with LLM assistance
 */

package app;

import ui.ConsoleUI;
public class App {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI(); // Initialize the console user interface
        ui.start();
    }
}

