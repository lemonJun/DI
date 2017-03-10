package net.cassite.ioc.testioc;

/**
 * destroy weaver
 */
public class DestroyWeaver implements lemon.needle.aop.DestroyWeaver<DestroyBean> {
    @Override
    public void doDestroy(DestroyBean target) {
        target.destroy();
    }
}
