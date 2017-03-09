package net.cassite.ioc.testioc;

import lemon.ioc.di.annotations.Force;
import lemon.ioc.di.annotations.Inject;

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
