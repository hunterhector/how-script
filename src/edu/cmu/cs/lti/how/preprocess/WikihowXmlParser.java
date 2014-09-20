package edu.cmu.cs.lti.how.preprocess;

import edu.cmu.cs.lti.how.model.WikihowMethod;
import edu.cmu.cs.lti.how.model.WikihowPage;
import edu.cmu.cs.lti.how.model.WikihowStep;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * It didn't do much parsing, it just let all the Dom element resolve at each level
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 9/18/14
 * Time: 3:29 PM
 */
public class WikihowXmlParser {

    private Iterator<File> iter;


    private DocumentBuilder db;

    public WikihowXmlParser() throws ParserConfigurationException {
        db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    public WikihowXmlParser(File f) throws ParserConfigurationException {
        this();
        if (f.isDirectory()) {
            System.out.println("Processing a directory");
            readWholeDirectory(f);
        } else {
            List<File> d = new ArrayList<File>();
            d.add(f);
            makeIterator(d);
        }
    }

    public WikihowPage parseSingleXml(File f) throws IOException, SAXException {
        Document dom = db.parse(f);
        Element root = dom.getDocumentElement();
        NodeList nodes = root.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("document")) {
                return new WikihowPage( (Element)node, f);
            }
        }

        System.err.println("Error parsing XML : "+f.getAbsolutePath());
        return null;
    }

    public void readWholeDirectory(File d) {
        final String[] SUFFIX = {"xml"};
        Collection<File> xmls = FileUtils.listFiles(
                d,
                SUFFIX,
                true
        );

        makeIterator(xmls);
    }

    private void makeIterator(Collection<File> xmls) {
        iter = xmls.iterator();
    }

    public WikihowPage parseNext() throws IOException, SAXException {
        if (iter == null || !iter.hasNext()) {
            throw new IllegalStateException("Nothing to parse!");
        }
        return parseSingleXml(iter.next());
    }

    public boolean hasNext() {
        return iter != null && iter.hasNext();
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        if (args.length < 2) {
            System.err.println("Provide the data path and output path as arguments");
            System.exit(1);
        }

        File dataPath = new File(args[0]);
        File outputDir = new File(args[1]);

        WikihowXmlParser parser = new WikihowXmlParser(dataPath);

        while (parser.hasNext()) {
            WikihowPage page = parser.parseNext();
            if (page != null) {
                List<String> steps = new ArrayList<String>();
                for (WikihowMethod method : page.getWikihowMethods()) {
                    for (WikihowStep step : method.getSteps()) {
                        steps.add(step.getStep().getAllText());
                    }
                }

                page.prettyPrint(System.out);
                FileUtils.writeLines(new File(outputDir.getAbsolutePath() + "/" + page.getOriginalFileName() + ".txt"), steps);
            }

        }
    }
}
