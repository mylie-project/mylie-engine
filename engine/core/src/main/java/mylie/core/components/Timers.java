package mylie.core.components;

import java.util.concurrent.TimeUnit;
import mylie.core.Timer;

public final class Timers {
	static final double NANOSECONDS_IN_SECOND = TimeUnit.SECONDS.toNanos(1);

	public static final class NanoTimer extends Timer {
		long startTime, endTime;

		public NanoTimer(Timer.Settings settings) {
			super(settings);
			endTime = System.nanoTime();
		}

		@Override
		protected Time getTime(long version) {
			startTime = endTime;
			endTime = System.nanoTime();
			NanoTime time = new NanoTime();
			time.version(version);
			time.delta((endTime - startTime) / NANOSECONDS_IN_SECOND);
			time.deltaMod(time.delta() * settings().timeModifier());
			return time;
		}

		static class NanoTime extends Time {
			@Override
			protected Time version(long version) {
				return super.version(version);
			}

			@Override
			protected Time delta(double delta) {
				return super.delta(delta);
			}

			@Override
			protected Time deltaMod(double deltaMod) {
				return super.deltaMod(deltaMod);
			}
		}

		public static class Settings extends Timer.Settings {
			@Override
			protected Timer build() {
				return new NanoTimer(this);
			}
		}
	}
}
