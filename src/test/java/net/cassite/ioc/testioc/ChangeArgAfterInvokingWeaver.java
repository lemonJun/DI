package net.cassite.ioc.testioc;

import lemon.ioc.aop.AOPPoint;
import lemon.ioc.aop.AfterWeaver;
import lemon.ioc.aop.LoggedWeaver;

public class ChangeArgAfterInvokingWeaver implements AfterWeaver {
    @Override
    public void doAfter(AOPPoint point) {
        if (point.method.getName().equals("getName")) {
            point.returnValue(point.returnValue() + "|aop2");
        }
    }
}
