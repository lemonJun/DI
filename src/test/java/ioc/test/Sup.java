package ioc.test;

import lemon.needle.ioc.annotations.ImplementedBy;

@ImplementedBy(value = ImplSup.class)
public interface Sup {
    
    void say();
}
