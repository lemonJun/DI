package ioc.test;

import javax.inject.Inject;
import javax.inject.Singleton;

public class B {

    @Inject
    public B() {
        System.out.println("b");
    }
}
