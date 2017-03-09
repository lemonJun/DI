package net.cassite.ioc.testioc;

import net.cassite.pure.ioc.annotations.Force;
import net.cassite.pure.ioc.annotations.Wire;

/**
 * test @Force(properties)
 */
@Wire
public class TestForceProperty {
    @Force(value = "a", properties = "prop")
    private int a;
    @Force(value = "b", properties = "prop")
    private String b;
    @Force(value = "c", properties = "prop")
    private char c;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }
}
