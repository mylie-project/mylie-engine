package mylie.async;

public record Target(String name) {
	public static final Target Background = new Target("Background");
}
