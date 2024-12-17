package mylie.async;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ManagedThreadThreading implements ManagedThread {
    private final Target target;
    private final BlockingQueue<Runnable> queue;
    private final Thread thread;
    private final List<ManagedThread> managedThreadList;
    private boolean running;

    public ManagedThreadThreading(Target target, BlockingQueue<Runnable> queue, List<ManagedThread> managedThreadList) {
        this.target = target;
        this.queue = queue;
        this.thread = new Thread(this::loop);
        this.thread.setName(target.name());
        this.managedThreadList = managedThreadList;
    }

    public void loop() {
        running = true;
        Async.registerThread(target);
        while (running) {
            try {
                Runnable runnable = queue.poll(10, TimeUnit.MILLISECONDS);
                if (runnable != null) {
                    runnable.run();
                }
            } catch (InterruptedException e) {
                log.error("Error while executing tasks: {}", e.getMessage(), e);
            }
        }
        managedThreadList.remove(this);
    }

    void start() {
        thread.start();
    }

    void stop() {
        CountDownLatch latch = new CountDownLatch(1);
        queue.add(() -> {
            running = false;
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("Error while stopping thread: {}", e.getMessage(), e);
        }
    }
}
