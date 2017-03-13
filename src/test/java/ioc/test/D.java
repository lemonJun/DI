package ioc.test;

import javax.inject.Inject;
import javax.inject.Singleton;

public class D {

    @Inject
    private D() {
        System.out.println("d");
    }

}
