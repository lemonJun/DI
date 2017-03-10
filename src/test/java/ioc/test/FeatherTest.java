package ioc.test;

import org.codejargon.feather.Feather;
import org.junit.Test;

public class FeatherTest {

    @Test
    public void instance() {
        Feather feather = Feather.with();
        A a = feather.instance(A.class);

    }

}
