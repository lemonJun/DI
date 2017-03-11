package ioc.test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lemon.needle.ioc.TypeLiteral;

public class Type2 {

    public void type() {
        try {
            List<String> list = new ArrayList<String>();
            Type p = TypeLiteral.getSuperclassTypeParameter(list.getClass());
            System.out.println(p.getTypeName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        try {

            TypeLiteral lite = new TypeLiteral<ArrayList<String>>();
            System.out.println(lite.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
