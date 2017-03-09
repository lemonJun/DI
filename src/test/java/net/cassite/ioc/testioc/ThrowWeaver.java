package net.cassite.ioc.testioc;

import net.cassite.pure.aop.AOPPoint;
import net.cassite.pure.aop.ExceptionWeaver;
import net.cassite.pure.aop.LoggedWeaver;

public class ThrowWeaver implements ExceptionWeaver {
    @Override
    public void doException(AOPPoint point) throws Throwable {
        point.returnValue("doAop");
        point.exceptionHandled();
    }
}
