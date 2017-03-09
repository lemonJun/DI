package net.cassite.ioc.testioc;

import lemon.ioc.di.annotations.Force;
import lemon.ioc.di.annotations.Inject;

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
