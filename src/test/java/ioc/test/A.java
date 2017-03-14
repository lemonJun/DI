package ioc.test;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class A {

    //    @Inject
    //    B b;

    @Inject
    public A(B b) {
        System.out.println("a");
    }
}
