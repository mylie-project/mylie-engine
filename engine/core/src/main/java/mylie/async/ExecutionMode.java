package mylie.async;

/**
 * Represents the execution configuration for a task, including the execution
 * mode, target, and caching strategy.
 *
 * The {@code ExecutionMode} record is used to encapsulate the parameters
 * required to execute a task with defined behavior based on the specified mode,
 * target, and cache.
 *
 * @param mode
 *            the {@link Mode} defining the execution strategy (e.g.,
 *            synchronous or asynchronous)
 * @param target
 *            the {@link Target} representing the context in which the execution
 *            should occur
 * @param cache
 *            the {@link Cache} instance associated with the execution, which
 *            handles caching of task results
 */
public record ExecutionMode(Mode mode, Target target, Cache cache) {
	/**
	 * Represents the mode of task execution.
	 *
	 * The `Mode` enumeration defines two modes for executing tasks:
	 *
	 * - `Async`: Indicates that the task should be executed asynchronously. -
	 * `Direct`: Indicates that the task should be executed directly in the current
	 * context.
	 *
	 * Typically used in frameworks or systems where tasks can be scheduled or
	 * executed with varying behaviors based on execution strategy.
	 */
	public enum Mode {
		/**
		 * Indicates that the task should be executed asynchronously.
		 *
		 * This mode is often used in scenarios where tasks are executed in the
		 * background or on a separate thread, allowing the current execution context to
		 * continue without waiting for the task to complete.
		 */
		Async,
		/**
		 * Indicates that the task should be executed directly in the current context.
		 *
		 * This mode ensures that the task runs immediately in the calling thread,
		 * without delegating execution to a background or separate thread. Commonly
		 * used for lightweight tasks or scenarios where immediate execution is
		 * required.
		 */
		Direct
	}
}
