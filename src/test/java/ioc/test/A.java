package ioc.test;

import javax.inject.Inject;

public class A {

    @Inject
    B b;

    @Inject
    public A() {
        System.out.println("a");
    }
}
