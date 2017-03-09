//package net.cassite.ioc.testioc;
//
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//
//import net.cassite.pure.ioc.AutoWire;
//import net.cassite.pure.ioc.annotations.Wire;
//
//@Wire
//public class TestBindBean extends AutoWire implements ApplicationContextAware {
//    private ApplicationContext context;
//    @Wire
//    EmptyBean bean;
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.context = applicationContext;
//    }
//
//    public ApplicationContext getContext() {
//        return context;
//    }
//}
