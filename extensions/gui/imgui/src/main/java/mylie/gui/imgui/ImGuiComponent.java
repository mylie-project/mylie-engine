package mylie.gui.imgui;

import mylie.component.AppComponent;
import mylie.core.Timer;

public interface ImGuiComponent extends AppComponent{

    void renderImGui(Timer.Time time);
}
