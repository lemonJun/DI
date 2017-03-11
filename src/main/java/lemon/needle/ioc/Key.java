package lemon.needle.ioc;

import java.lang.annotation.Annotation;

import javax.inject.Named;

/**
 * 由此还有两个问题没有解决：
 * 1、泛型；
 * 2、scope; 
 * 
 * 
 * @author WangYazhou
 * @date  2017年3月10日 下午8:38:33
 * @see 
 * @param <T>
 */
public class Key<T> {
    final Class<T> type;
    //也就是Named注解
    final Class<? extends Annotation> qualifier;
    //通过Named注解所起的名称
    final String name;

    private Key(Class<T> type, Class<? extends Annotation> qualifier, String name) {
        this.type = type;
        this.qualifier = qualifier;
        this.name = name;
    }

    public static <T> Key<T> of(Class<T> type) {
        return new Key<>(type, null, null);
    }

    public static <T> Key<T> of(Class<T> type, Class<? extends Annotation> qualifier) {
        return new Key<>(type, qualifier, null);
    }

    public static <T> Key<T> of(Key<T> key, Class<? extends Annotation> qualifier) {
        return new Key<>(key.type, qualifier, null);
    }

    public static <T> Key<T> of(Class<T> type, String name) {
        return new Key<>(type, Named.class, name);
    }

    static <T> Key<T> of(Class<T> type, Annotation qualifier) {
        if (qualifier == null) {
            return Key.of(type);
        } else {
            return qualifier.annotationType().equals(Named.class) ? Key.of(type, ((Named) qualifier).value()) : Key.of(type, qualifier.annotationType());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Key<?> key = (Key<?>) o;

        if (!type.equals(key.type))
            return false;
        if (qualifier != null ? !qualifier.equals(key.qualifier) : key.qualifier != null)
            return false;
        return !(name != null ? !name.equals(key.name) : key.name != null);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (qualifier != null ? qualifier.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String suffix = name != null ? "@\"" + name + "\"" : qualifier != null ? "@" + qualifier.getSimpleName() : "";
        return type.getName() + suffix;
    }

}