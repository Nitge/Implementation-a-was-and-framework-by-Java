package main.java.was.framework.view;

import main.java.was.framework.view.node.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateParser {
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "\\{\\{(?<type>#each|#if|/each|/if)?\\s*(?<key>\\w+)?\\s*\\}\\}");
    // \\{\\{ = {{로 시작
    // (#each|#if|/each//if)? = 타입 그룹, 0번 혹은 1번 등장
    // \\s* = 공백 0번 이상 등장 가능
    // (\\w+)? = key 그룹, 글자가 1개 이상인 단어가 0번 혹은 1번 등장
    // \\s* = 공백 0번 이상 등장 가능
    // \\}\\} = }}로 끝

    public static List<Node> parse(String template) {
        Stack<List<Node>> stack = new Stack<>();
        List<Node> root = new ArrayList<>();
        stack.push(root);

        Matcher matcher = TOKEN_PATTERN.matcher(template);
        int lastIndex = 0; // 이전에 매칭된 문자열의 끝을 가리키는 변수

        // 패턴과 일치하는 문자열이 존재하는 동안 반복
        while (matcher.find()) {

            // 매칭된 패턴 사이에 있는 일반 텍스트들을 텍스트 노드로 만듦
            // 매칭된 패턴이 연속으로 나왔을 경우 lastInedx == matcher.start()이므로 동작하지 않음
            if (lastIndex < matcher.start()) {
                String text = template.substring(lastIndex, matcher.start());
                stack.peek().add(new TextNode(text));
            }

            String type = matcher.group("type"); // #each, #if, /each, /if, or null
            String key = matcher.group("key");  // 변수명 or 조건명 or 리스트명

            if(type == null) {
                type = "";
            }

            switch (type) {
                case "#if":
                    List<Node> ifChildren = new ArrayList<>();
                    stack.peek().add(new IfNode(key, ifChildren));
                    stack.push(ifChildren);
                    break;
                case "/if":
                    stack.pop();
                    break;
                case "#each":
                    List<Node> eachChildren = new ArrayList<>();
                    stack.peek().add(new EachNode(key, eachChildren));
                    stack.push(eachChildren);
                    break;
                case "/each":
                    stack.pop();
                    break;
                default:
                    stack.peek().add(new VariableNode(key));
            }

            lastIndex = matcher.end(); // 찾은 패턴 바로 다음의 인덱스로 갱신
        }

        // 마지막 매칭된 패턴 다음으로 일반 텍스트가 남았으면 텍스트 노드로 만들어서 마무리 처리
        if (lastIndex < template.length()) {
            stack.peek().add(new TextNode(template.substring(lastIndex)));
        }

        return root;
    }
}
