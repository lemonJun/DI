package ioc.test;

import javax.inject.Inject;

public class A {

    @Inject
    public A(B b) {
        System.out.println("a");
    }
}
