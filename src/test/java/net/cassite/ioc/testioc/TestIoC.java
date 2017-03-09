//package net.cassite.ioc.testioc;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
//
//import java.io.IOException;
//import java.util.Properties;
//
//import org.apache.log4j.PropertyConfigurator;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import net.cassite.pure.ioc.AutoWire;
//import net.cassite.pure.ioc.Injector;
//import net.cassite.pure.ioc.Scope;
//
///**
// * test ioc
// */
//public class TestIoC {
//    private static EmptyBean emptyBean;
//
//    static {
//        PropertyConfigurator.configure("D:/log4j.properties");
//    }
//
//    @BeforeClass
//    public static void classSetUp() throws IOException {
//        // IOCController.autoRegister();
//        Scope rootScope = Injector.rootScope;
//        rootScope.registerInstance("testConst", new TestIoC());
//        rootScope.register("testVar", scope -> AutoWire.get(BeanFactory.class).getBean());
//        rootScope.register("testVar2", scope -> BeanFactory.text);
//
//        //        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//        //        rootScope.bind(ApplicationContext.class, session -> ctx);
//
//        Properties properties = new Properties();
//        properties.load(TestIoC.class.getResourceAsStream("/test.properties"));
//        rootScope.registerProperties("prop", properties);
//
//        emptyBean = new EmptyBean();
//        rootScope.bind(EmptyBean.class, scope -> emptyBean);
//        // IOCController.closeRegistering();
//    }
//
//    @Test
//    public void testAutoWire() {
//        BeanA a = new BeanA();
//        assertNotNull(a.getB());
//    }
//
//    @Test
//    public void testSingleton() {
//        try {
//            BeanB b1 = AutoWire.get(BeanB.class);
//            BeanB b2 = AutoWire.get(BeanB.class);
//            assertTrue(b1.getC() == b2.getC());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testWire() {
//        BeanB b = new BeanB(1.0);
//        assertNull(b.getC());
//        BeanB b2 = AutoWire.get(BeanB.class);
//        assertNotNull(b2.getC());
//    }
//
//    @Test
//    public void testCircularDep() {
//        BeanA a = new BeanA();
//        try {
//            a.getB().getC().getA().getB().getC().getA();
//            assertTrue(a.getB().getC() == a.getB().getC().getA().getB().getC());
//        } catch (NullPointerException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void testTypeDefault() {
//        BeanC c = AutoWire.get(BeanC.class);
//        assertNotNull(c.getD());
//        assertTrue(c.getD() instanceof BeanD);
//    }
//
//    @Test
//    public void testConstructorDefault() {
//        BeanC c = AutoWire.get(BeanC.class);
//        assertEquals(1, c.getI());
//    }
//
//    @Test
//    public void testSetterForce() {
//        BeanA a = new BeanA();
//        assertEquals("x", a.getX());
//    }
//
//    @Test
//    public void testParamForce() {
//        BeanB b = AutoWire.get(BeanB.class);
//        assertEquals(2.0, b.getD(), 0);
//    }
//
//    @Test
//    public void testIgnore() {
//        BeanA a = AutoWire.get(BeanA.class);
//        assertNull(a.getD());
//    }
//
//    @Test
//    public void testUse() {
//        BeanFactory.text = "text1";
//        BeanD d1 = new BeanD();
//        BeanFactory.text = "text2";
//        BeanD d2 = new BeanD();
//
//        assertEquals("text1", d1.getText());
//        assertEquals("text2", d2.getText());
//
//        assertNotNull(d1.getA());
//        assertNotNull(d2.getA());
//        assertNull(d1.getA().getB());
//        assertNull(d2.getA().getB());
//        assertTrue(d1.getTestIoC() == d2.getTestIoC());
//    }
//
//    @Test
//    public void testReturnBeforeInvokingAOP() {
//        ReturnBeforeInvokingAOPBean bean = AutoWire.get(ReturnBeforeInvokingAOPBean.class);
//        assertEquals("doAop", bean.getName());
//        bean.setName("123");
//        assertEquals("doAop", bean.getName());
//    }
//
//    @Test
//    public void testChangeArgBeforeInvokingAOP() {
//        ChangeArgBeforeInvokingAOPBean bean = AutoWire.get(ChangeArgBeforeInvokingAOPBean.class);
//        bean.setName("test");
//        assertEquals("test|aop", bean.getName());
//    }
//
//    @Test
//    public void testChangeReturnAfterInvokingAOP() {
//        ChangeReturnAfterInvokingAOPBean bean = AutoWire.get(ChangeReturnAfterInvokingAOPBean.class);
//        bean.setName("test");
//        assertEquals("test|aop2", bean.getName());
//    }
//
//    @Test
//    public void testThrowAOP() {
//        ThrowAOPBean bean = AutoWire.get(ThrowAOPBean.class);
//        bean.setName("test");
//        assertEquals("doAop", bean.getName());
//    }
//
//    @Test
//    public void testAllAOP() {
//        AllAOPBean bean = AutoWire.get(AllAOPBean.class);
//        bean.setName("test");
//        assertEquals("test|aop|aop2", bean.getName());
//        assertEquals("doAop", bean.name());
//    }
//
//    @Test
//    public void testTargetAware() {
//        TargetAwareAOPBean bean = AutoWire.get(TargetAwareAOPBean.class);
//        bean.doSomething();
//    }
//
//    @Test
//    public void testSession() {
//        Bean1 bean1 = AutoWire.get(Bean1.class);
//        assertTrue(bean1.getBeanA() == bean1.getBean2().getBeanA());
//        assertTrue(bean1.getBeanA() == bean1.getBean3().getBeanA());
//    }
//
//    //    @Test
//    //    public void testBind() {
//    //        TestBindBean bean = new TestBindBean();
//    //        assertNotNull(bean.getContext());
//    //        assertNotNull(bean.getContext().getBean("springTestBean"));
//    //
//    //        assertEquals(ctx.getBean("springTestBean"), bean.getContext().getBean("springTestBean"));
//    //    }
//
//    //    @Test
//    //    public void testExtendAndSpring() {
//    //        SpringExtTestBean bean = AutoWire.get(SpringExtTestBean.class);
//    //        //        assertTrue(ctx.getBean("springTestBean") == bean.getBean());
//    //        assertTrue(bean.getBean() == bean.getBean2());
//    //        assertTrue(bean.getBean() == bean.getBean3());
//    //    }
//
//    @Test
//    public void testForceProperty() {
//        TestForceProperty bean = new TestForceProperty();
//        assertEquals(1, bean.getA());
//        assertEquals("b", bean.getB());
//        assertEquals('c', bean.getC());
//    }
//
//    @Test
//    public void testPrimitive() {
//        PrimitiveBean bean = AutoWire.get(PrimitiveBean.class);
//        assertEquals(0, bean.getI());
//        assertEquals(0, bean.getD(), 0);
//    }
//
//    @Test
//    public void testFieldInjection() {
//        FieldInjectBean bean = new FieldInjectBean();
//        assertEquals(1, bean.integer);
//        assertEquals("a", bean.string);
//    }
//
//    @Test
//    public void testNonClassWire() {
//        NonClassWire bean = AutoWire.get(NonClassWire.class);
//        assertEquals(1, bean.getInteger());
//        assertEquals("a", bean.string);
//    }
//
//    @Test
//    public void testScopeAware() {
//        ScopeAwareBean bean = AutoWire.get(ScopeAwareBean.class);
//        assertNotNull(bean.getScope());
//        assertNotNull(bean.getScope().getBondInstance(Scope.class));
//    }
//
//    @Test
//    public void testWireLogic() {
//        TestWireLogicBean beanNon = AutoWire.get(TestWireLogicBean.class);
//        TestWireLogicBeanWithWireOnClass beanCls = AutoWire.get(TestWireLogicBeanWithWireOnClass.class);
//        TestWireLogicBeanWithWireOnField beanField = AutoWire.get(TestWireLogicBeanWithWireOnField.class);
//        TestWireLogicBeanWithWireOnSetter beanSetter = AutoWire.get(TestWireLogicBeanWithWireOnSetter.class);
//        assertNull(beanNon.string);
//        assertEquals(0, beanNon.integer);
//
//        assertNull(beanCls.string);
//        assertEquals(1, beanCls.integer);
//
//        assertEquals("a", beanField.string);
//        assertEquals(0, beanField.integer);
//
//        assertNull(beanSetter.string);
//        assertEquals(1, beanSetter.integer);
//    }
//
//    @Test
//    public void testThreadScope() {
//        ThreadScopeBean bean = AutoWire.get(ThreadScopeBean.class);
//        assertEquals("a", bean.string);
//
//        ThreadScopeBean2 bean2 = AutoWire.get(ThreadScopeBean2.class);
//        assertEquals("a", bean2.string);
//    }
//
//    @Test
//    public void testDestroy() throws InterruptedException {
//        DestroyBean bean = AutoWire.get(DestroyBean.class);
//        assertEquals("a", bean.getS());
//        Thread.sleep(110);
//        assertNull(bean.getS());
//    }
//
//    //    @Test
//    //    public void testBindWhenConstructingIsAlsoApproval() {
//    //        TestBindBean bindBean = AutoWire.get(TestBindBean.class);
//    //        assertEquals(emptyBean, bindBean.bean);
//    //    }
//}
