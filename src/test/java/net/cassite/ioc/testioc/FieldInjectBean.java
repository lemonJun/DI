package net.cassite.ioc.testioc;

import net.cassite.pure.ioc.annotations.Force;
import net.cassite.pure.ioc.annotations.Wire;

/**
 * test field injection
 */
public class FieldInjectBean {
    @Wire
    @Force("a")
    String string;
    @Wire
    @Force("1")
    int integer;
}
