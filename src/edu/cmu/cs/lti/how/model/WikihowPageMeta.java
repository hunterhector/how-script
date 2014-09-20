package edu.cmu.cs.lti.how.model;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 9/19/14
 * Time: 10:21 PM
 */
public class WikihowPageMeta implements Serializable {
    public WikihowPageMeta(Node topNode){
        NodeList nodes = topNode.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String nodeName = node.getNodeName();
            String text = node.getTextContent();
            if (nodeName.equals("description")){
                description = text;
            }else if (nodeName.equals("keywords")){
                keyWords = new ArrayList<String>();
                Collections.addAll(keyWords, text.split(","));
            }else if (nodeName.equals("title")){
                title = text;
            }else if (nodeName.equals("type")){
                type = text;
            }else if (nodeName.equals("url")){
                try {
                    url = new URL(text);
                } catch (MalformedURLException e) {
                    System.err.println("Can't parse url: "+text);
                }
            }
        }
    }

    private String description;
    private List<String> keyWords;
    private String title;
    private String type;
    private URL url;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
