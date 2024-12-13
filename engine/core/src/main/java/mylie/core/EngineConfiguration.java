package mylie.core;

import lombok.AccessLevel;
import lombok.Getter;
import mylie.util.configuration.Configuration;
import mylie.util.configuration.ConfigurationFactory;
import mylie.util.configuration.MapBasedConfigurationFactory;

public class EngineConfiguration {
    static final ConfigurationFactory<EngineConfiguration> CONFIGURATION_FACTORY =
            new MapBasedConfigurationFactory<>(EngineConfiguration::config);

    @Getter(AccessLevel.PRIVATE)
    private final Configuration<EngineConfiguration> config = CONFIGURATION_FACTORY.configuration();
}
