import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HorseTest {
private Horse horse;


@BeforeEach
public void init(){
    horse = new Horse("Name", 100D, 100D);
}

    @Test
    void nullHorse() {
        assertThrows(IllegalArgumentException.class, () -> {new Horse(null, 1, 1);});
        try {
            new Horse(null, 1, 1);
            fail();
        } catch (IllegalArgumentException exception){
            assertEquals("Name cannot be null.", exception.getMessage());
        }

    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void emptyHorse(String arg) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {Horse testHorse = new Horse(arg, 1);});
        assertEquals("Name cannot be blank.", exception.getMessage());
    }

    @Test
    void negativeHorse() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {Horse testHorse = new Horse("Name", -1);});
        assertEquals("Speed cannot be negative.", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> {Horse testHorse = new Horse("Name", -1, 1);});
        assertEquals("Speed cannot be negative.", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> {Horse testHorse = new Horse("Name", 1, -1);});
        assertEquals("Distance cannot be negative.", exception.getMessage());
    }
    @Test
    void getName() throws NoSuchFieldException, IllegalAccessException {
    Field name = Horse.class.getDeclaredField("name");
    name.setAccessible(true);
    String value = (String) name.get(horse);
        assertEquals("Name", value);
    }

    @Test
    void getSpeed() throws IllegalAccessException, NoSuchFieldException {
    Field speed = Horse.class.getDeclaredField("speed");
    speed.setAccessible(true);
    double value = (double) speed.get(horse);
    assertEquals(100D, value);
    }

    @Test
    void getDistance() throws NoSuchFieldException, IllegalAccessException {
        Field distance = Horse.class.getDeclaredField("distance");
        distance.setAccessible(true);
        double value = (double) distance.get(horse);
        assertEquals(100D, value);
        Horse testHorse = new Horse("Name", 100D, 0D);
        value = (double) distance.get(testHorse);
        assertEquals(0D, value);
    }

    @Test
    void move() {
        try (MockedStatic<Horse> mockHorse = Mockito.mockStatic(Horse.class)) {
            Horse testHorse = new Horse("Name", 1,1);
            testHorse.move();
            mockHorse.verify(()-> Horse.getRandomDouble(0.2,0.9));
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9})
    void moveParameterized(double arg) {
        try (MockedStatic<Horse> mockHorse = Mockito.mockStatic(Horse.class)) {
            mockHorse.when(() -> Horse.getRandomDouble(0.2,0.9)).thenReturn(arg);
            horse.move();
            assertEquals(100 + 100*arg, horse.getDistance());
        }
    }
}