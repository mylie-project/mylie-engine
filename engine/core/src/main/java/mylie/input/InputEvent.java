package mylie.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mylie.graphics.GraphicsContext;

@AllArgsConstructor
@Getter
public abstract class InputEvent {
	final GraphicsContext context;
}
