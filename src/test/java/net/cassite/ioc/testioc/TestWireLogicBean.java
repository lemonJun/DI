package net.cassite.ioc.testioc;

import net.cassite.pure.ioc.annotations.Force;
import net.cassite.pure.ioc.annotations.Wire;

public class TestWireLogicBean {
    String string;
    int integer;

    public void setInteger(int integer) {
        this.integer = integer;
    }
}

@Wire
class TestWireLogicBeanWithWireOnClass {
    @Force("a")
    String string;
    int integer;

    @Force("1")
    public void setInteger(int integer) {
        this.integer = integer;
    }
}

class TestWireLogicBeanWithWireOnField {
    @Wire
    @Force("a")
    String string;
    int integer;

    public void setInteger(int integer) {
        this.integer = integer;
    }
}

class TestWireLogicBeanWithWireOnSetter {
    String string;
    int integer;

    @Wire
    @Force("1")
    public void setInteger(int integer) {
        this.integer = integer;
    }
}