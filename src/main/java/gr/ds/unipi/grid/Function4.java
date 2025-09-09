package gr.ds.unipi.grid;

import java.io.Serializable;

public interface Function4<A, B, C, D> extends Serializable {
    public D apply(A a, B b, C c);
}
