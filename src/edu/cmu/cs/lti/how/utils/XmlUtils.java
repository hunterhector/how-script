package edu.cmu.cs.lti.how.utils;

import edu.cmu.cs.lti.how.model.ContentElement;
import edu.cmu.cs.lti.how.model.TextElement;
import edu.cmu.cs.lti.how.model.WikihowLink;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 9/20/14
 * Time: 1:39 PM
 */
public class XmlUtils {
    public static List<WikihowLink> elements2Links(NodeList nodes) {
        List<WikihowLink> strs = new ArrayList<WikihowLink>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                strs.add(new WikihowLink((Element)node));
            }
        }
        return strs;
    }

    public static List<String> elements2Strs(NodeList nodes) {
        List<String> strs = new ArrayList<String>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                strs.add(node.getTextContent().trim());
            }
        }
        return strs;
    }

    public static List<TextElement> elements2AnnotatedText(NodeList nodes) {
        List<TextElement> strs = new ArrayList<TextElement>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                strs.add(new TextElement(node));
            }
        }
        return strs;
    }

    public static List<ContentElement> elements2Content(NodeList nodes) {
        List<ContentElement> contents = new ArrayList<ContentElement>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            contents.add(new ContentElement(node));
        }
        return contents;
    }
}
