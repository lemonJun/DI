package lemon.needle.ioc.provider;

import javax.inject.Provider;

import lemon.needle.ioc.AppContext;
import lemon.needle.ioc.AppContextAware;
import lemon.needle.ioc.binder.Binder;

/**
 * 用法：Hasor.autoAware(env,new InfoAwareProvider(...));
 * 注意事项：只可以在 AppContext init 期间使用。
 * @version : 2015年12月18日
 * @author 赵永春(zyc@hasor.net)
 */
public class InfoAwareProvider<T> implements Provider<T>, AppContextAware {
    private Binder<? extends T> info;
    private AppContext appContext;

    public InfoAwareProvider(Binder<? extends T> info) {
        //        this.info = Hasor.assertIsNotNull(info);
    }

    @Override
    public void setAppContext(AppContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public T get() {
        if (this.appContext != null && this.info != null) {
            return this.appContext.getInstance(this.info);
        }
        throw new IllegalStateException("has not been initialized");
    }
}