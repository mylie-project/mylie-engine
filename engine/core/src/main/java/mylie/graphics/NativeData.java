package mylie.graphics;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class NativeData {

    @Getter
    @Setter
    public abstract static class Handle {
        long version;
        public abstract int handle();
        public abstract void handle(int handle);
    }

    @Slf4j
    public static class NonSharedData<T> {
        private final Map<GraphicsContext, Map<T, Handle>> data;

        public NonSharedData() {
            data = new ConcurrentHashMap<>();
        }

        public Handle get(GraphicsContext context, T key) {
            Map<T, Handle> thMap = data.computeIfAbsent(context, k -> new ConcurrentHashMap<>());
            return thMap.get(key);
        }

        public void put(GraphicsContext context, T key, Handle value) {
            Map<T, Handle> thMap = data.computeIfAbsent(context, k -> new WeakHashMap<>());
            thMap.put(key, value);
        }

        public static class Handle extends NativeData.Handle{
            int handle;

            @Override
            public int handle() {
                return handle;
            }

            @Override
            public void handle(int handle) {
                this.handle=handle;
            }
        }
    }

    public static class SharedData<T> {
        private final Map<T, Handle> data;

        public SharedData() {
            data=new WeakHashMap<>();
        }

        public Handle get(T key) {
            return data.get(key);
        }

        public void put(T key, Handle value) {
            data.put(key, value);
        }

        public static class Handle extends NativeData.Handle{
            CompletableFuture<Integer> handle;

            public Handle() {
                handle = new CompletableFuture<>();
            }

            @Override
            public int handle() {
                try {
                    return handle.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void handle(int handle) {
                this.handle.complete(handle);
            }
        }
    }
}
