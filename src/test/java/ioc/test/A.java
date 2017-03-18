package ioc.test;

import javax.inject.Inject;

public class A {

    @Inject
    B b;

    @Inject
    public A() {
        System.out.println("a");
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

}
