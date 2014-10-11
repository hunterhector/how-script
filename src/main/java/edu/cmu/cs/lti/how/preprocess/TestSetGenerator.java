package edu.cmu.cs.lti.how.preprocess;

import edu.cmu.cs.lti.how.model.wikihow.WikihowMethod;
import edu.cmu.cs.lti.how.model.wikihow.WikihowPage;
import edu.cmu.cs.lti.how.model.wikihow.WikihowStep;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 10/10/14
 * Time: 7:59 PM
 */
public class TestSetGenerator {
    public TestSetGenerator(){
    }

    public void filter(File dataPath, File outputDir) throws ParserConfigurationException, IOException, SAXException {
        WikihowXmlParser parser = new WikihowXmlParser(dataPath, false);

        while (parser.hasNext()) {
            WikihowPage page = parser.parseNext();
            if (page != null) {
                int mid = 0;
                for (WikihowMethod method : page.getWikihowMethods()) {
                    if (method.getSteps().size() >= 3){
                        List<String> steps = new ArrayList<String>();
                        for (WikihowStep step : method.getSteps()) {
                            steps.add(step.getStep().getAllText());
                        }


                    }
                    mid++;
                }
            }
        }
    }

    private void shuffle(List<String> steps){

    }

    private void replace(List<String> steps){

    }


    public static void main(String args[]) throws IOException, SAXException, ParserConfigurationException {
        if (args.length < 2) {
            System.err.println("Provide the data path and base output path as arguments");
            System.exit(1);
        }

        File dataFile = new File(args[0]);
        File outputDir = new File(args[1]);

        TestSetGenerator filter = new TestSetGenerator();

        filter.filter(dataFile, outputDir);
    }
}
