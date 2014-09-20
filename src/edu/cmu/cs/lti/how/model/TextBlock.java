package edu.cmu.cs.lti.how.model;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TextBlock implements Serializable {
    public TextBlock(Node topNode) {
        list = new ArrayList<TextElement>();

        if (topNode.getNodeName().equals("ul")){
            NodeList ul = topNode.getChildNodes();
            for (int i = 0 ; i < ul.getLength() ; i ++){
                Node li = ul.item(i);
                if (li.getNodeType() == Node.ELEMENT_NODE && li.getNodeName().equals("li")) {
                    list.add(new TextElement(li));
//                    System.out.println(t);
                }
            }
        }else{
            list.add(new TextElement(topNode));
        }
    }

    private List<TextElement> list;

    public List<TextElement> getList() {
        return list;
    }

    public void setList(List<TextElement> list) {
        this.list = list;
    }

    public String getText(){
        StringBuilder builder = new StringBuilder();
        for (TextElement t : list){
            builder.append(t.getText());
        }
        return builder.toString();
    }

    public String toString(){
        return getText();
    }
}