package mylie.util.configurations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import mylie.util.configuration.Configuration;
import mylie.util.configuration.ConfigurationFactory;
import mylie.util.configuration.MapBasedConfigurationFactory;
import mylie.util.configuration.Option;
import org.junit.jupiter.api.Test;

public class ConfigurationTest {

    public static class TestObject {
        private static final ConfigurationFactory<TestObject> Factory =
                new MapBasedConfigurationFactory<>(TestObject::config);

        public interface Options {
            Option<String, TestObject> StringOption = Factory.option();
            Option<Integer, TestObject> IntegerOption = Factory.option();
            Option<Integer, TestObject> IntegerOptionNotNull = Factory.option(1);
        }

        private final Configuration<TestObject> config = Factory.configuration();

        public Configuration<TestObject> config() {
            return config;
        }
    }

    @Test
    void testStringOptionDefault() {
        ConfigurationTest.TestObject testObject = new ConfigurationTest.TestObject();
        assertNull(
                ConfigurationTest.TestObject.Options.StringOption.get(testObject),
                "Default value for StringOption should be null");
    }

    @Test
    void testIntegerOptionDefault() {
        ConfigurationTest.TestObject testObject = new ConfigurationTest.TestObject();
        assertNull(
                ConfigurationTest.TestObject.Options.IntegerOption.get(testObject),
                "Default value for IntegerOption should be null");
    }

    @Test
    void testIntegerOptionNotNullDefault() {
        ConfigurationTest.TestObject testObject = new ConfigurationTest.TestObject();
        assertEquals(
                1,
                ConfigurationTest.TestObject.Options.IntegerOptionNotNull.get(testObject),
                "Default value for IntegerOptionNotNull should be 1");
    }

    @Test
    void testSetStringOption() {
        ConfigurationTest.TestObject testObject = new ConfigurationTest.TestObject();
        ConfigurationTest.TestObject.Options.StringOption.set(testObject, "Hello World");
        assertEquals(
                "Hello World",
                ConfigurationTest.TestObject.Options.StringOption.get(testObject),
                "StringOption should return the value set");
    }

    @Test
    void testSetIntegerOption() {
        ConfigurationTest.TestObject testObject = new ConfigurationTest.TestObject();
        ConfigurationTest.TestObject.Options.IntegerOption.set(testObject, 42);
        assertEquals(
                42,
                ConfigurationTest.TestObject.Options.IntegerOption.get(testObject),
                "IntegerOption should return the value set");
    }

    @Test
    void testOverrideIntegerOptionNotNull() {
        ConfigurationTest.TestObject testObject = new ConfigurationTest.TestObject();
        ConfigurationTest.TestObject.Options.IntegerOptionNotNull.set(testObject, 99);
        assertEquals(
                99,
                ConfigurationTest.TestObject.Options.IntegerOptionNotNull.get(testObject),
                "IntegerOptionNotNull should return the overridden value");
    }

    @Test
    void testMultipleTestObjectsIndependentConfigurations() {
        ConfigurationTest.TestObject testObject1 = new ConfigurationTest.TestObject();
        ConfigurationTest.TestObject testObject2 = new ConfigurationTest.TestObject();

        // Set StringOption for testObject1 and assert changes
        ConfigurationTest.TestObject.Options.StringOption.set(testObject1, "Object1_String");
        assertEquals(
                "Object1_String",
                ConfigurationTest.TestObject.Options.StringOption.get(testObject1),
                "StringOption of testObject1 should return the value set");
        assertNull(
                ConfigurationTest.TestObject.Options.StringOption.get(testObject2),
                "StringOption of testObject2 should remain null");

        // Set IntegerOption for both objects and assert changes
        ConfigurationTest.TestObject.Options.IntegerOption.set(testObject1, 10);
        ConfigurationTest.TestObject.Options.IntegerOption.set(testObject2, 20);
        assertEquals(
                10,
                ConfigurationTest.TestObject.Options.IntegerOption.get(testObject1),
                "IntegerOption of testObject1 should return the value set");
        assertEquals(
                20,
                ConfigurationTest.TestObject.Options.IntegerOption.get(testObject2),
                "IntegerOption of testObject2 should return the value set");

        // Override IntegerOptionNotNull for testObject2 and assert changes
        ConfigurationTest.TestObject.Options.IntegerOptionNotNull.set(testObject2, 200);
        assertEquals(
                1,
                ConfigurationTest.TestObject.Options.IntegerOptionNotNull.get(testObject1),
                "IntegerOptionNotNull of testObject1 should keep its default value");
        assertEquals(
                200,
                ConfigurationTest.TestObject.Options.IntegerOptionNotNull.get(testObject2),
                "IntegerOptionNotNull of testObject2 should reflect the overridden value");
    }
}
