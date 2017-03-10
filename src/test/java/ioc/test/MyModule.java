package ioc.test;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import lemon.needle.ioc.annotations.Provides;

public class MyModule {

    @Provides
    @Singleton // an app will probably need a single instance 
    List ds() {
        List dataSource = new ArrayList();// instantiate some DataSource
        return dataSource;
    }
}
