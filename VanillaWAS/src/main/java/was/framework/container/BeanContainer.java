package main.java.was.framework.container;

import main.java.was.framework.annotation.Bean;
import main.java.was.framework.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static main.java.util.Logger.log;

public class BeanContainer {
    // config 클래스를 찾아서 @Bean이 붙어있는 메소드로부터 객체를 가져와서 등록
    private final Map<Class<?>, Object> beans = new HashMap<>();
    private final Map<Class<?>, Method> methodMap = new HashMap<>();
    private final Config config;

    public BeanContainer(Config config) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        this.config = config;

        // BeanContainer 자기자신을 요구하는 Bean을 위해 처리
        this.beans.put(BeanContainer.class, this);
        this.methodMap.put(BeanContainer.class, BeanContainer.class.getDeclaredMethod("getSelf"));

        init(config);
    }

    private BeanContainer getSelf() {
        return this;
    }

    private void init(Config config) throws InvocationTargetException, IllegalAccessException {

        Class<? extends Config> aClass = config.getClass();
        assert aClass.isAnnotationPresent(Configuration.class);

        //구성 파일에서 Bean을 반환하는 메소드들을 가져옴
        Method[] declaredMethods = aClass.getDeclaredMethods();

        // 재귀함수에서 원하는 Bean을 반환하는 메소드를 먼저 실행하기 위해 매핑해놓음
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Bean.class)) {
                methodMap.put(method.getReturnType(), method);
            }
        }

        // 각 메소드들을 실행해서 Bean으로 등록 시작
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Bean.class)) {
                registerBean(method);
            }
        }
    }

    private void registerBean(Method method) throws InvocationTargetException, IllegalAccessException {

        Object bean;

        // Bean이 이미 등록되어 있으면 무시
        if(beans.containsKey(method.getReturnType())) {
            return;
        }

        // 파라미터가 없으면 바로 실행 후 Bean으로 등록
        if(method.getParameterCount() <= 0) {
            bean = method.invoke(config);
        } else {
            // 파라미터가 있을 경우 그 Bean을 컨테이너에서 찾아보고 없으면 먼저 등록 처리(재귀함수)
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];

            for(int i = 0; i < parameterTypes.length; i++) {
                if(!beans.containsKey(parameterTypes[i])) {
                    registerBean(methodMap.get(parameterTypes[i]));
                }
                args[i] = beans.get(parameterTypes[i]);
            }

            bean = method.invoke(config, args);
        }

        Class<?>[] beansInterfaces = bean.getClass().getInterfaces();

        if(beansInterfaces.length == 0) { // 구현체가 아닐 경우
            beans.put(bean.getClass(), bean);
        } else { // 구현체일 경우
            beans.put(beansInterfaces[0], bean);
        }

        log(bean + ": Bean 등록완료");
    }

    public Map<Class<?>, Object> getBeans() {
        return beans;
    }

    public Object getBean(Class<?> clazz)  {
        return this.beans.get(clazz);
    }
}
