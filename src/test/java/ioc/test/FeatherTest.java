package ioc.test;

import org.junit.Test;

import lemon.needle.ioc.Injector;

public class FeatherTest {

    @Test
    public void instance() {
        Injector feather = Injector.with();
        A a = feather.instance(A.class);

    }

}
