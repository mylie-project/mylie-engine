package mylie.util.configurations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import mylie.util.configuration.Configurable;
import mylie.util.configuration.Configurations;
import org.junit.jupiter.api.Test;

public class OptionsTest {

    public static class TestObject implements Configurable<TestObject, TestObject.Option<?>> {
        private final Configurations<TestObject, Option<?>> configurations = new Configurations.Map<>();

        @Override
        public Configurations<TestObject, Option<?>> configuration() {
            return configurations;
        }

        public static class Option<T> extends mylie.util.configuration.Option<TestObject, T> {
            public Option(String name, T defaultValue) {
                super(name, defaultValue);
            }

            public static final Option<String> StringOption = new Option<>("StringOption", null);
            public static final Option<Integer> IntegerOption = new Option<>("IntegerOption", null);
            public static final Option<Integer> IntegerOptionNotNull = new Option<>("IntegerOptionNotNull", 1);
        }
    }

    @Test
    void testStringOptionDefault() {
        OptionsTest.TestObject testObject = new OptionsTest.TestObject();
        assertNull(testObject.option(TestObject.Option.StringOption), "Default value for StringOption should be null");
    }

    @Test
    void testIntegerOptionDefault() {
        OptionsTest.TestObject testObject = new OptionsTest.TestObject();
        assertNull(
                testObject.option(TestObject.Option.IntegerOption), "Default value for IntegerOption should be null");
    }

    @Test
    void testIntegerOptionNotNullDefault() {
        OptionsTest.TestObject testObject = new OptionsTest.TestObject();
        assertEquals(
                1,
                testObject.option(TestObject.Option.IntegerOptionNotNull),
                "Default value for IntegerOptionNotNull should be 1");
    }

    @Test
    void testSetStringOption() {
        OptionsTest.TestObject testObject = new OptionsTest.TestObject();
        testObject.option(TestObject.Option.StringOption, "Hello World");
        assertEquals(
                "Hello World",
                testObject.option(TestObject.Option.StringOption),
                "StringOption should return the value set");
    }

    @Test
    void testSetIntegerOption() {
        OptionsTest.TestObject testObject = new OptionsTest.TestObject();
        testObject.option(TestObject.Option.IntegerOption, 42);
        assertEquals(
                42, testObject.option(TestObject.Option.IntegerOption), "IntegerOption should return the value set");
    }

    @Test
    void testOverrideIntegerOptionNotNull() {
        OptionsTest.TestObject testObject = new OptionsTest.TestObject();
        testObject.option(TestObject.Option.IntegerOptionNotNull, 99);
        assertEquals(
                99,
                testObject.option(TestObject.Option.IntegerOptionNotNull),
                "IntegerOptionNotNull should return the overridden value");
    }

    @Test
    void testMultipleTestObjectsIndependentConfigurations() {
        OptionsTest.TestObject testObject1 = new OptionsTest.TestObject();
        OptionsTest.TestObject testObject2 = new OptionsTest.TestObject();

        // Set StringOption for testObject1 and assert changes
        testObject1.option(TestObject.Option.StringOption, "Object1_String");
        assertEquals(
                "Object1_String",
                testObject1.option(TestObject.Option.StringOption),
                "StringOption of testObject1 should return the value set");
        assertNull(
                testObject2.option(TestObject.Option.StringOption), "StringOption of testObject2 should remain null");

        // Set IntegerOption for both objects and assert changes
        testObject1.option(TestObject.Option.IntegerOption, 10);
        testObject2.option(TestObject.Option.IntegerOption, 20);
        assertEquals(
                10,
                testObject1.option(TestObject.Option.IntegerOption),
                "IntegerOption of testObject1 should return the value set");
        assertEquals(
                20,
                testObject2.option(TestObject.Option.IntegerOption),
                "IntegerOption of testObject2 should return the value set");

        // Override IntegerOptionNotNull for testObject2 and assert changes
        testObject2.option(TestObject.Option.IntegerOptionNotNull, 200);
        assertEquals(
                1,
                testObject1.option(TestObject.Option.IntegerOptionNotNull),
                "IntegerOptionNotNull of testObject1 should keep its default value");
        assertEquals(
                200,
                testObject2.option(TestObject.Option.IntegerOptionNotNull),
                "IntegerOptionNotNull of testObject2 should reflect the overridden value");
    }
}
