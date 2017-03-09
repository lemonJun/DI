package net.cassite.ioc.testioc;

import net.cassite.pure.aop.AOPPoint;
import net.cassite.pure.aop.BeforeWeaver;
import net.cassite.pure.aop.LoggedWeaver;

public class ReturnBeforeInvokingWeaver implements BeforeWeaver {
    @Override
    public void doBefore(AOPPoint point) {
        if (point.method.getName().equals("getName")) {
            point.returnValue("doAop");
        }
    }
}
