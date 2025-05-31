package main.java.was.framework.handler.adapter;

import main.java.was.framework.annotation.Param;
import main.java.was.framework.handler.mapping.ControllerAndMethod;
import main.java.was.http.HttpRequest;
import main.java.was.http.HttpResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static main.java.util.Logger.log;

public class HandlerAdapter {

    public ModelAndView handle(HttpRequest request, HttpResponse response, ControllerAndMethod controllerAndMethod) throws IOException {

        ModelAndView mav = new ModelAndView();
        Model model = new Model();
        String viewName;

        // 컨트롤러에 넘겨줄 메소드 설정
        Object controller = controllerAndMethod.getController();
        Method method = controllerAndMethod.getMethod();

        Parameter[] parameters = method.getParameters();
        Object[] arguments = new Object[parameters.length];

        // 메소드에 넘겨줄 매개변수 설정
        for (int i = 0; i < parameters.length; i++) {
            if(parameters[i].getType() == HttpRequest.class) {
                arguments[i] = request;
            } else if(parameters[i].getType() == HttpResponse.class) {
                arguments[i] = response;
            } else if(parameters[i].getType() == Model.class) {
                arguments[i] = model;
            } else if(parameters[i].isAnnotationPresent(Param.class)) {
                String paramKey = parameters[i].getAnnotation(Param.class).value();
                arguments[i] = request.getQueryParameters().get(paramKey);
            } else {
                log(method + "컨트롤러 매개 변수 오류: " + parameters[i]);
                throw new IOException();
            }
        }

        // 컨트롤러의 메소드 실행
        try {
            viewName = (String) method.invoke(controller, arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // 컨트롤러가 비즈니스 로직 수행한 결과인 모델과 이를 표현할 뷰의 이름 반환
        mav.setModel(model);
        mav.setViewName(viewName);
        return mav;
    }

}
