package net.cassite.ioc.testioc;

import lemon.needle.aop.AOPPoint;
import lemon.needle.aop.AfterWeaver;
import lemon.needle.aop.LoggedWeaver;

public class ChangeArgAfterInvokingWeaver implements AfterWeaver {
    @Override
    public void doAfter(AOPPoint point) {
        if (point.method.getName().equals("getName")) {
            point.returnValue(point.returnValue() + "|aop2");
        }
    }
}
