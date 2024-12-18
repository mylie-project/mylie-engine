package mylie.input.devices;

public interface EventType {
	interface DigitalEvent {
		boolean pressed();
	}

	interface AnalogEvent {
		float value();
	}

	interface ValueEvent<T> {
		T value();
	}
}
