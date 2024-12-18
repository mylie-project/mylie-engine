package mylie.graphics;

import java.util.List;
import mylie.component.AppComponent;

public class GraphicsManager implements AppComponent {
    private final GraphicsModule graphicsModule;

    public GraphicsManager(GraphicsModule graphicsModule) {
        this.graphicsModule = graphicsModule;
    }

    public List<Display> displays() {
        return graphicsModule.api().contextProvider().displays();
    }

    public Display display() {
        return graphicsModule.api().contextProvider().display();
    }

    public GraphicsContext createContext(GraphicsContextConfiguration configuration, boolean synced) {
        return graphicsModule.createContext(configuration,synced);
    }
}
