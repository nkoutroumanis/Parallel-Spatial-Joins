package gr.ds.unipi.grid;

public interface TriFunction<T, U, V, W> {
    public W apply(T t, U u, V v);
}
