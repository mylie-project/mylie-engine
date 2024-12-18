package mylie.lwjgl3.glfw;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import mylie.graphics.Display;

@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
public class GlfwDisplay extends Display {
    private final long handle;

    public GlfwDisplay(long handle, boolean primary, VideoMode videoMode) {
        super(primary, videoMode);
        this.handle = handle;
    }
}
