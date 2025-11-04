package app;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import ui.ConsoleUI;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void mainInitializesAndStartsConsoleUIWithMockedConstruction() {
        try (MockedConstruction<ConsoleUI> mockedConstruction = Mockito.mockConstruction(ConsoleUI.class)) {
            App.main(new String[]{});
            assertEquals(1, mockedConstruction.constructed().size());
            ConsoleUI constructedInstance = mockedConstruction.constructed().get(0);
            Mockito.verify(constructedInstance, Mockito.times(1)).start();
        }
    }


}