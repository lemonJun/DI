package lemon.needle.ioc;

import java.util.List;

import javax.inject.Provider;

import org.omg.CORBA.Environment;

import lemon.needle.ioc.binder.Binder;

/**
 * Hasor的核心接口，它为应用程序提供了一个统一的配置界面和运行环境。
 * @version : 2013-3-26
 */
public interface AppContext {
    /**容器事件，在所有模块 start 阶段之后引发。
     * @see net.hasor.core.context.TemplateAppContext*/
    public static final String ContextEvent_Started = "ContextEvent_Started";
    /**容器事件，在所有模块 start 阶段之后引发。
     * @see net.hasor.core.context.TemplateAppContext*/
    public static final String ContextEvent_Shutdown = "ContextEvent_Shutdown";

    /** @return 获取 {@link Environment} */
    public Environment getEnvironment();

    /**获取当创建Bean时使用的{@link ClassLoader}*/
    public ClassLoader getClassLoader();

    /**
     * 模块启动通知，如果在启动期间发生异常，将会抛出该异常。
     * @param modules 启动时使用的模块。
     * @throws Throwable 启动过程中引发的异常。
     */
    public void start(Module... modules) throws Throwable;

    /**
     * 确定 AppContext 目前状态是否处于启动状态。
     * @return 返回 true 表示已经完成初始化并且启动完成。false表示尚未完成启动过程。
     */
    public boolean isStart();

    /**发送停止通知*/
    public void shutdown();
    //
    /*---------------------------------------------------------------------------------------Bean*/

    /**通过名获取Bean的类型。*/
    public Class<?> getBeanType(String bindID);

    /** @return 获取已经注册的BeanID。*/
    public String[] getBindIDs();

    /** @return 如果存在目标类型的Bean则返回Bean的名称。*/
    public String[] getNames(Class<?> targetClass);

    /** @return 判断是否存在某个ID的绑定。*/
    public boolean containsBindID(String bindID);

    /**根据ID获取{@link BindInfo}。*/
    public <T> Binder<T> getBindInfo(String bindID);

    /**根据ID获取{@link BindInfo}。*/
    public <T> Binder<T> getBindInfo(Class<T> bindType);

    /**创建Bean。*/
    public <T> T getInstance(String bindID);

    /**仅仅执行依赖注入。*/
    public <T> T justInject(T object);

    /**仅仅执行依赖注入。*/
    public <T> T justInject(T object, Class<?> beanType);

    /**创建Bean。*/
    public <T> T getInstance(Class<T> targetClass);

    /**创建Bean。*/
    public <T> T getInstance(Binder<T> info);

    /**创建Bean{@link Provider}。*/
    public <T> Provider<T> getProvider(String bindID);

    /**创建Bean{@link Provider}。*/
    public <T> Provider<T> getProvider(Class<T> targetClass);

    /**创建Bean{@link Provider}。*/
    public <T> Provider<T> getProvider(Binder<T> info);
    //
    /*-------------------------------------------------------------------------------------Binder*/

    /**
     * 通过一个类型获取所有绑定到该类型的上的对象实例。
     * @param bindType bean type
     * @return 返回符合条件的绑定对象。
     */
    public <T> List<T> findBindingBean(Class<T> bindType);

    /**
     * 通过一个类型获取所有绑定到该类型的上的对象实例（Provider形式返回）。
     * @param bindType bean type
     * @return 返回符合条件的绑定对象。
     */
    public <T> List<Provider<T>> findBindingProvider(Class<T> bindType);

    /**
     * 根据名字和类型获取绑定的对象。
     * @param withName name
     * @param bindType bean type
     * @return 返回符合条件的绑定对象。
     */
    public <T> T findBindingBean(String withName, Class<T> bindType);

    /**
     * 根据名字和类型获取绑定的对象。
     * @param withName 绑定名称。
     * @param bindType bean type
     * @return 返回{@link Provider}形式对象。
     */
    public <T> Provider<T> findBindingProvider(String withName, Class<T> bindType);

    /**
     * 通过一个类型获取所有绑定该类型下的绑定信息。
     * @param bindType bean type
     * @return 返回所有符合条件的绑定信息。
     */
    public <T> List<Binder<T>> findBindingRegister(Class<T> bindType);

    /**
     * 通过一个类型获取所有绑定该类型下的绑定信息。
     * @param withName 绑定名
     * @param bindType bean type
     * @return 返回所有符合条件的绑定信息。
     */
    public <T> Binder<T> findBindingRegister(String withName, Class<T> bindType);
}