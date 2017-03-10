package net.cassite.ioc.testioc;

import lemon.needle.ioc.annotations.Force;
import lemon.needle.ioc.annotations.Inject;

/**
 * test field injection
 */
public class FieldInjectBean {
    @Inject
    @Force("a")
    String string;
    @Inject
    @Force("1")
    int integer;
}
