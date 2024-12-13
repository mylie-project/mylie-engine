package mylie.async;

/**
 * The `Functions` class is a utility container for defining custom, abstract
 * function types. It includes templates for zero-parameter (`F0`), one-parameter (`F1`),
 * and two-parameter (`F2`) functions, allowing for extensibility and custom functionality.
 *
 * This class cannot be instantiated as it serves as a collection
 * for nested functional interfaces and abstracts. The nested classes extend a base
 * `Function` class to encapsulate and maintain a name property for each function.
 */
public final class Functions {
    private Functions() {}

    /**
     * Represents a zero-parameter functional interface with a return type.
     * This abstract class allows implementing custom functionality by
     * subclassing and defining the `run` method. It serves as a foundational
     * template for functions that take one input parameter of type `O` and return
     * a result of type `R`.
     *
     * @param <R> The return type of the `run` method.
     * @param <O> The parameter type accepted by the `run` method.
     */
    public abstract static class F0<R, O> extends Function {
        protected F0(String name) {
            super(name);
        }

        protected abstract R run(O object);
    }

    /**
     * Represents a one-parameter functional abstraction extending the base `Function` class.
     * This abstract class is intended to be extended by subclasses to define specific
     * functionality via the `run` method. The `run` method processes a main input object
     * and an additional parameter of a specific type, returning a result of a defined type.
     *
     * Key Characteristics:
     * - Encapsulates the behavior of a function that operates on an object and one parameter.
     * - The `run` method is abstract and must be implemented by subclasses to provide custom logic.
     * - The function instance has an associated name inherited from the `Function` base class.
     *
     * @param <R> The return type of the `run` method.
     * @param <O> The type of the primary input object.
     * @param <P1> The type of the additional parameter.
     */
    public abstract static class F1<R, O, P1> extends Function {
        protected F1(String name) {
            super(name);
        }

        protected abstract R run(O object, P1 p1);
    }

    /**
     * Represents a two-parameter functional abstraction extending the base `Function` class.
     * This abstract class is intended to be extended by subclasses that define specific
     * functionality via the `run` method, which processes a main input and two additional parameters.
     *
     * Key Characteristics:
     * - Encapsulates the behavior of a function that operates on an object and two parameters.
     * - The `run` method is abstract and must be implemented by subclasses to provide custom logic.
     * - The function instance has an associated name inherited from the `Function` base class.
     *
     * @param <R> The return type of the `run` method.
     * @param <O> The type of the primary input object.
     * @param <P1> The type of the first parameter.
     * @param <P2> The type of the second parameter.
     */
    public abstract static class F2<R, O, P1, P2> extends Function {
        protected F2(String name) {
            super(name);
        }

        protected abstract R run(O object, P1 p1, P2 p2);
    }
}
