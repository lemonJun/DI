package net.cassite.ioc.testioc;

import lemon.needle.aop.AOPPoint;
import lemon.needle.aop.BeforeWeaver;
import lemon.needle.aop.LoggedWeaver;

public class ReturnBeforeInvokingWeaver implements BeforeWeaver {
    @Override
    public void doBefore(AOPPoint point) {
        if (point.method.getName().equals("getName")) {
            point.returnValue("doAop");
        }
    }
}
