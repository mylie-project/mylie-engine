package mylie.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mylie.util.configuration.Configurations;
import mylie.util.configuration.Option;
@Slf4j
@Setter
public class GraphicsContextConfiguration extends Configurations.Map<GraphicsContext, GraphicsContext.Option<?>> {
	@Setter(AccessLevel.PACKAGE)
	private GraphicsContext context;

	@Override
	public <T> void option(Option<GraphicsContext, T> option, T value) {

		boolean changed = false;
		T oldValue = option(option);
		if(!Objects.equals(oldValue, value)) {
			changed = true;
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

	public Iterable<? extends GraphicsContext.Option<?>> getOptions() {
		List<GraphicsContext.Option<?>> list = new ArrayList<>();
		for (Option<GraphicsContext, ?> option : super.options()) {
			list.add((GraphicsContext.Option<?>) option);
		}
		return list;
	}
}
