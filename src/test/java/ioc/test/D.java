package ioc.test;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class D {

    @Inject
    public D(Map maps) {
        System.out.println(maps.size());
    }

}
