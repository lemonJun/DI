package lemon.ioc.dinjection;
//package net.cassite.pure.ioc;
//
//import net.cassite.pure.ioc.annotations.Invoke;
//import net.cassite.pure.ioc.annotations.Wire;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static net.cassite.style.reflect.Reflect.*;
//
///**
// * 
// * Base of pojo classes requiring auto wire.<br>
// * Gives POJO (or a class with setters) the capability of autowiring. <br>
// * Simply <b>new</b> a class, then all setters would be automatically called
// * <br>
// * with corresponding parameters.
// * 
// * @author lemon
// */
//public abstract class AutoWire {
//
//    private static final Logger logger = LoggerFactory.getLogger(AutoWire.class);
//
//    protected AutoWire() {
//        logger.debug("Constructing object {}", this);
//        wire(this);
//        logger.debug("Finished Constructing {}", this);
//    }
//
//    /**
//     * Retrieve an instance of given class
//     *
//     * @param scope ioc scope
//     * @param cls   instance of which to retrieve
//     * @param <T>   type of the instance
//     * @return retreived class
//     */
//    @SuppressWarnings("unchecked")
//    //    public static <T> T get(Scope scope, Class<T> cls) {
//    //        autoRegister();
//    //        if (scope == null) {
//    //            logger.debug("constructing a scope");
//    //            scope = new Scope(Scope.currentThreadScope());
//    //        }
//    //        return (T) Injector.get(scope, cls, cls);
//    //    }
//
//    /**
//     * Retrieve an instance of given class
//     *
//     * @param cls instance of which to retrieve
//     * @param <T> type of the instance
//     * @return retrieved class
//     */
//    //    public static <T> T get(Class<T> cls) {
//    //        return get(null, cls);
//    //    }
//
//    /**
//     * Invoke setters and methods with Invoke annotation on given object
//     *
//     * @param scope ioc scope
//     * @param o     the object to wire
//     */
//    public static void wire(Scope scope, Object o) {
//        autoRegister();
//        if (null == scope) {//当前线程
//            logger.debug("constructing a scope for object {}", o);
//            scope = new Scope(Scope.currentThreadScope());
//        }
//        final Scope s = scope;
//        //
//        if (!o.getClass().getName().contains("$$EnhancerByCGLIB$$")) {
//            // prevent wiring cglib generated objects
//            boolean wireAll = o.getClass().isAnnotationPresent(Wire.class);
//
//            logger.debug("Start Wiring object {}", o);
//            Injector.registerSingleton(o);
//            // check field
//            logger.debug("--checking fields");
//            List<String> setterNamesOfWiredFields = new ArrayList<>();
//            cls(o).allFields().stream().filter(f -> f.isAnnotationPresent(Wire.class)).forEach(f -> {
//                Injector.fillField(s, o, f);
//                setterNamesOfWiredFields.add("set" + f.name().substring(0, 1).toUpperCase() + f.name().substring(1));
//            });
//            logger.debug("--field wiring completed");
//            logger.debug("--checking setters");
//            cls(o).setters().stream().filter(m -> !setterNamesOfWiredFields.contains(m.name()) && (wireAll || m.isAnnotationPresent(Wire.class))).forEach(m -> Injector.invokeSetter(s, o, m));
//            logger.debug("--setter wiring completed");
//            logger.debug("Finished Wiring {}", o);
//
//            logger.debug("Start Invoking methods of object {}", o);
//            cls(o).allMethods().stream().filter(m -> m.annotation(Invoke.class) != null && !m.isStatic()).forEach(m -> Injector.invokeMethod(s, m, o));
//            logger.debug("Finished Invoking methods of object {}", o);
//        }
//    }
//
//    //    private static void autoRegister() {
//    //        // auto register if no handlers are found
//    //        if (!Injector.isWithHandlers()) {
//    //            synchronized (Injector.class) {
//    //                if (!Injector.isWithHandlers()) {
//    //                    Injector.autoRegister();
//    //                    Injector.closeRegistering();
//    //                }
//    //            }
//    //        }
//    //    }
//
//    /**
//     * Invoke setters and methods with Invoke annotation on given object
//     *
//     * @param o the object to wire
//     */
//    public static void wire(Object o) {
//        wire(null, o);
//    }
//}
