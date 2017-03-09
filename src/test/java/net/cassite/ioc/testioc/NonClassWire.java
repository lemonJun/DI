package net.cassite.ioc.testioc;

import net.cassite.pure.ioc.annotations.Force;
import net.cassite.pure.ioc.annotations.Wire;

/**
 * test non class wire
 */
public class NonClassWire {
    @Wire
    @Force("a")
    String string;

    private int integer;

    public int getInteger() {
        return integer;
    }

    @Wire
    @Force("1")
    public void setInteger(int integer) {
        this.integer = integer;
    }
}
