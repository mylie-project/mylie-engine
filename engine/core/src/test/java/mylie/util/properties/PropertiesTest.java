package mylie.util.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import mylie.util.versioned.AutoIncremented;
import org.junit.jupiter.api.Test;

public class PropertiesTest {

    public static class TestObject {
        private static final PropertiesFactory<TestObject> Factory =
                new MapBasedPropertiesFactory<>(TestObject::config, AutoIncremented::new);

        public interface Options {
            Property<String, TestObject> StringOption = Factory.property();
            Property<Integer, TestObject> IntegerOption = Factory.property();
        }

        private final Properties<TestObject> config = Factory.properties();

        public Properties<TestObject> config() {
            return config;
        }
    }

    @Test
    public void testSetAndGetStringOption() {
        TestObject testObject = new TestObject();
        TestObject.Options.StringOption.set(testObject, "TestValue");
        assertEquals(
                "TestValue", TestObject.Options.StringOption.get(testObject).value());
    }

    @Test
    public void testSetAndGetIntegerOption() {
        TestObject testObject = new TestObject();
        TestObject.Options.IntegerOption.set(testObject, 42);
        assertEquals(42, TestObject.Options.IntegerOption.get(testObject).value());
    }

    @Test
    public void testSetNullStringOption() {
        TestObject testObject = new TestObject();
        TestObject.Options.StringOption.set(testObject, null);
        assertNull(TestObject.Options.StringOption.get(testObject).value());
    }

    @Test
    public void testSetNullIntegerOption() {
        TestObject testObject = new TestObject();
        TestObject.Options.IntegerOption.set(testObject, null);
        assertNull(TestObject.Options.IntegerOption.get(testObject).value());
    }

    @Test
    public void testUpdateVersionOnIntegerOptionChange() {
        TestObject testObject = new TestObject();
        TestObject.Options.IntegerOption.set(testObject, 10);
        long versionBefore = TestObject.Options.IntegerOption.get(testObject).version();
        TestObject.Options.IntegerOption.set(testObject, 20);
        long versionAfter = TestObject.Options.IntegerOption.get(testObject).version();
        assertEquals(versionBefore + 1, versionAfter);
    }

    @Test
    public void testDoNotUpdateVersionOnSameIntegerValue() {
        TestObject testObject = new TestObject();
        TestObject.Options.IntegerOption.set(testObject, 15);
        long versionBefore = TestObject.Options.IntegerOption.get(testObject).version();
        TestObject.Options.IntegerOption.set(testObject, 15); // Same value, should not change version
        long versionAfter = TestObject.Options.IntegerOption.get(testObject).version();
        assertEquals(versionBefore, versionAfter);
    }

    @Test
    public void testUpdateVersionOnNullStringValue() {
        TestObject testObject = new TestObject();
        TestObject.Options.StringOption.set(testObject, null);
        long versionBefore = TestObject.Options.StringOption.get(testObject).version();
        TestObject.Options.StringOption.set(testObject, "NewValue");
        long versionAfter = TestObject.Options.StringOption.get(testObject).version();
        assertEquals(versionBefore + 1, versionAfter);
    }

    @Test
    public void testStringOptionRetainsValue() {
        TestObject testObject = new TestObject();
        TestObject.Options.StringOption.set(testObject, "FirstValue");
        assertEquals(
                "FirstValue", TestObject.Options.StringOption.get(testObject).value());
        TestObject.Options.StringOption.set(testObject, "SecondValue");
        assertEquals(
                "SecondValue", TestObject.Options.StringOption.get(testObject).value());
    }

    @Test
    public void testVersionIncrementsOnNullToNonNullChange() {
        TestObject testObject = new TestObject();
        TestObject.Options.IntegerOption.set(testObject, null);
        long versionBefore = TestObject.Options.IntegerOption.get(testObject).version();
        TestObject.Options.IntegerOption.set(testObject, 123);
        long versionAfter = TestObject.Options.IntegerOption.get(testObject).version();
        assertEquals(versionBefore + 1, versionAfter);
    }

    @Test
    public void testVersionDoesNotIncrementForSameNullValue() {
        TestObject testObject = new TestObject();
        TestObject.Options.StringOption.set(testObject, null);
        long versionBefore = TestObject.Options.StringOption.get(testObject).version();
        TestObject.Options.StringOption.set(testObject, null); // Same null value, version should not change
        long versionAfter = TestObject.Options.StringOption.get(testObject).version();
        assertEquals(versionBefore, versionAfter);
    }
}
