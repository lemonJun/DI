package net.cassite.ioc.testioc;

/**
 * destroy weaver
 */
public class DestroyWeaver implements net.cassite.pure.aop.DestroyWeaver<DestroyBean> {
    @Override
    public void doDestroy(DestroyBean target) {
        target.destroy();
    }
}
