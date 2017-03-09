package net.cassite.pure.ioc;

import net.cassite.style.Style;
import net.cassite.style.interfaces.RFunc1;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A scope of dependency injection<br>
 * A scope can store variables and bind classes.
 */
public class Scope {
    private static ThreadLocal<Scope> scopeThreadLocal = new ThreadLocal<>();

    /**
     * retrieve scope in current thread
     * 
     * @return current thread scope
     */
    public static Scope currentThreadScope() {
        Scope scope = scopeThreadLocal.get();
        if (scope != null)
            return scope;
        scope = new Scope(null);
        scopeThreadLocal.set(scope);
        return scope;
    }

    /**
     * parent scope
     */
    protected Scope parent;
    /**
     * container for constants, variables and properties.
     */
    protected Map<String, RFunc1<?, Scope>> dataMap = new LinkedHashMap<>();
    /**
     * container for bond classes -&gt; objects<br>
     * values that the functions return would be used to fill parameters of their bond classes
     */
    protected Map<Class<?>, RFunc1<?, Scope>> bondMap = new LinkedHashMap<>();

    /**
     * create a scope without parent scope
     */
    public Scope() {
        this(null);
    }

    /**
     * create a scope with parent scope
     *
     * @param parent parent scope
     */
    public Scope(Scope parent) {
        this.parent = parent;
    }

    /**
     * get instance stored data map
     *
     * @param name name of the data
     * @param <T>  data type
     * @return retrieved data
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        try {
            return (T) (dataMap.containsKey(name) ? (dataMap.get(name).apply(this)) : (getParent() == null ? null : getParent().get(name)));
        } catch (Throwable t) {
            throw Style.$(t);
        }
    }

    /**
     * determine whether the scope contain instance provider with given name
     *
     * @param name name
     * @return true if contains, false otherwise
     */
    public boolean containsKey(String name) {
        return dataMap.containsKey(name) || (getParent() != null && getParent().containsKey(name));
    }

    /**
     * register a function into data map, the function will be called when retrieving the object
     *
     * @param name name of the data
     * @param func function that produces data
     */
    public void register(String name, RFunc1<?, Scope> func) {
        dataMap.put(name, func);
    }

    /**
     * get parent scope
     *
     * @return parent scope, or null if no parent scope
     */
    public Scope getParent() {
        return parent;
    }

    /**
     * register a constant instance into data map
     *
     * @param name     name of the constant
     * @param constant constant object
     */
    public void registerInstance(String name, Object constant) {
        register(name, scope -> constant);
    }

    /**
     * register properties(Map[Object,Object]) into data map
     *
     * @param name       name
     * @param properties properties
     */
    public void registerProperties(String name, Map<Object, Object> properties) {
        registerInstance(name, properties);
    }

    /**
     * bind class to function that produces instance
     *
     * @param cls          class
     * @param instanceFunc function
     */
    public void bind(Class<?> cls, RFunc1<?, Scope> instanceFunc) {
        bondMap.put(cls, instanceFunc);
    }

    /**
     * get bond instance
     *
     * @param cls class
     * @return instance retrieved
     */
    public Object getBondInstance(Class<?> cls) {
        try {
            return bondMap.containsKey(cls) ? bondMap.get(cls).apply(this) : (getParent() == null ? null : getParent().getBondInstance(cls));
        } catch (Throwable e) {
            throw Style.$(e);
        }
    }

    /**
     * determine whether the class has been bond to an instance provider
     *
     * @param cls class
     * @return true if bond, false otherwise
     */
    public boolean isBond(Class<?> cls) {
        return bondMap.containsKey(cls) || (getParent() != null && getParent().isBond(cls));
    }
}
