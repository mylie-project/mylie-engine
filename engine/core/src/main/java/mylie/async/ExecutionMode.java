package mylie.async;

public record ExecutionMode(Mode mode, Target target, Cache cache) {
    public enum Mode {
        Async,
        Direct
    }
}
