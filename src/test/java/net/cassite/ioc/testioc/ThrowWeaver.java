package net.cassite.ioc.testioc;

import lemon.ioc.aop.AOPPoint;
import lemon.ioc.aop.ExceptionWeaver;
import lemon.ioc.aop.LoggedWeaver;

public class ThrowWeaver implements ExceptionWeaver {
    @Override
    public void doException(AOPPoint point) throws Throwable {
        point.returnValue("doAop");
        point.exceptionHandled();
    }
}
