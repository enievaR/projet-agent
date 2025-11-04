package app;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import ui.ConsoleUi;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void mainInitializesAndStartsConsoleUIWithMockedConstruction() {
        try (MockedConstruction<ConsoleUi> mockedConstruction = Mockito.mockConstruction(ConsoleUi.class)) {
            App.main(new String[]{});
            assertEquals(1, mockedConstruction.constructed().size());
            ConsoleUi constructedInstance = mockedConstruction.constructed().get(0);
            Mockito.verify(constructedInstance, Mockito.times(1)).start();
        }
    }


}