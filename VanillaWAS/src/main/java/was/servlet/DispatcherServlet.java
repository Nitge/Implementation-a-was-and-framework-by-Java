package main.java.was.servlet;

import main.java.was.framework.container.BeanContainer;
import main.java.was.framework.container.Config;
import main.java.was.framework.handler.adapter.HandlerAdapter;
import main.java.was.framework.handler.adapter.ModelAndView;
import main.java.was.framework.handler.mapping.ControllerAndMethod;
import main.java.was.framework.handler.mapping.HandlerMapping;
import main.java.was.framework.handler.mapping.HttpMethodAndPath;
import main.java.was.framework.view.View;
import main.java.was.framework.view.ViewResolver;
import main.java.was.http.HttpRequest;
import main.java.was.http.HttpResponse;
import main.java.was.http.header.ContentType;
import main.java.was.http.header.HttpStatus;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;

import static java.nio.charset.StandardCharsets.*;
import static main.java.util.Logger.log;

public class DispatcherServlet implements HttpServlet{

    private final BeanContainer beanContainer;
    private final HandlerMapping handlerMapping;
    private final HandlerAdapter handlerAdapter;
    private final ViewResolver viewResolver;

    public DispatcherServlet() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // 실제 서블릿 <--> 스프링은 dispatcher-servlet.xml로 초기 설정을 하지만 여기선 간소화
        this.beanContainer = new BeanContainer(new Config());
        this.handlerMapping = (HandlerMapping) beanContainer.getBean(HandlerMapping.class);
        this.handlerMapping.init();
        this.handlerAdapter = (HandlerAdapter) beanContainer.getBean(HandlerAdapter.class);
        this.viewResolver = (ViewResolver) beanContainer.getBean(ViewResolver.class);
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {

        try {
            if (tryHandleStaticResource(request, response)) {
                return;
            }
            tryHandleByController(request, response);
        } catch (IOException e) {
            handleError(HttpStatus.INTERNAL_SERVER_ERROR, response);
        }

    }

    private boolean tryHandleStaticResource(HttpRequest request, HttpResponse response) throws IOException {
        // 정적 리소스 가져오기
        // jar로 배포시 클래스로더를 사용해서 클래스패스 상대경로를 기준으로 가져와야 함
        String path = request.getPath();
        // root 경로 호출 시 처리
        if(path.equals("/")) {
            path = "/index.html";
        }
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        fileName = URLDecoder.decode(fileName, UTF_8);
        try (InputStream resourceAsStream = getClass()
                .getClassLoader()
                .getResourceAsStream("main/resources/static/" + fileName)) {

            if(resourceAsStream == null || fileName.isEmpty()) {
                return false;
            }

            // 경로에 정적 리소스가 존재할 경우
            // 확장자 구하기 ("." 문자가 위치한 인덱스의 1칸 뒤에서부터 문자열 끝까지의 substring을 반환)
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            response.setHttpStatus(HttpStatus.OK);
            // 확장자로 ContentType을 구해서 넣어줌
            response.setContentType(ContentType.valueOf(extension.toUpperCase()));

            // 다운로드로 처리할 경우 헤더 설정
            // response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
            try (BufferedInputStream in = new BufferedInputStream(resourceAsStream);
                 OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                response.flush();
            }
            log("파일 전송 완료: " + fileName);
            return true;
        }
    }

    private void tryHandleByController(HttpRequest request, HttpResponse response) throws IOException {
        // 존재하지 않는 경로이거나 파일이 아닐 경우 컨트롤러 위임 로직 수행
        String path = request.getPath();
        ControllerAndMethod controllerAndMethod = this.handlerMapping
                .getControllerAndMethod(new HttpMethodAndPath(request.getMethod(), path));

        if(controllerAndMethod == null) {
            handleError(HttpStatus.NOT_FOUND, response);
            return;
        }

        ModelAndView mav = this.handlerAdapter.handle(request, response, controllerAndMethod);
        String viewName = mav.getViewName();

        // redirect 처리
        if (viewName.startsWith("redirect:")) {
            String redirectPath = viewName.substring("redirect:".length());
            response.setHttpStatus(HttpStatus.FOUND);
            response.setHeader("Location", redirectPath);
            response.flush();
            return;
        }

        // 렌더링 처리
        View view = this.viewResolver.resolveView(viewName);
        view.render(request, response, mav.getModel());
    }

    private void handleError(HttpStatus httpStatus, HttpResponse response) {
        PrintWriter writer = response.getWriter();
        response.setHttpStatus(httpStatus);
        response.setContentType(ContentType.PLAIN);
        writer.println(httpStatus);
        writer.flush();
    }
}
