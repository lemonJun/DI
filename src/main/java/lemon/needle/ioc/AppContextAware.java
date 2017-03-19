package lemon.needle.ioc;

/**
 * 当 AppContext 创建这个Bean时。容器会调用Bean实现的这个接口方法，将容器自身注入进来。
 * @version : 2013-11-8
 * @author 赵永春(zyc@hasor.net)
 */
public interface AppContextAware {
    /**
     * 注入AppContext。
     * @param appContext 注入的AppContext。
     */
    public void setAppContext(AppContext appContext);
}