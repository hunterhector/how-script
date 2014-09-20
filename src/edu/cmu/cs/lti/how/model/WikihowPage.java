package edu.cmu.cs.lti.how.model;

import edu.cmu.cs.lti.how.utils.Joiners;
import edu.cmu.cs.lti.how.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.PrintStream;
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
    public WikihowPage(Element documentNode, File f) {
        this.originalFileName = f.getName();

        System.out.println("Procesing " + originalFileName);

        NodeList nodes = documentNode.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            Element rootChild;
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                rootChild = (Element) n;
            } else {
                continue;
            }
            String nodeName = rootChild.getNodeName();

            if (nodeName.equals("meta")) {
                meta = new WikihowPageMeta(rootChild);
            } else if (nodeName.equals("categories")) {
                categories = XmlUtils.elements2Strs(rootChild.getElementsByTagName("c"));
                System.out.println("" +
                        "\t- Category: " + Joiners.slashJoiner.join(categories));
            } else if (nodeName.equals("alter_lang")) {
                alternativeLanguages = XmlUtils.elements2AnnotatedText(rootChild.getElementsByTagName("lang"));
            } else if (nodeName.equals("general")) {
                NodeList generalChildren = rootChild.getChildNodes();
                for (int j = 0; j < generalChildren.getLength(); j++) {
                    Node generalChild = generalChildren.item(j);
                    String generalChildName = generalChild.getNodeName();
                    if (generalChildName.equals("title")) {
                        title = generalChild.getTextContent().trim();
                    } else if (generalChildName.equals("summary")) {
                        summary = new ContentElement(generalChild);
                    }
                }
            } else if (nodeName.equals("methods")) {
                wikihowMethods = new ArrayList<WikihowMethod>();
                NodeList methodChildren = rootChild.getChildNodes();
                for (int j = 0; j < methodChildren.getLength(); j++) {
                    Node methodChild = methodChildren.item(j);
                    wikihowMethods.add(new WikihowMethod(methodChild));
                }
            } else if (nodeName.equals("warnings")) {
                warnings = XmlUtils.elements2Content(rootChild.getChildNodes());
            } else if (nodeName.equals("tips")) {
                tips = XmlUtils.elements2Content(rootChild.getChildNodes());
            } else if (nodeName.equals("ingredients")) {
                ingredients = XmlUtils.elements2Content(rootChild.getChildNodes());
            } else if (nodeName.equals("things")) {
                things = XmlUtils.elements2Content(rootChild.getChildNodes());
            } else if (nodeName.equals("related_pages")) {
                relatedPages = new ArrayList<WikihowLink>();
                NodeList pages = rootChild.getChildNodes();
                XmlUtils.elements2Links(pages);
            }
        }
    }

    private String originalFileName;
    private WikihowPageMeta meta;
    private List<TextElement> alternativeLanguages;
    private List<String> categories;
    private String title;
    private ContentElement summary;
    private List<WikihowMethod> wikihowMethods;
    private List<ContentElement> tips;
    private List<ContentElement> things;
    private List<ContentElement> ingredients;
    private List<ContentElement> warnings;
    private List<WikihowLink> relatedPages;

    public void prettyPrint(PrintStream out) {
        out.println(asFormattedStr());
    }

    public String asFormattedStr() {
        StringBuilder builder = new StringBuilder();
        builder.append(title).append("\n");
        builder.append("\t- Summary: " + "\n");
        builder.append("\t\t- ").append(summary.getAllText()).append("\n");
        builder.append("\t- Meta: ").append("\n");
        builder.append("\t\t- Keywords: ").append(Joiners.commaJoiner.join(meta.getKeyWords())).append("\n");
        builder.append("\t\t- Title: ").append((meta.getTitle())).append("\n");
        builder.append("\t\t- Description: ").append((meta.getDescription())).append("\n");
        builder.append("\t\t- Type: ").append((meta.getType())).append("\n");
        builder.append("\t\t- URL: ").append((meta.getUrl())).append("\n");
        builder.append("\t-Alternative language: ").append((Joiners.commaJoiner.join(alternativeLanguages))).append("\n");
        builder.append("\t- Category: ").append(Joiners.slashJoiner.join(categories));
        builder.append("\t- Methods: ").append("\n");
        builder.append("\t\t- Method:").append("\n");
        builder.append("\t\t\t").append(Joiners.nlJoiner.join(wikihowMethods)).append("\n");
        return builder.toString();
    }

    public WikihowPageMeta getMeta() {
        return meta;
    }

    public void setMeta(WikihowPageMeta meta) {
        this.meta = meta;
    }

    public List<TextElement> getAlternativeLanguages() {
        return alternativeLanguages;
    }

    public void setAlternativeLanguages(List<TextElement> alternativeLanguages) {
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
