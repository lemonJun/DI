//package net.cassite.ioc.testioc;
//
//import net.cassite.pure.ioc.AutoWire;
//import net.cassite.pure.ioc.annotations.Extend;
//import net.cassite.pure.ioc.annotations.Wire;
//import net.cassite.pure.ioc.ext.SpringExt;
//
//@Wire
//public class SpringExtTestBean extends AutoWire {
//    @Extend(handler = SpringExt.class, args = "springTestBean")
//    private SpringTestBean bean; // setter
//
//    private SpringTestBean bean2; // type
//
//    private SpringTestBean bean3; // param
//
//    public SpringExtTestBean(SpringTestBean bean3) {
//        this.bean3 = bean3;
//    }
//
//    public SpringTestBean getBean2() {
//        return bean2;
//    }
//
//    public void setBean2(SpringTestBean bean2) {
//        this.bean2 = bean2;
//    }
//
//    public SpringTestBean getBean() {
//        return bean;
//    }
//
//    public void setBean(SpringTestBean bean) {
//        this.bean = bean;
//    }
//
//    public SpringTestBean getBean3() {
//        return bean3;
//    }
//}
