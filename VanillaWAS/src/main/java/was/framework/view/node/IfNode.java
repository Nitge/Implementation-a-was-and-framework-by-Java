package main.java.was.framework.view.node;

import main.java.was.framework.handler.adapter.Model;

import java.util.List;
import java.util.stream.Collectors;

public class IfNode implements Node {

    private final String conditionKey;
    private final List<Node> children;

    public IfNode(String conditionKey, List<Node> children) {
        this.conditionKey = conditionKey;
        this.children = children;
    }

    @Override
    public String render(Model model) {
        // 모델에서 조건을 가져와 참일 경우 자식 노드를 모두 렌더링
        Object condition = model.getAttribute(conditionKey);
        if(condition instanceof Boolean && (Boolean) condition) {
            return children.stream()
                    .map(node -> node.render(model))
                    .collect(Collectors.joining());
        }
        return "";
    }
}
