package net.cassite.ioc.testioc;

import lemon.needle.aop.AOPPoint;
import lemon.needle.aop.BeforeWeaver;

public class ChangeArgBeforeInvokingWeaver implements BeforeWeaver {
    @Override
    public void doBefore(AOPPoint point) {
        if (point.method.getName().equals("setName")) {
            point.args[0] = point.args[0] + "|aop";
        }
    }
}
