package mylie.graphics;

import mylie.util.configuration.Configurations;
import mylie.util.configuration.Option;

import java.util.ArrayList;
import java.util.List;

public class GraphicsContextConfiguration extends Configurations.Map<GraphicsContext, GraphicsContext.Option<?>> {
    private GraphicsContext context;

    @Override
    public  <T> void option(Option<GraphicsContext, T> option, T value) {
        boolean changed=false;
        T oldValue = option(option);
        if(oldValue!=null && oldValue.equals(value)){
            changed=true;
        }
        super.option(option, value);
        if (context != null && changed) {
            context.onOptionChanged(option, value);
        }
    }

    @Override
    public <T> T option(Option<GraphicsContext, T> option) {
        return super.option(option);
    }


    @SuppressWarnings("unchecked")
    public Iterable<? extends GraphicsContext.Option<?>> getOptions() {
        List<GraphicsContext.Option<?>> list=new ArrayList<>();
        for (Option<GraphicsContext, ?> option : super.options()) {
            list.add((GraphicsContext.Option<?>) option);
        }
        return list;
    }
}