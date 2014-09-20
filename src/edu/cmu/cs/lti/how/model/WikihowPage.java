package edu.cmu.cs.lti.how.model;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A java bean that represent information mined from one wikihow page
 * <p/>
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 9/19/14
 * Time: 10:12 PM
 */
public class WikihowPage implements Serializable {
    public WikihowPage(Node documentNode, File f) {
        this.originalFileName = f.getName();

        System.out.println("Procesing "+originalFileName);

        NodeList nodes = documentNode.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node rootChild = nodes.item(i);

            String nodeName = rootChild.getNodeName();

            if (nodeName.equals("meta")) {
                meta = new WikihowPageMeta(rootChild);
            } else if (nodeName.equals("categories")) {
                categories = nodeList2Strs(rootChild.getChildNodes());
            } else if (nodeName.equals("alter_lang")){
                alternativeLanguages = nodeList2Strs(rootChild.getChildNodes());
            } else if (nodeName.equals("general")){
                NodeList generalChildren = rootChild.getChildNodes();
                for (int j = 0 ; j < generalChildren.getLength(); j++){
                    Node generalChild = generalChildren.item(j);
                    String generalChildName  = generalChild.getNodeName();
                    if (generalChildName.equals("title")){
                        title = generalChild.getTextContent();
                    }else if (generalChildName.equals("summary")){
                        summary = new ContentElement(generalChild);
                    }
                }
            } else if (nodeName.equals("methods")){
                wikihowMethods = new ArrayList<WikihowMethod>();
                NodeList methodChildren = rootChild.getChildNodes();
                for (int j = 0 ; j < methodChildren.getLength(); j++) {
                    Node methodChild = methodChildren.item(j);
                    wikihowMethods.add( new WikihowMethod(methodChild));
                }
            }else if (nodeName.equals("warnings")){
                warnings = nodeList2Contents(rootChild.getChildNodes());
            }else if (nodeName.equals("tips")){
                tips = nodeList2Contents(rootChild.getChildNodes());
            }else if (nodeName.equals("ingredients")){
                ingredients = nodeList2Contents(rootChild.getChildNodes());
            }else if (nodeName.equals("things")){
                things = nodeList2Contents(rootChild.getChildNodes());
            }else if (nodeName.equals("related_pages")){
                relatedPages = new ArrayList<WikihowLink>();

                NodeList pages = rootChild.getChildNodes();

                for (int j = 0; i < pages.getLength(); j++) {
                    Node page = pages.item(j);
                    relatedPages.add(new WikihowLink(page));
                }
            }
        }
    }

    public  List<String> nodeList2Strs(NodeList nodes){
        List<String> strs = new ArrayList<String>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            strs.add(node.getTextContent());
        }
        return strs;
    }

    public List<ContentElement> nodeList2Contents(NodeList nodes){
        List<ContentElement> contents = new ArrayList<ContentElement>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            contents.add(new ContentElement(node));
        }
        return contents;
    }

    private String originalFileName;
    private WikihowPageMeta meta;
    private List<String> alternativeLanguages;
    private List<String> categories;
    private String title;
    private ContentElement summary;
    private List<WikihowMethod> wikihowMethods;
    private List<ContentElement> tips;
    private List<ContentElement> things;
    private List<ContentElement> ingredients;
    private List<ContentElement> warnings;
    private List<WikihowLink> relatedPages;

    public WikihowPageMeta getMeta() {
        return meta;
    }

    public void setMeta(WikihowPageMeta meta) {
        this.meta = meta;
    }

    public List<String> getAlternativeLanguages() {
        return alternativeLanguages;
    }

    public void setAlternativeLanguages(List<String> alternativeLanguages) {
        this.alternativeLanguages = alternativeLanguages;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ContentElement getSummary() {
        return summary;
    }

    public void setSummary(ContentElement summary) {
        this.summary = summary;
    }

    public List<WikihowMethod> getWikihowMethods() {
        return wikihowMethods;
    }

    public void setWikihowMethods(List<WikihowMethod> wikihowMethods) {
        this.wikihowMethods = wikihowMethods;
    }

    public List<ContentElement> getTips() {
        return tips;
    }

    public void setTips(List<ContentElement> tips) {
        this.tips = tips;
    }

    public List<ContentElement> getThings() {
        return things;
    }

    public void setThings(List<ContentElement> things) {
        this.things = things;
    }

    public List<ContentElement> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<ContentElement> ingredients) {
        this.ingredients = ingredients;
    }

    public List<ContentElement> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<ContentElement> warnings) {
        this.warnings = warnings;
    }

    public List<WikihowLink> getRelatedPages() {
        return relatedPages;
    }

    public void setRelatedPages(List<WikihowLink> relatedPages) {
        this.relatedPages = relatedPages;
    }


    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
}
