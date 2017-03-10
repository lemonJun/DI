package net.cassite.ioc.testioc;

import lemon.needle.ioc.annotations.Force;
import lemon.needle.ioc.annotations.Inject;

/**
 * test non class wire
 */
public class NonClassWire {
    @Inject
    @Force("a")
    String string;

    private int integer;

    public int getInteger() {
        return integer;
    }
    
    @Inject
    @Force("1")
    public void setInteger(int integer) {
        this.integer = integer;
    }
}
