package net.cassite.ioc.testioc;

import lemon.ioc.aop.AOPPoint;
import lemon.ioc.aop.BeforeWeaver;
import lemon.ioc.aop.LoggedWeaver;

public class ReturnBeforeInvokingWeaver implements BeforeWeaver {
    @Override
    public void doBefore(AOPPoint point) {
        if (point.method.getName().equals("getName")) {
            point.returnValue("doAop");
        }
    }
}
