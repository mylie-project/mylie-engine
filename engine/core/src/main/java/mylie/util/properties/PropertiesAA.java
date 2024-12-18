package mylie.util.properties;

import mylie.util.configuration.Configurations;
import mylie.util.configuration.Option;
import mylie.util.versioned.Versioned;

public interface PropertiesAA<TARGET,OPTION extends Property<TARGET,?>>{
    default <T> T property(Property<TARGET,T> option){
        return properties().property(option);
    }

    default <T>Versioned.Reference<T> propertyReference(Property<TARGET,T> option){
        return properties().reference(option);
    }

    Properties<TARGET,OPTION> properties();
}
