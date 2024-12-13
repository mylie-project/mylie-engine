package mylie.util.versioned;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * This class contains tests for the method `value()` of the `AutoIncremented` class,
 * which gets the value of the `AutoIncremented` object and increments the version
 * number if a new value is provided.
 */
public class VersionedTest {

    @Test
    public void whenValueCalledWithoutArgument_thenSameObjectIsReturned() {
        AutoIncremented<String> autoIncrement = new AutoIncremented<>("initial");
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

    @Test
    public void whenReferenceIsCreated_thenValueAndVersionMatchParent() {
        AutoIncremented<String> autoIncrement = new AutoIncremented<>("initial");
        Versioned.Reference<String> reference = autoIncrement.reference();

        assertEquals("initial", reference.value());
        assertEquals(autoIncrement.version(), reference.version());
    }

    @Test
    public void whenParentValueChanges_thenReferenceDetectsChangeAndUpdates() {
        AutoIncremented<String> autoIncrement = new AutoIncremented<>("initial");
        Versioned.Reference<String> reference = autoIncrement.reference();

        autoIncrement.value("updated");

        assertTrue(reference.changed());
        assertEquals("updated", reference.value());
        assertEquals(autoIncrement.version(), reference.version());
    }

    @Test
    public void whenParentValueDoesNotChange_thenReferenceDetectsNoChange() {
        AutoIncremented<String> autoIncrement = new AutoIncremented<>("initial");
        Versioned.Reference<String> reference = autoIncrement.reference();

        autoIncrement.value("initial");

        assertFalse(reference.changed());
        assertEquals("initial", reference.value());
        assertEquals(autoIncrement.version(), reference.version());
    }

    @Test
    public void whenReferenceRechecksParent_thenVersionAndValueSynchronize() {
        AutoIncremented<String> autoIncrement = new AutoIncremented<>("initial");
        Versioned.Reference<String> reference = autoIncrement.reference();

        autoIncrement.value("updated");
        reference.value();

        assertFalse(reference.changed());
        assertEquals("updated", reference.value());
        assertEquals(autoIncrement.version(), reference.version());
    }

    @Test
    public void whenParentValueIsNull_thenReferenceUpdatesToNull() {
        AutoIncremented<String> autoIncrement = new AutoIncremented<>("initial");
        Versioned.Reference<String> reference = autoIncrement.reference();

        autoIncrement.value(null);

        assertTrue(reference.changed());
        assertNull(reference.value());
        assertEquals(autoIncrement.version(), reference.version());
    }
}
