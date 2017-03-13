package ioc.test;

import javax.inject.Inject;

public class B {

    @Inject
    public B() {
        System.out.println("b");
    }
}
