package mylie.graphics;

import lombok.Getter;
import mylie.async.Async;
import mylie.async.Functions;

import java.util.ArrayList;
import java.util.List;

public class RenderTask {
    @Getter
    final GraphicsContext context;

    final List<Runnable> subTasks;

    public RenderTask(GraphicsContext context) {
        this.context = context;
        subTasks = new ArrayList<>();
    }

    public void subTask(Runnable task) {
        subTasks.add(task);
    }

    public void submit() {
        Async.async(context.executionMode(), -1, Execute, subTasks);
    }

    private static Functions.F0<Boolean, List<Runnable>> Execute = new Functions.F0<>("Execute Render Task") {
        @Override
        protected Boolean run(List<Runnable> o) {
            for (Runnable runnable : o) {
                runnable.run();
            }
            return true;
        }
    };
}
