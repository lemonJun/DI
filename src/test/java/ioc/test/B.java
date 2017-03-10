package ioc.test;

import javax.inject.Inject;

public class B {

    @Inject
    public B(C c, D b) {
        System.out.println("b");
    }
}
