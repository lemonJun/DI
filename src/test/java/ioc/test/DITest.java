package ioc.test;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import lemon.ioc.DIHolder;

public class DITest {

    static {
        PropertyConfigurator.configure("D:/log4j.properties");
        DIHolder.init();
    }

    @Test
    public void getInstance() {
        try {
            Bll bll = DIHolder.getInstance(Bll.class);
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
            Bll bll1 = DIHolder.getInstance(Bll.class);
            Bll bll2 = DIHolder.getInstance(Bll.class);
            System.out.println(bll1 == bll2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
