package mylie.graphics;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

public class NativeData {

	@Getter
	@Setter
	public abstract static class Handle {
		long version = -1;
		public abstract int handle();
		public abstract void handle(int handle);
	}

	@Slf4j
	public static class NonSharedData<T, H extends Handle> {
		private final Map<GraphicsContext, Map<T, H>> data;
		private final Supplier<H> supplier;
		public NonSharedData(Supplier<H> supplier) {
			this.supplier = supplier;
			data = new ConcurrentHashMap<>();
		}

		public H get(GraphicsContext context, T key) {
			Map<T, H> thMap = data.computeIfAbsent(context, k -> new ConcurrentHashMap<>());
			return thMap.computeIfAbsent(key, _ -> supplier.get());
		}

		public void put(GraphicsContext context, T key, H value) {
			Map<T, H> thMap = data.computeIfAbsent(context, k -> new WeakHashMap<>());
			thMap.put(key, value);
		}

		public static class Handle extends NativeData.Handle {
			int handle = -1;

			@Override
			public int handle() {
				return handle;
			}

			@Override
			public void handle(int handle) {
				this.handle = handle;
			}
		}
	}

	public static class SharedData<T, H extends Handle> {
		private final Map<T, H> data;
		private final Supplier<H> supplier;
		public SharedData(Supplier<H> supplier) {
			this.supplier = supplier;
			data = new WeakHashMap<>();
		}

		public H get(T key) {
			return data.computeIfAbsent(key, _ -> supplier.get());
		}

		public void put(T key, H value) {
			data.put(key, value);
		}

		public static class Handle extends NativeData.Handle {
			CompletableFuture<Integer> handle;
			@Getter
			@Setter
			boolean initialized = false;
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
