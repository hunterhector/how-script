package edu.cmu.cs.lti.how.model;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TextElement implements Serializable {
    public TextElement(Node topNode) {
        text = topNode.getTextContent().trim();
        annotations = new HashMap<String, String>();
        NamedNodeMap attributes = topNode.getAttributes();
        if (attributes!= null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attr = attributes.item(i);
                annotations.put(attr.getNodeName(), attr.getNodeValue());
            }
        }
        tagName = topNode.getNodeName();
    }

    private String text;
    private Map<String, String> annotations;
    private String tagName;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, String> getAnnotations() {
        return annotations;
    }

    public String getAnnotation(String field) {
        return annotations.containsKey(field) ?  annotations.get(field) : null;
    }

    public void setAnnotations(Map<String, String> annotations) {
        this.annotations = annotations;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }


    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        String sep = "";
        for (Map.Entry<String,String> anno: annotations.entrySet()){
            builder.append(sep);
            builder.append(anno.getKey()).append(":").append(anno.getValue());
            sep = " ";
        }
        return getText() + " ["+builder.toString()+"]";
    }
}