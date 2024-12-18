package mylie.util.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import mylie.util.versioned.AutoIncremented;
import org.junit.jupiter.api.Test;

public class PropertiesTest {

	public static class TestObject implements PropertiesAA<TestObject, TestObject.Property<?>> {
		private final Properties<TestObject, Property<?>> properties = new Properties.Map<>(AutoIncremented::new);

		@Override
		public Properties<TestObject, Property<?>> properties() {
			return properties;
		}

		public static class Property<T> extends mylie.util.properties.Property<TestObject, T> {
			public Property(String name) {
				super(name);
			}

			public static Property<String> StringOption = new Property<>("StringOption");
			public static Property<Integer> IntegerOption = new Property<>("IntegerOption");
		}
	}

	@Test
	public void testSetAndGetStringOption() {
		TestObject testObject = new TestObject();
		testObject.properties.property(TestObject.Property.StringOption, "TestValue");
		assertEquals("TestValue", testObject.property(TestObject.Property.StringOption));
	}

	@Test
	public void testSetAndGetIntegerOption() {
		TestObject testObject = new TestObject();
		testObject.properties.property(TestObject.Property.IntegerOption, 42);

		assertEquals(42, testObject.property(TestObject.Property.IntegerOption));
	}

	@Test
	public void testSetNullStringOption() {
		TestObject testObject = new TestObject();
		testObject.properties.property(TestObject.Property.StringOption, null);
		assertNull(testObject.property(TestObject.Property.StringOption));
	}

	@Test
	public void testSetNullIntegerOption() {
		TestObject testObject = new TestObject();
		testObject.properties.property(TestObject.Property.IntegerOption, null);
		assertNull(testObject.property(TestObject.Property.IntegerOption));
	}
}
