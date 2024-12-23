package mylie.graphics.managers;

import mylie.graphics.ApiManager;
import mylie.graphics.RenderTask;
import mylie.graphics.geometry.Mesh;

public interface RenderManager extends ApiManager {
    void render(RenderTask renderTask, Mesh mesh);

}
