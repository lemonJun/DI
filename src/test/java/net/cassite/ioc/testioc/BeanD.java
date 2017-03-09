package net.cassite.ioc.testioc;

import lemon.ioc.di.annotations.Use;

public class BeanD implements IBeanD {
    private BeanA a;
    private String text;

    public BeanA getA() {
        return a;
    }

    @Use("testVar")
    public void setA(BeanA a) {
        this.a = a;
    }

    //    public TestIoC getTestIoC() {
    //        return testIoC;
    //    }
    //
    //    public void setTestIoC(@Use("testConst") TestIoC testIoC) {
    //        this.testIoC = testIoC;
    //    }

    public String getText() {
        return text;
    }

    @Use("testVar2")
    public void setText(String text) {
        this.text = text;
    }
}
