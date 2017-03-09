package net.cassite.ioc.testioc;

import net.cassite.pure.aop.AOPPoint;
import net.cassite.pure.aop.AfterWeaver;
import net.cassite.pure.aop.LoggedWeaver;

public class ChangeArgAfterInvokingWeaver implements AfterWeaver {
    @Override
    public void doAfter(AOPPoint point) {
        if (point.method.getName().equals("getName")) {
            point.returnValue(point.returnValue() + "|aop2");
        }
    }
}
