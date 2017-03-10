package lemon.needle.ioc;

import static net.cassite.style.Style.If;
import static net.cassite.style.aggregation.Aggregation.$;
import static net.cassite.style.reflect.Reflect.cls;
import static net.cassite.style.reflect.Reflect.readOnly;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.needle.ioc.annotations.Inject;
import lemon.needle.ioc.annotations.Invoke;
import lemon.needle.ioc.annotations.Singleton;
import lemon.needle.ioc.binder.Scope;
import lemon.needle.ioc.exception.ConstructingMultiSingletonException;
import lemon.needle.ioc.provider.ConstructorFilter;
import lemon.needle.ioc.provider.ConstructorFilterChain;
import lemon.needle.ioc.provider.HandlerChain;
import lemon.needle.ioc.provider.IgnoredAnnoException;
import lemon.needle.ioc.provider.InstanceFactory;
import lemon.needle.ioc.provider.IrrelevantAnnoException;
import lemon.needle.ioc.provider.ParamHandler;
import lemon.needle.ioc.provider.ParamHandlerChain;
import lemon.needle.ioc.provider.constructor.ConstructorDefaultFilter;
import lemon.needle.ioc.provider.constructor.DefaultConstructorFilter;
import lemon.needle.ioc.provider.factory.DefaultFactory;
import lemon.needle.ioc.provider.factory.ImplementedByFactory;
import lemon.needle.ioc.provider.factory.SingletonFactory;
import lemon.needle.ioc.provider.factory.TypeAOPHandler;
import lemon.needle.ioc.provider.factory.TypeWireHandler;
import lemon.needle.ioc.provider.param.DefaultParamHandler;
import lemon.needle.ioc.provider.param.ParamForceHandler;
import lemon.needle.ioc.provider.param.ParamIgnoreHandler;
import lemon.needle.ioc.provider.param.ParamScopeHandler;
import lemon.needle.ioc.provider.param.ParamUseHandler;
import lemon.needle.ioc.provider.param.PrimitiveParameterHandler;
import net.cassite.style.reflect.ConstructorSup;
import net.cassite.style.reflect.FieldSupport;
import net.cassite.style.reflect.MethodSupport;

/**
 * 
 * The class is to control the process of handling annotations and constructing
 * objects. <br>
 * Annotation handlers are divided into 4 kinds. <br>
 * <ul>
 * <li>TypeAnnotationHandler : enabled when constructing objects</li>
 * <li>ConstructorFilter : enabled when selecting constructors</li>
 * <li>ParamAnnotationHandler : enabled when getting parameter values of a
 * constructor</li>
 * <li>SetterAnnotationHandler : enabled when invoking a setter. <br>
 * (field, method, parameter annotation of a setter are considered as 'setter
 * annotation'.)</li>
 * </ul>
 * All handlers process in a simple logic: <br>
 * enable handlers in a chain, low priority handler are earlier to be called,
 * <br>
 * handlers are recommended to call higher priority handlers, check return <br>
 * value, then decide whether to run its own handling process.
 * 
 * @author lemon
 * @see lemon.needle.ioc.provider.HandlerChain
 * @see lemon.needle.ioc.provider.InstanceFactory
 * @see lemon.needle.ioc.provider.factory.ImplementedByFactory
 * @see lemon.needle.ioc.provider.ConstructorFilterChain
 * @see lemon.needle.ioc.provider.ConstructorFilter
 * @see lemon.needle.ioc.provider.ParamHandlerChain
 * @see lemon.needle.ioc.provider.ParamHandler
 */
public class InjectorOld {

    private static final Logger logger = LoggerFactory.getLogger(InjectorOld.class);

    /**
     * param annotation handlers
     */
    private List<ParamHandler> paramAnnotationHandlers = new ArrayList<>();
    /**
     * constructor filters
     */
    private List<ConstructorFilter> constructorFilters = new ArrayList<>();
    /**
     * type annotation handler
     */
    private List<InstanceFactory> typeAnnotationHandlers = new ArrayList<>();

    //存放的是单例实体
    private Map<Class<?>, Object> singletons = new ConcurrentHashMap<>();
    public static final Scope rootScope = new Scope();
    
    protected InjectorOld() {
        rootScope.bind(Scope.class, scope -> scope);
    }

    public <T> T getInstance(Class<?> cls) {
        return getInstance(null, cls);
    }

    /**
     * get instance by class. <br>
     * this method would check TypeHandlerChain to generate instance.
     *
     * @param scope          wire scope
     * @param cls            class
     * @param expectingClass the class originally wanted
     * @return instance handled by type chain
     */
    public <T> T getInstance(Scope scope, Class<?> cls) {
        return getInstance(scope, cls, cls);
    }

    public <T> T getInstance(Scope scope, Class<?> cls, Class<?> expectingClass) {
        if (null == scope) {//当前线程
            logger.debug("constructing a scope for class {}", cls.getName());
            scope = new Scope(Scope.currentThreadScope());
        }
        logger.debug("Invoking get(Class) to get instance of {}", cls);
        HandlerChain chain = new HandlerChain(typeAnnotationHandlers, cls.getAnnotations());
        return (T) chain.next().handle(scope, cls, expectingClass, chain);
    }

    public void wire(Scope scope, Object o) {
        if (null == scope) {//当前线程
            logger.debug("constructing a scope for object {}", o);
            scope = new Scope(Scope.currentThreadScope());
        }
        final Scope s = scope;
        //
        if (!o.getClass().getName().contains("$$EnhancerByCGLIB$$")) {
            // prevent wiring cglib generated objects
            boolean wireAll = o.getClass().isAnnotationPresent(Inject.class);

            logger.debug("Start Wiring object {}", o);
            registerSingleton(o);
            // check field
            logger.debug("--checking fields");
            List<String> setterNamesOfWiredFields = new ArrayList<>();
            cls(o).allFields().stream().filter(f -> f.isAnnotationPresent(Inject.class)).forEach(f -> {
                fillField(s, o, f);
                setterNamesOfWiredFields.add("set" + f.name().substring(0, 1).toUpperCase() + f.name().substring(1));
            });
            logger.debug("--field wiring completed");
            logger.debug("--checking setters");
            cls(o).setters().stream().filter(m -> !setterNamesOfWiredFields.contains(m.name()) && (wireAll || m.isAnnotationPresent(Inject.class))).forEach(m -> invokeSetter(s, o, m));
            logger.debug("--setter wiring completed");
            logger.debug("Finished Wiring {}", o);

            logger.debug("Start Invoking methods of object {}", o);
            cls(o).allMethods().stream().filter(m -> m.annotation(Invoke.class) != null && !m.isStatic()).forEach(m -> invokeMethod(s, m, o));
            logger.debug("Finished Invoking methods of object {}", o);
        }
    }

    @SuppressWarnings("unchecked")
    public void fillField(Scope scope, Object target, @SuppressWarnings("rawtypes") FieldSupport f) {
        Annotation[] annotations = f.getMember().getAnnotations();
        logger.debug("Wiring object {}'s field {} with annotations {}", target, f, annotations);

        ParamHandlerChain chain = new ParamHandlerChain(paramAnnotationHandlers, annotations);
        Object obj = chain.next().handle(scope, f, f.getMember().getType(), f.getMember().getType(), annotations, chain);
        f.set(target, obj);
    }

    /**
     * invoke a setter<br>
     * This method would check corresponding field(might not found, that's
     * unimportant) and method annotations, then go through
     * SetterHandlerChain.
     *
     * @param scope  scope
     * @param target object to invoke
     * @param m      setter
     */
    @SuppressWarnings("unchecked")
    public void invokeSetter(Scope scope, Object target, @SuppressWarnings("rawtypes") MethodSupport m) {
        logger.debug("Wiring object {}'s method {}", target, m);

        List<FieldSupport<?, Object>> fields = cls(target).allFields();

        Set<Annotation> annset = new HashSet<>();

        // get inferred field name
        String fieldName = m.name().substring(3);

        // try to get field and its annotations ( ignore field name case)
        If($(fields).findOne(f -> f.name().equalsIgnoreCase(fieldName)), found -> {
            Collections.addAll(annset, found.getMember().getAnnotations());
        }).End();

        // try to get method annotations
        Collections.addAll(annset, m.getMember().getAnnotations());
        // parameter value annotations
        Collections.addAll(annset, m.getMember().getParameterAnnotations()[0]);

        logger.debug("With Annotations: {}", annset);

        // handle
        Annotation[] annoArray = annset.toArray(new Annotation[annset.size()]);
        ParamHandlerChain chain = new ParamHandlerChain(paramAnnotationHandlers, annoArray);
        try {
            Object o = chain.next().handle(scope, m, m.argTypes()[0], m.argTypes()[0], annoArray, chain);
            m.invoke(target, o);
        } catch (IrrelevantAnnoException | IgnoredAnnoException ignore) {
        }
    }

    /**
     * construct with given class and parameter values.
     *
     * @param con             constructor to call
     * @param parameterValues parameter values
     * @return new instance generated by the constructor and parameterValues
     */
    private Object construct(ConstructorSup<?> con, Object[] parameterValues) {
        return con.newInstance(parameterValues);
    }

    /**
     * construct with given class <br>
     * this method gets constructor from constructor filter chain, and then
     * get parameters from ParamHandlerChain. <br>
     * finally call {@link #construct(ConstructorSup, Object[])}
     *
     * @param scope wire scope
     * @param cls   class to construct
     * @return instance of the class
     * @see ParamHandlerChain
     */
    public Object constructObject(Scope scope, @SuppressWarnings("rawtypes") Class cls) {
        logger.debug("Invoking constructObject(Class) to get instance of type {}", cls);
        Set<Annotation> set = new HashSet<>();
        for (Constructor<?> cons : cls.getConstructors()) {
            Collections.addAll(set, cons.getAnnotations());
        }
        logger.debug("--gathered annotations are {}", set);
        ConstructorFilterChain chain = new ConstructorFilterChain(constructorFilters, set);
        @SuppressWarnings("unchecked")
        ConstructorSup<?> con = chain.next().handle(scope, cls(cls).constructors(), chain);

        logger.debug("--retrieved constructor is {}", con);

        Object[] pv = new Object[con.argCount()];
        for (int i = 0; i < pv.length; ++i) {
            ParamHandlerChain chain2 = new ParamHandlerChain(paramAnnotationHandlers, con.getMember().getParameterAnnotations()[i]);
            pv[i] = chain2.next().handle(scope, con, con.argTypes()[i], con.argTypes()[i], con.getMember().getParameterAnnotations()[i], chain2);
            logger.debug("--parameter at index {} is {}", i, pv[i]);
        }
        return construct(con, pv);
    }

    /**
     * get instance with class. <br>
     * this method would check whether it IsSingleton <br>
     * then invoke constructObject(Class&lt;?&gt;) to get instance<br>
     * <br>
     * This method might call singletons.get directly if the singleton class
     * already in or finished construction <b>or</b>
     * {@link #constructObject(Scope, Class)}<br>
     * <b>Note</b> that, this method will construct the given class, it will
     * not go through TypeHandlerChain, e.g. it won't find out whether it's
     * redirected to another class(using Default annotation)<br>
     * Use {@link #get(Scope, Class, Class)} to do the type check.
     *
     * @param scope wire session
     * @param cls   class to instantiate
     * @return instance of the class.
     * @see #constructObject(Scope, Class)
     * @see #get(Scope, Class, Class)
     */
    @SuppressWarnings("unchecked")
    public Object getObject(Scope scope, @SuppressWarnings("rawtypes") Class cls) {
        logger.debug("Invoking getObject(Class) to get instance of type {}", cls);
        if (cls.isAnnotationPresent(Singleton.class)) {
            logger.debug("--is singleton");
            if (singletons.containsKey(cls)) {
                return singletons.get(cls);
            } else {
                return constructObject(scope, cls);
            }
        } else {
            return constructObject(scope, cls);
        }
    }

    /**
     * This method invokes given method with inferred arguments<br>
     * It will check the ParamHandlerChain, and retrieve arguments.
     *
     * @param scope  wire scope
     * @param method method to invoke
     * @param target object of the method to invoke on
     * @return invocation result
     * @see ParamHandlerChain
     */
    @SuppressWarnings({ "unchecked" })
    public Object invokeMethod(Scope scope, @SuppressWarnings("rawtypes") MethodSupport method, Object target) {
        logger.debug("Invoking method {} of object {}", method, target);

        Object[] pv = new Object[method.argCount()];
        for (int i = 0; i < pv.length; ++i) {
            ParamHandlerChain chain2 = new ParamHandlerChain(paramAnnotationHandlers, method.getMember().getParameterAnnotations()[i]);
            pv[i] = chain2.next().handle(scope, method, method.argTypes()[i], method.argTypes()[i], method.getMember().getParameterAnnotations()[i], chain2);
        }
        return method.invoke(target, pv);
    }

    public void register(ConstructorFilter ah) {
        logger.info("registering {}", ah);
        constructorFilters.add(ah);
    }

    public void register(ParamHandler ah) {
        logger.info("registering {}", ah);
        paramAnnotationHandlers.add(ah);
    }

    //注册处理类
    public void register(InstanceFactory ah) {
        logger.info("registering {}", ah);
        typeAnnotationHandlers.add(ah);
    }

    //注册一个单例
    public void registerSingleton(Object instance) {
        if (singletons.containsKey(instance.getClass())) {
            throw new ConstructingMultiSingletonException(instance.getClass());
        }
        if (null != instance.getClass().getAnnotation(Singleton.class)) {
            singletons.put(instance.getClass(), instance);
        }
    }

    /**
     * Automatically register built-in handlers
     */
    public void autoRegister() {
        logger.info("start auto registering...");
        register(new DefaultConstructorFilter());
        register(new ConstructorDefaultFilter());

        register(new ParamScopeHandler());
        register(new PrimitiveParameterHandler());
        register(new DefaultParamHandler(this));
        register(new ParamUseHandler(this));
        //        register(new ParamExtendHandler(this));
        register(new ParamForceHandler());
        register(new ParamIgnoreHandler());

        register(new TypeAOPHandler());
        register(new TypeWireHandler(this));
        register(new DefaultFactory(this));
        register(new SingletonFactory(this));
        register(new ImplementedByFactory(this));
        //        register(new TypeExtendHandler(this));
    }

    public void closeRegistering() {
        paramAnnotationHandlers = readOnly(paramAnnotationHandlers);
        constructorFilters = readOnly(constructorFilters);
        typeAnnotationHandlers = readOnly(typeAnnotationHandlers);
        logger.info("registration closed.");
    }

    /**
     * determine whether there are handlers registered
     *
     * @return true if handlers had been registered, false otherwise
     */
    boolean isWithHandlers() {
        return !typeAnnotationHandlers.isEmpty() || !constructorFilters.isEmpty() || !paramAnnotationHandlers.isEmpty();
    }
}
