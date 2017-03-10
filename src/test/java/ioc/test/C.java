package ioc.test;

import javax.inject.Singleton;

@Singleton
public class C {

    private C() {
        System.out.println("c");
    }
}
