package mylie.util.configuration;

public interface Configurable<TARGET, OPTION extends Option<TARGET, ?>> {

	default <T> void option(Option<TARGET, T> option, T value) {
		configuration().option(option, value);
	}

	default <T> T option(Option<TARGET, T> option) {
		return configuration().option(option);
	}

	Configurations<TARGET, OPTION> configuration();
}
