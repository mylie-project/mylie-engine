package mylie.graphics;

import java.util.List;

public abstract class ContextProvider {
    public abstract List<Display> displays();

    public abstract Display display();
}
