package org.osgl.inject.util;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

import lemon.needle.ioc.util.StringUtil;

/**
 * A String value resolver resolves a {@link String string value} into
 * a certain type of object instance.
 */
public abstract class StringValueResolver<T> {

    public abstract T resolve(String value);

    // For primary types
    private static final StringValueResolver<Boolean> _boolean = new StringValueResolver<Boolean>() {
        @Override
        public Boolean resolve(String value) {
            if (StringUtil.isBlank(value)) {
                return Boolean.FALSE;
            }
            return Boolean.parseBoolean(value);
        }
    };
    private static final StringValueResolver<Boolean> _Boolean = new StringValueResolver<Boolean>() {
        @Override
        public Boolean resolve(String value) {
            if (StringUtil.isBlank(value)) {
                return null;
            }
            return Boolean.parseBoolean(value);
        }
    };
    private static final Map<String, Character> PREDEFINED_CHARS = new HashMap<String, Character>();

    static {
        PREDEFINED_CHARS.put("\\b", '\b');
        PREDEFINED_CHARS.put("\\f", '\f');
        PREDEFINED_CHARS.put("\\n", '\n');
        PREDEFINED_CHARS.put("\\r", '\r');
        PREDEFINED_CHARS.put("\\t", '\t');
        PREDEFINED_CHARS.put("\\", '\"');
        PREDEFINED_CHARS.put("\\'", '\'');
        PREDEFINED_CHARS.put("\\\\", '\\');
    }

    /**
     * Parsing String into char. The rules are:
     *
     * 1. if there value is null or empty length String then return `defval` specified
     * 2. if the length of the String is `1`, then return that one char in the string
     * 3. if the value not starts with '\', then throw `IllegalArgumentException`
     * 4. if the value starts with `\\u` then parse the integer using `16` radix. The check
     *    the range, if it fall into Character range, then return that number, otherwise raise
     *    `IllegalArgumentException`
     * 5. if the value length is 2 then check if it one of {@link #PREDEFINED_CHARS}, if found
     *    then return
     * 6. check if it valid OctalEscape defined in the <a href="https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10.6">spec</a>
     *    if pass the check then return that char
     * 7. all other cases throw `IllegalArgumentException`
     *
     * @param value the string value to be resolved
     * @param defVal the default value when string value is `null` or empty
     * @return the char resolved from the string
     */
    private static Character resolveChar(String value, Character defVal) {
        if (null == value) {
            return defVal;
        }
        switch (value.length()) {
            case 0:
                return defVal;
            case 1:
                return value.charAt(0);
            default:
                if (value.startsWith("\\")) {
                    if (value.length() == 2) {
                        Character c = PREDEFINED_CHARS.get(value);
                        if (null != c) {
                            return c;
                        }
                    }
                    try {
                        String s = value.substring(1);
                        if (s.startsWith("u")) {
                            int i = Integer.parseInt(s.substring(1), 16);
                            if (i > Character.MAX_VALUE || i < Character.MIN_VALUE) {
                                throw new IllegalArgumentException("Invalid character: " + value);
                            }
                            return (char) i;
                        } else if (s.length() > 3) {
                            throw new IllegalArgumentException("Invalid character: " + value);
                        } else {
                            if (s.length() == 3) {
                                int i = Integer.parseInt(s.substring(0, 1));
                                if (i > 3) {
                                    throw new IllegalArgumentException("Invalid character: " + value);
                                }
                            }
                            int i = Integer.parseInt(s, 8);
                            return (char) i;
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid character: " + value);
                    }
                } else {
                    throw new IllegalArgumentException("Invalid character: " + value);
                }
        }
    }

    /**
     * Returns the char resolver based on {@link #resolveChar(String, Character)} with `\0` as
     * default value
     */
    private static final StringValueResolver<Character> _char = new StringValueResolver<Character>() {
        @Override
        public Character resolve(String value) {
            return resolveChar(value, '\0');
        }
    };

    /**
     * Returns the char resolver based on {@link #resolveChar(String, Character)} with `null` as
     * default value
     */
    private static final StringValueResolver<Character> _Char = new StringValueResolver<Character>() {
        @Override
        public Character resolve(String value) {
            return resolveChar(value, null);
        }
    };

    private static final StringValueResolver<Byte> _byte = new StringValueResolver<Byte>() {
        @Override
        public Byte resolve(String value) {
            if (StringUtil.isBlank(value)) {
                return (byte) 0;
            }
            return Byte.parseByte(value);
        }
    };
    private static final StringValueResolver<Byte> _Byte = new StringValueResolver<Byte>() {
        @Override
        public Byte resolve(String value) {
            if (StringUtil.isBlank(value)) {
                return null;
            }
            return Byte.parseByte(value);
        }
    };
    private static final StringValueResolver<Short> _short = new StringValueResolver<Short>() {
        @Override
        public Short resolve(String value) {
            if (StringUtil.isBlank(value)) {
                return (short) 0;
            }
            return Short.valueOf(value);
        }
    };
    private static final StringValueResolver<Short> _Short = new StringValueResolver<Short>() {
        @Override
        public Short resolve(String value) {
            if (StringUtil.isBlank(value)) {
                return null;
            }
            return Short.valueOf(value);
        }
    };

    private static int _int(String s) {
        if (s.contains(".")) {
            float f = Float.valueOf(s);
            return Math.round(f);
        } else {
            return Integer.valueOf(s);
        }
    }

    private static final StringValueResolver<Integer> _int = new StringValueResolver<Integer>() {
        @Override
        public Integer resolve(String value) {
            if (StringUtil.isBlank(value)) {
                return 0;
            }
            return _int(value);
        }
    };
    private static final StringValueResolver<Integer> _Integer = new StringValueResolver<Integer>() {
        @Override
        public Integer resolve(String value) {
            if (StringUtil.isBlank(value)) {
                return null;
            }
            return _int(value);
        }
    };
    private static final StringValueResolver<Long> _long = new StringValueResolver<Long>() {
        @Override
        public Long resolve(String value) {
            if (StringUtil.isBlank(value)) {
                return 0l;
            }
            return Long.valueOf(value);
        }
    };
    private static final StringValueResolver<Long> _Long = new StringValueResolver<Long>() {
        @Override
        public Long resolve(String value) {
            if (StringUtil.isBlank(value)) {
                return null;
            }
            return Long.valueOf(value);
        }
    };
    private static final StringValueResolver<Float> _float = new StringValueResolver<Float>() {
        @Override
        public Float resolve(String value) {
            if (StringUtil.isBlank(value)) {
                return 0f;
            }
            float n = Float.valueOf(value);
            if (Float.isInfinite(n) || Float.isNaN(n)) {
                throw new IllegalArgumentException("float value out of scope: " + value);
            }
            return n;
        }
    };
    private static final StringValueResolver<Float> _Float = new StringValueResolver<Float>() {
        @Override
        public Float resolve(String value) {
            if (StringUtil.isBlank(value)) {
                return null;
            }
            float n = Float.valueOf(value);
            if (Float.isInfinite(n) || Float.isNaN(n)) {
                throw new IllegalArgumentException("float value out of scope: " + value);
            }
            return n;
        }
    };
    private static final StringValueResolver<Double> _double = new StringValueResolver<Double>() {
        @Override
        public Double resolve(String value) {
            if (StringUtil.isBlank(value)) {
                return 0d;
            }
            double n = Double.valueOf(value);
            if (Double.isInfinite(n) || Double.isNaN(n)) {
                throw new IllegalArgumentException("double value out of scope: " + value);
            }
            return n;
        }
    };
    private static final StringValueResolver<Double> _Double = new StringValueResolver<Double>() {
        @Override
        public Double resolve(String value) {
            if (StringUtil.isBlank(value)) {
                return null;
            }
            double n = Double.valueOf(value);
            if (Double.isInfinite(n) || Double.isNaN(n)) {
                throw new IllegalArgumentException("double value out of scope: " + value);
            }
            return n;
        }
    };
    private static final StringValueResolver<String> _String = new StringValueResolver<String>() {
        @Override
        public String resolve(String value) {
            return value;
        }
    };

    private static Map<Class, StringValueResolver> predefined = Maps.newHashMap();
    {
        predefined.put(boolean.class, _boolean);
        predefined.put(Boolean.class, _Boolean);
        predefined.put(char.class, _char);
        predefined.put(Character.class, _Char);
        predefined.put(byte.class, _byte);
        predefined.put(Byte.class, _Byte);
        predefined.put(short.class, _short);
        predefined.put(Short.class, _Short);
        predefined.put(int.class, _int);
        predefined.put(Integer.class, _Integer);
        predefined.put(long.class, _long);
        predefined.put(Long.class, _Long);
        predefined.put(float.class, _float);
        predefined.put(Float.class, _Float);
        predefined.put(double.class, _double);
        predefined.put(Double.class, _Double);
        predefined.put(String.class, _String);
        //        predefined.put(BigInteger.class, new BigIntegerValueObjectCodec());
        //        predefined.put(BigDecimal.class, new BigDecimalValueObjectCodec());
        //        predefined.put(Keyword.class, new KeywordValueObjectCodec());
    }

    public static <T> void addPredefinedResolver(Class<T> type, StringValueResolver<T> resolver) {
        predefined.put(type, resolver);
    }

    public static Map<Class, StringValueResolver> predefined() {
        return predefined;
    }

    @SuppressWarnings("unchecked")
    public static <T> StringValueResolver<T> predefined(Class<T> type) {
        return predefined.get(type);
    }

}
