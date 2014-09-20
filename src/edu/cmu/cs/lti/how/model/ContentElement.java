package edu.cmu.cs.lti.how.model;

import edu.cmu.cs.lti.how.utils.Joiners;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A block of text elements because HTML pages have a lot of tags
 * interwined, but multiple element might actually belongs together,
 * so the text in them should be used together
 * <p/>
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 9/19/14
 * Time: 11:05 PM
 */
public class ContentElement implements Serializable {
    public ContentElement(Node topNode) {
        this(topNode,false);
    }

    public ContentElement(Node topNode, boolean allowText) {
        textBlocks = new ArrayList<TextBlock>();

        NodeList children = topNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (allowText || child.getNodeType() == Node.ELEMENT_NODE) {
                textBlocks.add(new TextBlock(child));
            }
        }
    }

    private List<TextBlock> textBlocks;

    public String getAllText() {
        StringBuilder builder = new StringBuilder();
        for (TextBlock l : textBlocks) {
            builder.append(l.getText());
        }

        return builder.toString();
    }

    public void setTextBlocks(List<TextBlock> textBlocks) {
        this.textBlocks = textBlocks;
    }

    public List<TextBlock> getTextBlocks() {
        return textBlocks;
    }

    public String toString(){
        return Joiners.bJoiner.join(textBlocks);
    }
}