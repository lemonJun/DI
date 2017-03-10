package lemon.needle;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import lemon.needle.ioc.AbstractModule;
import lemon.needle.ioc.Provides;

public class BindingModel extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Singleton
    @Provides
    Map maps() {
        return new HashMap();
    }

}
