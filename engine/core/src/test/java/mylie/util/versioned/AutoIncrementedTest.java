package mylie.util.versioned;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests for the method `value()` of the `AutoIncremented` class,
 * which gets the value of the `AutoIncremented` object and increments the version
 * number if a new value is provided.
 */
public class AutoIncrementedTest {

    @Test
    public void whenValueCalledWithoutArgument_thenSameObjectIsReturned() {
        AutoIncremented<String> autoIncrement = new AutoIncremented<String>("initial");
        assertEquals("initial", autoIncrement.value());
    }

    @Test
    public void whenNewValueProvided_thenObjectIsUpdatedAndVersionIsIncremented() {
        AutoIncremented<String> autoIncrement = new AutoIncremented<>("initial");

        autoIncrement.value("updated");

        assertEquals("updated", autoIncrement.value());

        long versionAfterUpdate = autoIncrement.version();

        assertEquals(1, versionAfterUpdate);
    }

    @Test
    public void whenSameValueIsProvided_thenObjectRemainsUnchangedAndVersionIsNotIncremented() {
        AutoIncremented<String> autoIncrement = new AutoIncremented<>("initial");

        autoIncrement.value("initial");

        assertEquals("initial", autoIncrement.value());

        long versionAfterUpdate = autoIncrement.version();

        assertEquals(0, versionAfterUpdate);
    }

    @Test
    public void whenSameValueIsProvidedButUpdateIfEqualIsTrue_thenVersionIsIncremented() {
        AutoIncremented<String> autoIncrement = new AutoIncremented<>("initial", true);

        autoIncrement.value("initial");

        assertEquals("initial", autoIncrement.value());

        long versionAfterUpdate = autoIncrement.version();

        assertEquals(1, versionAfterUpdate);
    }

    @Test
    public void whenNullValueIsProvided_thenObjectIsUpdatedAndVersionIsIncremented() {
        AutoIncremented<String> autoIncrement = new AutoIncremented<>("initial");

        autoIncrement.value(null);

        assertNull(autoIncrement.value());

        long versionAfterUpdate = autoIncrement.version();

        assertEquals(1, versionAfterUpdate);
    }
}