package net.cassite.ioc.testioc;

/**
 * Created by wkgcass on 15/12/14.
 */
public class PrimitiveBean {
    private int i;
    private double d;

    public PrimitiveBean(Integer i) {
        this.i = i;
    }

    public double getD() {
        return d;
    }

    public void setD(Double d) {
        this.d = d;
    }

    public int getI() {
        return i;
    }
}
