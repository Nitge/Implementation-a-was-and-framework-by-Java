package main.java.was.framework.view.node;

import main.java.was.framework.handler.adapter.Model;

import java.util.List;

public class EachNode implements Node{

    private final String listKey;
    private final List<Node> children;

    public EachNode(String listKey, List<Node> children) {
        this.listKey = listKey;
        this.children = children;
    }

    @Override
    public String render(Model model) {

        Object obj = model.getAttribute(listKey);

        if(obj instanceof Iterable<?>) {
            StringBuilder sb = new StringBuilder();

            // 기존 Model + 1 .. n 번 아이템이 들어간 subModel을 만듦
            // subModel별로 children을 한번씩 렌더링해서 StringBuilder에 합쳐주기
            for(Object item : (Iterable<?>) obj) {
                Model subModel = new Model(model);
                subModel.addAttribute("this", item);
                for(Node child : children) {
                    sb.append(child.render(subModel));
                }
            }
            return sb.toString();
        }
        return "";
    }
}
