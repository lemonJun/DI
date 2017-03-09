package net.cassite.ioc.testioc;

public class BeanFactory {
    public static String text;

    public BeanA getBean() {
        BeanA a = new BeanA();
        a.setB(null);
        return a;
    }
}
