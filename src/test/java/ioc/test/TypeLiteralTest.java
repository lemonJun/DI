package ioc.test;

import org.junit.Test;

import lemon.needle.ioc.TypeLiteral;

public class TypeLiteralTest {

    @Test
    public void testtype() {
        try {
            TypeLiteral type = TypeLiteral.get(Bll.class);
            System.out.println(type.toString());
            TypeLiteral type2 = TypeLiteral.get(Bll2.class);
            System.out.println(type2.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
