package app;

import ui.ConsoleUi;

/**
 * The App class serves as the main entry point for the application.
 * It initializes and starts the console-based user interface.
 */
public class App {

    /**
     * The main method that launches the application.
     * It creates an instance of the ConsoleUi class and starts the user interface.
     *
     * @param args Command-line arguments passed to the application (not used).
     */
    public static void main(String[] args) {
        ConsoleUi ui = new ConsoleUi(); // Initialize the console user interface
        ui.start();
    }
}
