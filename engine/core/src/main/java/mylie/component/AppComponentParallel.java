package mylie.component;

public non-sealed abstract class AppComponentParallel extends BaseAppComponent implements AppComponent {

	@Override
	protected void runAfter(Class<? extends Component> component) {
		super.runAfter(component);
	}

	@Override
	protected void runBefore(Class<? extends Component> component) {
		super.runBefore(component);
	}
}
