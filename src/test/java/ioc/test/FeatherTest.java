package ioc.test;

import org.junit.Test;

import lemon.needle.ioc.Feather;

public class FeatherTest {

    @Test
    public void instance() {
        Feather feather = Feather.with();
        A a = feather.instance(A.class);

    }

}
