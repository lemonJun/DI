package lemon.ioc.di.annotations;
//package net.cassite.pure.ioc.annotations;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Inherited;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//import net.cassite.pure.ioc.ExtendingHandler;
//
///**
// * This annotations aims to simplify retrieving objects from other object
// * containers.<br>
// * e.g. if you need to retrieve an object from spring, you need to (for example)
// * create a <code>Spring</code> annotation, then create
// * <code>TypeSpringHandler</code> and <code>ParamSpringHandler</code> to
// * finish the injection and object-retrieving. <br>
// * However the only extension was retrieving an object from Spring.<br>
// * Now you can use this to simplify your retrieving process.
// *
// * @author lemon
// */
//@Retention(RetentionPolicy.RUNTIME)
//@Target({ ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
//@Inherited
//public @interface Extend {
//    /**
//     * ExtendingHandler implement class. The handler would be retrieved
//     * using {@link net.cassite.pure.ioc.AutoWire#get(Class)}
//     *
//     * @return class
//     * @see net.cassite.pure.ioc.AutoWire#get(Class)
//     * @see ExtendingHandler
//     */
//    @SuppressWarnings("rawtypes")
//    Class handler();
//
//    /**
//     * Arguments to fill into your handler
//     *
//     * @return arguments
//     */
//    String[] args();
//}
