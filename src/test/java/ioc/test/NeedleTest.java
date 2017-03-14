package ioc.test;

import org.junit.Test;

import lemon.needle.ioc.Needle;

public class NeedleTest {

    static {
        Needle.init();
    }
    
    @Test
    public void instance() {
        try {
            A a = Needle.getInstance(A.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void same() {
        try {
            D c1 = Needle.getInstance(D.class);
            D c2 = Needle.getInstance(D.class);
            System.out.println(c1);
            System.out.println();
            System.out.println(c1 == c2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void provid() {
        try {
            D c2 = Needle.getInstance(D.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bind() {
        try {
            Sup sup = Needle.getInstance(Sup.class);
            sup.say();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void implentby() {
        try {
            Sup sup = Needle.getInstance(Sup.class);
            sup.say();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
