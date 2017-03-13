package ioc.test;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import lemon.needle.ioc.annotations.Provides;

public class MyModule {

    @Provides
    @Singleton // an app will probably need a single instance 
    List<String> ss() {
        List dataSource = new ArrayList();// instantiate some DataSource
        return dataSource;
    }

    @Provides
    @Singleton // an app will probably need a single instance 
    List<Integer> is() {
        List dataSource = new ArrayList();// instantiate some DataSource
        return dataSource;
    }
}
