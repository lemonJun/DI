package ioc.test;

import org.junit.Test;

import lemon.ioc.dinjection.annotations.Singleton;

@Singleton
public class Bll {

    public Bll() {
        System.out.println("bll init");
    }

    @Test
    public void say() {
        System.out.println("hello bll");
    }

}
