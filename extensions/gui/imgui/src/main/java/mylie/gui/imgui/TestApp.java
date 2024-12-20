package mylie.gui.imgui;

import imgui.ImGui;
import imgui.app.Application;

public class TestApp extends Application {
    @Override
    public void process() {
        ImGui.showDemoWindow();
    }

    public static void main(String[] args) {
        new Thread(() -> {
                try {
                    launch(new TestApp());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }).start();

    }
}
