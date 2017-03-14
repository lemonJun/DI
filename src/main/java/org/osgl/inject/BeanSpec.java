package org.osgl.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.osgl.inject.annotation.LoadValue;
import org.osgl.inject.annotation.Provides;
import org.osgl.inject.util.CommonUtil;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import lemon.needle.ioc.util.StringUtil;

/**
 * key
 * Specification of a bean to be injected
 */
public class BeanSpec {
    private final Injector injector;
    private final int hc;
    private final Type type;
    private final Set<Annotation> qualifiers = Sets.newHashSet();
    private final Set<Annotation> postProcessors = Sets.newHashSet();
    // only applied when bean spec constructed from Field
    private final int modifiers;
    /**
     * The annotations will be used for calculating the hashCode and do
     * equality test. The following annotations will added into
     */
    private final Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
    /**
     * Stores all annotations including the ones that participating hashCode calculating and
     */
    private final Map<Class<? extends Annotation>, Annotation> allAnnotations = new HashMap<Class<? extends Annotation>, Annotation>();

    private final Set<AnnoData> annoData = new HashSet<AnnoData>();
    /**
     * Store the name value of Named annotation if presented
     */
    private String name;
    private Class<? extends Annotation> scope;
    private Annotation valueLoader;
    private List<Type> typeParams;

    /**
     * Construct the `BeanSpec` with bean type and field or parameter
     * annotations
     */
    private BeanSpec(Type type, Annotation[] annotations, String name, Injector injector, int modifiers) {
        this.injector = injector;
        this.type = type;
        this.name = name;
        this.resolveTypeAnnotations();
        this.resolveAnnotations(annotations);
        this.hc = calcHashCode();
        this.modifiers = modifiers;
    }

    private BeanSpec(BeanSpec source, Type convertTo) {
        this.name = source.name;
        this.injector = source.injector;
        this.type = convertTo;
        this.qualifiers.addAll(source.qualifiers);
        //        this.elementLoaders.addAll(source.elementLoaders);
        //        this.filters.addAll(source.filters);
        //        this.transformers.addAll(source.transformers);
        this.valueLoader = source.valueLoader;
        this.annotations.putAll(source.annotations);
        this.annoData.addAll(source.annoData);
        this.allAnnotations.putAll(source.allAnnotations);
        this.hc = calcHashCode();
        this.modifiers = source.modifiers;
    }

    private BeanSpec(BeanSpec source, String name) {
        this.name = name;
        this.injector = source.injector;
        this.type = source.type;
        this.qualifiers.addAll(source.qualifiers);
        //        this.elementLoaders.addAll(source.elementLoaders);
        //        this.filters.addAll(source.filters);
        //        this.transformers.addAll(source.transformers);
        this.valueLoader = source.valueLoader;
        this.annotations.putAll(source.annotations);
        this.annoData.addAll(source.annoData);
        this.allAnnotations.putAll(source.allAnnotations);
        this.hc = calcHashCode();
        this.modifiers = source.modifiers;
    }

    @Override
    public int hashCode() {
        return hc;
    }

    /**
     * A bean spec equals to another bean spec if all of the following conditions are met:
     * * the {@link #type} of the two bean spec equals to each other
     * * the {@link #annotations} of the two bean spec equals to each other
     *
     * Specifically, {@link #scope} does not participate comparison because
     * 1. Scope annotations shall be put onto {@link java.lang.annotation.ElementType#TYPE type},
     *    or the factory method with {@link Provides} annotation, which is equivalent to `Type`.
     *    So it is safe to ignore scope annotation because one type cannot be annotated with different
     *    scope
     * 2. If we count scope annotation in equality test, we will never be able to get the correct
     *    provider stem from the factory methods.
     *
     * @param obj the object to compare with this object
     * @return `true` if the two objects equals as per described above
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof BeanSpec) {
            BeanSpec that = (BeanSpec) obj;
            return that.hc == hc && CommonUtil.eq(type, that.type) && CommonUtil.eq(name, that.name) && CommonUtil.eq(annoData, that.annoData);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append(type());
        if (StringUtil.isNotNullOrEmpty(name)) {
            sb.append("(").append(name).append(")");
        }
        List<Object> list = Lists.newArrayList();
        if (null != valueLoader) {
            list.add(valueLoader);
        } else {
            list.add(qualifiers);
            //            list.add(elementLoaders);
            //            list.add(filters);
        }
        if (null != scope) {
            list.add(scope.getSimpleName());
        }
        if (!list.isEmpty()) {
            sb.append("@[").append(Joiner.on(", ").join(list)).append("]");
        }
        return sb.toString();
    }

    public Injector injector() {
        return injector;
    }

    public Type type() {
        return type;
    }

    @SuppressWarnings("rawtypes")
    public Class rawType() {
        return rawTypeOf(type);
    }

    public String name() {
        return name;
    }

    public Annotation[] allAnnotations() {
        return allAnnotations.values().toArray(new Annotation[allAnnotations.size()]);
    }

    /**
     * Convert an array bean spec to a list bean spec
     * @return the array bean spec with component type derived from this bean spec
     */
    public BeanSpec toList() {
        return new BeanSpec(this, ArrayList.class);
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(Class<T> annoClass) {
        return (T) allAnnotations.get(annoClass);
    }

    public boolean hasAnnotation(Class<? extends Annotation> annoClass) {
        return allAnnotations.containsKey(annoClass);
    }

    public boolean hasAnnotation() {
        return !allAnnotations.isEmpty();
    }

    public int getModifiers() {
        return modifiers;
    }

    public boolean isTransient() {
        return Modifier.isTransient(modifiers);
    }

    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }

    public boolean isPrivate() {
        return Modifier.isPrivate(modifiers);
    }

    public boolean isPublic() {
        return Modifier.isPublic(modifiers);
    }

    public boolean isProtected() {
        return Modifier.isProtected(modifiers);
    }

    public boolean isFinal() {
        return Modifier.isFinal(modifiers);
    }

    BeanSpec rawTypeSpec() {
        return BeanSpec.of(rawType(), injector);
    }

    boolean isMap() {
        return Map.class.isAssignableFrom(rawType());
    }

    boolean isProvider() {
        return Provider.class.isAssignableFrom(rawType());
    }

    BeanSpec toProvidee() {
        return new BeanSpec(this, ((ParameterizedType) type).getActualTypeArguments()[0]);
    }

    BeanSpec withoutName() {
        return new BeanSpec(this, (String) null);
    }

    BeanSpec withoutQualifiers() {
        if (qualifiers.isEmpty()) {
            return this;
        }
        BeanSpec spec = withoutName();
        spec.qualifiers.clear();
        return spec;
    }

    public List<Type> typeParams() {
        if (null == typeParams) {
            if (type instanceof ParameterizedType) {
                ParameterizedType ptype = cast(type);
                Type[] ta = ptype.getActualTypeArguments();
                typeParams = Arrays.asList(ta);
            } else {
                typeParams = new ArrayList<Type>();
            }
        }
        return typeParams;
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
        return (T) o;
    }

    public boolean isInstanceOf(Class<?> c) {
        return c.isAssignableFrom(rawType());
    }

    public boolean isInstance(Object o) {
        Class<?> c = rawType();
        if (c.isInstance(o)) {
            return true;
        }
        Class<?> p = CommonUtil.primitiveTypeOf(c);
        if (null != p && p.isInstance(o)) {
            return true;
        }
        Class<?> w = CommonUtil.wrapperClassOf(c);
        if (null != w && w.isInstance(o)) {
            return true;
        }
        return false;
    }

    public Set<Annotation> qualifiers() {
        return new HashSet<>(qualifiers);
    }

    boolean hasValueLoader() {
        return null != valueLoader;
    }

    Set<Annotation> postProcessors() {
        return postProcessors;
    }

    Annotation valueLoader() {
        return valueLoader;
    }

    public Class<? extends Annotation> scope() {
        return scope;
    }

    BeanSpec scope(Class<? extends Annotation> scopeAnno) {
        scope = scopeAnno;
        return this;
    }

    boolean notConstructable() {
        Class<?> c = rawType();
        return c.isInterface() || c.isArray() || Modifier.isAbstract(c.getModifiers());
    }

    private void resolveTypeAnnotations() {
        for (Annotation annotation : rawType().getAnnotations()) {
            resolveScope(annotation);
            Class<? extends Annotation> annoType = annotation.annotationType();
            if (annoType == Named.class) {
                name = ((Named) annotation).value();
            } else if (injector.isQualifier(annoType)) {
                qualifiers.add(annotation);
            }
            allAnnotations.put(annotation.annotationType(), annotation);
        }
    }

    private void resolveAnnotations(Annotation[] aa) {
        if (null == aa || aa.length == 0) {
            return;
        }
        Class<?> rawType = rawType();
        //        boolean isMap = Map.class.isAssignableFrom(rawType);
        //        boolean isContainer = isMap || Collection.class.isAssignableFrom(rawType) || rawType.isArray();
        //        MapKey mapKey = null;
        List<Annotation> loadValueIncompatibles = new ArrayList<Annotation>();
        // Note only qualifiers and bean loaders annotation are considered
        // effective annotation. Scope annotations is not effective here
        // because they are tagged on target type, not the field or method
        // parameter
        for (Annotation anno : aa) {
            Class<? extends Annotation> cls = anno.annotationType();
            allAnnotations.put(cls, anno);
            if (Inject.class == cls || Provides.class == cls) {
                continue;
            }

            if (LoadValue.class == cls || cls.isAnnotationPresent(LoadValue.class)) {
                valueLoader = anno;
                //            } else if (LoadCollection.class == cls || cls.isAnnotationPresent(LoadCollection.class)) {
                //                if (isContainer) {
                //                    //                    elementLoaders.add(anno);
                //                    loadValueIncompatibles.add(anno);
                //                } else {
                //                    Genie.logger.warn("LoadCollection annotation[%s] ignored as target type is neither Collection nor Map", cls.getSimpleName());
                //                }
            } else if (Named.class == cls) {
                name = ((Named) anno).value();
            } else if (injector.isQualifier(cls)) {
                qualifiers.add(anno);
                loadValueIncompatibles.add(anno);
                //            } else if (Filter.class == cls || cls.isAnnotationPresent(Filter.class)) {
                //                if (isContainer) {
                //                    filters.add(anno);
                //                    loadValueIncompatibles.add(anno);
                //                } else {
                //                    Genie.logger.warn("Filter annotation[%s] ignored as target type is neither Collection nor Map", cls.getSimpleName());
                //                }
                //            } else if (Transform.class == cls || cls.isAnnotationPresent(Transform.class)) {
                //                transformers.add(anno);
            } else {
                if (injector.isPostConstructProcessor(cls)) {
                    postProcessors.add(anno);
                    annotations.put(cls, anno);
                    annoData.add(new AnnoData(anno));
                } else {
                    resolveScope(anno);
                }
            }
        }

        //        if (isContainer && elementLoaders.isEmpty()) {
        //            // assume we want to inject typed elements
        //            Class<?> rawType0;
        //            if (rawType.isArray()) {
        //                rawType0 = rawType.getComponentType();
        //            } else {
        //                List<Type> typeParams = typeParams();
        //                if (typeParams.isEmpty()) {
        //                    rawType0 = Object.class;
        //                } else {
        //                    Type theType = typeParams.get(isMap ? 1 : 0);
        //                    rawType0 = rawTypeOf(theType);
        //                }
        //            }
        //            if (!CommonUtil.isSimpleType(rawType0)) {
        //                TypeOf typeOfAnno = AnnotationUtil.createAnnotation(TypeOf.class);
        //                elementLoaders.add(typeOfAnno);
        //                loadValueIncompatibles.add(typeOfAnno);
        //            }
        //        }
        if (null != valueLoader) {
            if (!loadValueIncompatibles.isEmpty()) {
                throw new InjectException(String.format("ValueLoader annotation cannot be used with ElementLoader and Filter annotations: %s", annotations));
            }
            annotations.put(valueLoader.annotationType(), valueLoader);
            annoData.add(new AnnoData(valueLoader));
        } else {
            for (Annotation anno : loadValueIncompatibles) {
                annotations.put(anno.annotationType(), anno);
                annoData.add(new AnnoData(anno));
            }
        }
    }

    private void resolveScope(Annotation annotation) {
        Class<? extends Annotation> annoClass = annotation.annotationType();
        if (injector.isScope(annoClass)) {
            if (null != scope) {
                throw new InjectException(String.format("Multiple Scope annotation found: %s", this));
            }
            scope = annoClass;
        }
    }

    /**
     * Return hash code based on {@link #type} and {@link #annotations}.
     *
     * @return {@link Object#hashCode()} of this bean
     * @see #equals(Object)
     */
    private int calcHashCode() {
        return CommonUtil.hc(type, name, annoData);
    }

    public static BeanSpec of(Class<?> clazz, Injector injector) {
        return new BeanSpec(clazz, null, null, injector, 0);
    }

    public static BeanSpec of(Type type, Annotation[] paramAnnotations, Injector injector) {
        return new BeanSpec(type, paramAnnotations, null, injector, 0);
    }

    public static BeanSpec of(Type type, Annotation[] paramAnnotations, Injector injector, int modifiers) {
        return new BeanSpec(type, paramAnnotations, null, injector, modifiers);
    }

    public static BeanSpec of(Type type, Annotation[] paramAnnotations, String name, Injector injector) {
        return new BeanSpec(type, paramAnnotations, name, injector, 0);
    }

    public static BeanSpec of(Type type, Annotation[] paramAnnotations, String name, Injector injector, int modifiers) {
        return new BeanSpec(type, paramAnnotations, name, injector, modifiers);
    }

    public static BeanSpec of(Field field, Injector injector) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        return BeanSpec.of(field.getGenericType(), annotations, field.getName(), injector, field.getModifiers());
    }
    
    @SuppressWarnings("rawtypes")
    public static Class<?> rawTypeOf(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getRawType();
        } else {
            throw new InjectException(String.format("type not recognized: %s", type));
        }
    }

    // keep the effective annotation data
    // - the property annotated with NonBinding is ignored
    private static class AnnoData {
        private Class<? extends Annotation> annoClass;
        private Map<String, Object> data;

        AnnoData(Annotation annotation) {
            annoClass = annotation.annotationType();
            data = evaluate(annotation);
        }

        @Override
        public int hashCode() {
            return CommonUtil.hc(annoClass, data);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof AnnoData) {
                AnnoData that = (AnnoData) obj;
                return CommonUtil.eq(annoClass, that.annoClass) && CommonUtil.eq(data, that.data);
            }
            return false;
        }

        private static Map<String, Object> evaluate(Annotation anno) {
            Map<String, Object> properties = new HashMap<String, Object>();
            Class<? extends Annotation> annoClass = anno.annotationType();
            Method[] ma = annoClass.getMethods();
            for (Method m : ma) {
                if (isStandardAnnotationMethod(m) || shouldIgnore(m)) {
                    continue;
                }
                properties.put(m.getName(), CommonUtil.invokeVirtual(anno, m));
            }
            return properties;
        }

        private static Set<String> standardsAnnotationMethods = ImmutableSet.of("equals", "hashCode", "toString", "annotationType", "getClass");

        private static boolean isStandardAnnotationMethod(Method m) {
            return standardsAnnotationMethods.contains(m.getName());
        }

        private static boolean shouldIgnore(Method method) {
            Annotation[] aa = method.getDeclaredAnnotations();
            for (Annotation a : aa) {
                if (shouldIgnore(a)) {
                    return true;
                }
            }
            return false;
        }

        private static boolean shouldIgnore(Annotation annotation) {
            return annotation.annotationType().getName().endsWith("Nonbinding");
        }
    }

}
