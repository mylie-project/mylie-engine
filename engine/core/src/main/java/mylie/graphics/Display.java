package mylie.graphics;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import mylie.math.Vector2ic;
import mylie.math.Vector4ic;

@AllArgsConstructor
@Getter
public class Display {
    private boolean primary;
    private VideoMode videoMode;
    private final List<VideoMode> videoModes = new ArrayList<>();

    @AllArgsConstructor
    @Getter
    public static class VideoMode {
        final Vector2ic resolution;
        final int refreshRate;
        final Vector4ic format;

        @Override
        public String toString() {
            return "VideoMode{" +
                    "resolution=" + resolution +
                    ", refreshRate=" + refreshRate +
                    ", format=" + format +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Display{" +
                "primary=" + primary +
                ", videoMode=" + videoMode +
                '}';
    }
}
