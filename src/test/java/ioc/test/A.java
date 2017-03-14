package ioc.test;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class A {

    @Inject
    B b;

    @Inject
    public A() {
        System.out.println("a");
    }
}
