package net.cassite.ioc.testioc;

import net.cassite.pure.aop.AOPPoint;
import net.cassite.pure.aop.BeforeWeaver;
import net.cassite.pure.aop.TargetAware;
import net.cassite.style.interfaces.RFunc0;

public class TargetAwareWeaver implements BeforeWeaver, TargetAware<Object> {
    private Object target;

    @Override
    public void doBefore(AOPPoint point) {
        assert target != null;
    }

    @Override
    public void targetAware(RFunc0<Object> o) {
        this.target = o;
    }
}
