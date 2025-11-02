/**
 * Main application entry point.
 * Initializes and starts the console user interface.
 * author: Florian Mordohai with LLM assistance
 */

package app;

import ui.ConsoleUI;
/**
 * The App class serves as the main entry point for the application.
 * It initializes and starts the console-based user interface.
 */
public class App {
        /**
         * The main method that launches the application.
         * It creates an instance of the ConsoleUI class and starts the user interface.
         *
         * @param args Command-line arguments passed to the application (not used).
         */
        public static void main(String[] args) {
            ConsoleUI ui = new ConsoleUI(); // Initialize the console user interface
            ui.start();
        }
}

