package gr.ds.unipi.processing;

import java.io.Serializable;

public interface Function3<A, B, D> extends Serializable {
    public D apply(A a, B b);
}
