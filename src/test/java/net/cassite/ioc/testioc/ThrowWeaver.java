package net.cassite.ioc.testioc;

import lemon.needle.aop.AOPPoint;
import lemon.needle.aop.ExceptionWeaver;
import lemon.needle.aop.LoggedWeaver;

public class ThrowWeaver implements ExceptionWeaver {
    @Override
    public void doException(AOPPoint point) throws Throwable {
        point.returnValue("doAop");
        point.exceptionHandled();
    }
}
