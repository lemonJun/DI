package ioc.test;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import lemon.needle.ioc.Needle;

public class DITest {

    static {
        PropertyConfigurator.configure("D:/log4j.properties");
        Needle.init();
    }

    @Test
    public void getInstance() {
        try {
            Bll bll = Needle.getInstance(Bll.class);
            if (bll != null) {
                bll.say();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void same() {
        try {
            Bll bll1 = Needle.getInstance(Bll.class);
            Bll bll2 = Needle.getInstance(Bll.class);
            System.out.println(bll1 == bll2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
