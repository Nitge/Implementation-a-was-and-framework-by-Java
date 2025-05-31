package main.java.was.framework.handler.mapping;

import main.java.was.framework.annotation.Mapping;
import main.java.was.framework.container.BeanContainer;
import main.java.was.http.header.HttpMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {

    private final Map<HttpMethodAndPath, ControllerAndMethod> controllerMapping = new HashMap<>();
    private final BeanContainer beanContainer;

    // Bean 컨테이너에서 Beans를 가져와 메소드에 Mapping 어노테이션이 붙은 클래스들을 탐색
    public HandlerMapping(BeanContainer beanContainer) {
        this.beanContainer = beanContainer;
        //init(beanContainer);
    }

    public void init() {
        Map<Class<?>, Object> beans = this.beanContainer.getBeans();

        // Bean으로 등록된 클래스를 모두 순회
        for (Class<?> beanClass : beans.keySet()) {
            Method[] declaredMethods = beanClass.getDeclaredMethods();
            // 가져온 Bean 클래스의 메소드를 모두 순회
            for (Method declaredMethod : declaredMethods) {
                // Mapping이 붙어있는 메소드가 있을 경우 컨트롤러로 판단
                if(declaredMethod.isAnnotationPresent(Mapping.class)) {
                    // 메소드에 매핑된 경로를 가져옴
                    Mapping mapping = declaredMethod.getAnnotation(Mapping.class);

                    HttpMethod httpMethod = mapping.method();
                    String path ="";

                    // 컨트롤러 클래스에 매핑이 달린 경우 경로의 맨 앞에 추가
                    if(beanClass.isAnnotationPresent(Mapping.class)) {
                        path = beanClass.getAnnotation(Mapping.class).value();
                    }
                    path += mapping.value();

                    HttpMethodAndPath hm = new HttpMethodAndPath(httpMethod, path);

                    // 중복 매핑 발생 시 예외를 던짐
                    if(controllerMapping.containsKey(hm)) {
                        ControllerAndMethod controllerAndMethod = controllerMapping.get(hm);
                        throw new IllegalArgumentException("[경로 중복 등록] " +
                                "method: " + hm.getHttpMethod() +
                                "path: " + hm.getPath() +
                                "\n이미 등록된 메소드: " + controllerAndMethod.getMethod() +
                                "\n등록을 시도한 메소드: " + declaredMethod);
                    }

                    Object controller = beans.get(beanClass);
                    // 경로와 메소드, 그걸 실행할 컨트롤러 Bean을 매핑
                    controllerMapping.put(hm, new ControllerAndMethod(controller, declaredMethod));

                }
            }
        }
    }

    public ControllerAndMethod getControllerAndMethod(HttpMethodAndPath httpMethodAndPath) {
        return controllerMapping.get(httpMethodAndPath);
    }

}
