package ioc.test;

import javax.inject.Singleton;

import org.junit.Test;

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
