package ioc.test;

public class D {

    private int i;
    private String str;

    //    @Inject
    //    private A a;

    D() {
        System.out.println("d");
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    //    private D(int i) {
    //        System.out.println("d");
    //    }

}
