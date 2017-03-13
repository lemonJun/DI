package test.genie;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.osgl.inject.AbsModule;
import org.osgl.inject.Genie;

import ioc.test.B;
import ioc.test.D;
import ioc.test.ImplSup;
import ioc.test.Sup;

public class GenieTest extends AbsModule {

    @Override
    protected void configure() {
        bind(Sup.class).to(ImplSup.class);
        bind(List.class).to(ArrayList.class);
    }

    @Test
    public void circle() {
        try {
            Genie genie = Genie.create();
            genie.get(B.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void eq() {
        try {
            Genie genie = Genie.create();
            System.out.println(genie.get(D.class) == genie.get(D.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
