package lemon.needle.ioc;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import ioc.test.ImplSup;
import ioc.test.Sup;
import lemon.needle.aop.Matchers;
import lemon.needle.aop.MethodInvokeTime;
import lemon.needle.ioc.annotations.Provides;

public class NeedleModel extends AbsModule {

    @Override
    public void configure() {
        bind(Sup.class).to(ImplSup.class);
        bindInterceptor(Matchers.inSubpackage("com"), Matchers.any(), new MethodInvokeTime());
    }

    @Singleton
    @Provides
    Map maps() {
        Map map = new HashMap();
        map.put("1", "1");
        map.put("12", "1");
        return map;
    }

}
