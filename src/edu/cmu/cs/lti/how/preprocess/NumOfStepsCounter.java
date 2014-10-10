package edu.cmu.cs.lti.how.preprocess;

import edu.cmu.cs.lti.how.model.wikihow.WikihowPage;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 10/9/14
 * Time: 8:34 PM
 */
public class NumOfStepsCounter {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        if (args.length < 2) {
            System.err.println("Provide the data path and base output path as arguments");
            System.exit(1);
        }

        File dataPath = new File(args[0]);
        File outputFile = new File(args[1]);


        boolean debug = true;
        WikihowXmlParser parser = new WikihowXmlParser(dataPath, debug);

        OutputStreamWriter writer = new OutputStreamWriter( FileUtils.openOutputStream(outputFile));



        while (parser.hasNext()) {
            WikihowPage page = parser.parseNext();
            if (page != null) {
                int numSteps = page.getWikihowMethods().size();
                String url = page.getMeta().getUrl().toString();
                writer.write(url+"\t"+numSteps+"\n");
            }
        }
    }
}
