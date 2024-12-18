package mylie.util.properties;

import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.Getter;
import mylie.util.versioned.Versioned;

public abstract class Properties<TARGET, OPTION extends Property<TARGET, ?>> {
	@Getter(AccessLevel.PACKAGE)
	private final Supplier<Versioned<?>> versionedSupplier;

	protected Properties(Supplier<Versioned<?>> versionedSupplier) {
		this.versionedSupplier = versionedSupplier;
	}

	public abstract <T> void property(Property<TARGET, T> option, T value);

	protected abstract <T> T property(Property<TARGET, T> option);

	protected abstract <T> Versioned.Reference<T> reference(Property<TARGET, T> option);

	public static class Map<TARGET, OPTION extends Property<TARGET, ?>> extends Properties<TARGET, OPTION> {
		private final java.util.Map<Property<TARGET, ?>, Versioned<?>> dataStore = new java.util.HashMap<>();

		public Map(Supplier<Versioned<?>> versionedSupplier) {
			super(versionedSupplier);
		}

		@Override
		public <T> void property(Property<TARGET, T> option, T value) {
			versioned(option).value(value);
		}

		@Override
		protected <T> T property(Property<TARGET, T> option) {
			Versioned<T> versioned = versioned(option);
			return versioned.value();
		}

		@Override
		protected <T> Versioned.Reference<T> reference(Property<TARGET, T> option) {
			Versioned<T> versioned = versioned(option);
			return versioned.reference();
		}

		@SuppressWarnings("unchecked")
		private <T> Versioned<T> versioned(Property<TARGET, T> option) {
			return (Versioned<T>) dataStore.computeIfAbsent(option, targetProperty -> versionedSupplier().get());
		}
	}
}
