package mylie.graphics.managers;

import mylie.graphics.ApiManager;
import mylie.graphics.RenderTask;
import mylie.graphics.geometry.Mesh;
import mylie.graphics.geometry.MeshDataAccess;

public interface MeshManager extends ApiManager, MeshDataAccess {
    void bindMesh(RenderTask renderTask,Mesh mesh);
}
