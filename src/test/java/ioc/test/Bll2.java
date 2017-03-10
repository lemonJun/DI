package ioc.test;

import javax.inject.Singleton;

@Singleton
public class Bll2<T> {

    private T t;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public Bll2() {
        System.out.println("bll init");
    }

    public void say() {
        System.out.println("hello bll");
    }

}
