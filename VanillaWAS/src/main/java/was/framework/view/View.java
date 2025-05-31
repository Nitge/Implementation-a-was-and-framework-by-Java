package main.java.was.framework.view;

import main.java.was.framework.handler.adapter.Model;
import main.java.was.framework.view.node.Node;
import main.java.was.http.HttpRequest;
import main.java.was.http.HttpResponse;
import main.java.was.http.header.ContentType;
import main.java.was.http.header.HttpStatus;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.*;
import static main.java.util.Logger.log;

public class View {

    private final String viewName;

    public View(String viewName) {
        this.viewName = viewName;
    }

    public void render(HttpRequest request, HttpResponse response, Model model) throws IOException {
        PrintWriter writer = response.getWriter();

        // viewName으로 템플릿 가져오기
        // jar로 배포시 클래스로더를 사용해서 클래스패스 상대경로를 기준으로 가져와야 함
        String templateString;
        try (InputStream in = getClass()
                .getClassLoader()
                .getResourceAsStream("main/resources/templates/" + viewName + ".html")) {
            if(in == null) {
                throw new FileNotFoundException();
            }
            templateString = new String(in.readAllBytes(), UTF_8);
        }

        // TemplateParser로 ast 만들기
        List<Node> nodes = TemplateParser.parse(templateString);

        // 받은 list<node> 렌더링하기
        String result = nodes.stream()
                .map(node -> node.render(model))
                .collect(Collectors.joining());

        // response에 직접 쓰기
        response.setHttpStatus(HttpStatus.OK);
        response.setContentType(ContentType.HTML);
        writer.print(result);
        writer.flush();
    }
}
