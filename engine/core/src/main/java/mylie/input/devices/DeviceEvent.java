package mylie.input.devices;

import lombok.Getter;
import mylie.graphics.GraphicsContext;
import mylie.input.InputEvent;

@Getter
public abstract class DeviceEvent<T extends InputDevice, U> extends InputEvent<U> {
	final T device;
	public DeviceEvent(GraphicsContext context, T device, U value) {
		super(context, value);
		this.device = device;
	}
}
