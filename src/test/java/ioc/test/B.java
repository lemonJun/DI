package ioc.test;

import javax.inject.Inject;

public class B {

    @Inject
    public B(A c, D b) {
        System.out.println("b");
    }
}
