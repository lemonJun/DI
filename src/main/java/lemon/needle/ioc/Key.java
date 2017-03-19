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
    final String name;
    private final TypeLiteral<T> typeLiteral;
    private Class<? extends Annotation> scope;

    private Key(Class<T> type, Class<? extends Annotation> qualifier, String name) {
        this.type = type;
        this.qualifier = qualifier;
        this.typeLiteral = (TypeLiteral<T>) TypeLiteral.get(type);
        this.name = name;
        this.scope = null;
    }

    public static <T> Key<T> of(Class<T> type) {
        return new Key<>(type, null, null);
    }

    public static <T> Key<T> of(Class<T> type, Class<? extends Annotation> qualifier) {
        return new Key<>(type, qualifier, null);
    }

    public static <T> Key<T> of(Class<T> type, Class<? extends Annotation> qualifier, String name) {
        return new Key<>(type, qualifier, name);
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
        if (typeLiteral != null ? !typeLiteral.equals(key.typeLiteral) : key.typeLiteral != null) {
            return false;
        }
        return !(name != null ? !name.equals(key.name) : key.name != null);
    }

    public Class<? extends Annotation> getScope() {
        return scope;
    }

    public void setScope(Class<? extends Annotation> scope) {
        this.scope = scope;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (qualifier != null ? qualifier.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (typeLiteral != null ? typeLiteral.hashCode() * 31 : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (name != null) {
            sb.append("@\"").append(name).append("\"");
        } else if (qualifier != null) {
            sb.append("@").append(qualifier.getSimpleName());
        } else if (typeLiteral != null) {
            sb.append("@").append(typeLiteral);
        }
        //        String suffix = name != null ? "@\"" + name + "\"" : qualifier != null ? "@" + qualifier.getSimpleName() : "";
        return type.getName() + sb.toString();
    }

}