package lemon.needle;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import ioc.test.ImplSup;
import ioc.test.Sup;
import lemon.needle.ioc.AbstractModule;
import lemon.needle.ioc.annotations.Provides;

public class BindingModel extends AbstractModule {

    @Override
    protected void configure() {
        bind(Sup.class).to(ImplSup.class);
    }

    @Singleton
    @Provides
    Map maps() {
        Map map = new HashMap();
        map.put("1", "1");
        return map;
    }

}
