package net.cassite.ioc.testioc;

import lemon.ioc.dinjection.annotations.Force;
import lemon.ioc.dinjection.annotations.Inject;

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
