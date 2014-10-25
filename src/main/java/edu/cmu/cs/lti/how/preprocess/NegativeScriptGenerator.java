package edu.cmu.cs.lti.how.preprocess;

import edu.cmu.cs.lti.how.model.wikihow.WikihowMethod;
import edu.cmu.cs.lti.how.model.wikihow.WikihowPage;
import edu.cmu.cs.lti.how.model.wikihow.WikihowStep;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 10/10/14
 * Time: 7:59 PM
 */
public class NegativeScriptGenerator {


    boolean doReplace = false;
    Random rand = new Random();

    public NegativeScriptGenerator(boolean doReplace) {
        this.doReplace = doReplace;
    }

    enum GenerateMethod {
        origin, shuffle, replace, remove
    }


    public void generate(File dataPath, File outputDir) throws ParserConfigurationException, IOException, SAXException {
        WikihowXmlParser parser = new WikihowXmlParser(dataPath, false);
        //store all steps in the whole domain, random steps are drawn from this pool
        List<String> stepLibrary = new ArrayList<>();

        Map<String, List<String>> stepsByName = new HashMap<>();

        while (parser.hasNext()) {
            WikihowPage page = parser.parseNext();
            if (page != null) {
                int mid = 0;
                for (WikihowMethod method : page.getWikihowMethods()) {
                    String name = page.getOriginalFileName() + "_" + mid;
                    List<String> steps = toStepText(method);
                    if (steps.size() >= 3) {
                        FileUtils.writeLines(new File(outputDir.getAbsolutePath() + "/shuffled/" + name + ".txt"), shuffle(steps));
                        FileUtils.writeLines(new File(outputDir.getAbsolutePath() + "/origin/" + name + ".txt"), steps);
                        FileUtils.writeLines(new File(outputDir.getAbsolutePath() + "/removed/" + name + ".txt"), remove(steps));

                        if (doReplace) {
                            stepsByName.put(name, steps);
                            stepLibrary.addAll(steps);
                        }
                    }
                    mid++;
                }
            }
        }

        if (doReplace) {
            for (Map.Entry<String, List<String>> entry : stepsByName.entrySet()) {
                FileUtils.writeLines(new File(outputDir.getAbsolutePath() + "/replaced/" + entry.getKey() + ".txt"), replace(entry.getValue(), stepLibrary));
            }
        }
    }


    private List<String> toStepText(WikihowMethod method) {
        List<String> steps = new ArrayList<>();
        for (WikihowStep step : method.getSteps()) {
            String stepText = step.getStep().getAllText().trim();
            if (!stepText.trim().equals("")) {
                steps.add(stepText);
            }
        }
        return steps;
    }

    private List<String> remove(List<String> steps) {
        //make a new list cuz shuffle is in-place
        List<String> removedList = new ArrayList<>(steps);

        int indexToRemove = rand.nextInt(steps.size());

        removedList.remove(indexToRemove);

        return removedList;
    }


    private List<String> shuffle(List<String> steps) {
        //make a new list cuz shuffle is in-place
        List<String> shuffledList = new ArrayList<>(steps);
        Collections.shuffle(shuffledList);
        return shuffledList;
    }

    private List<String> replace(List<String> steps, List<String> replacePool) {
        List<String> replacedList = new ArrayList<>(steps);

        int indexToChoose = rand.nextInt(replacePool.size());

        int indexToReplace = rand.nextInt(steps.size());

        replacedList.set(indexToReplace, replacePool.get(indexToChoose));

        return replacedList;
    }


    public static void main(String args[]) throws IOException, SAXException, ParserConfigurationException {
        if (args.length < 2) {
            System.err.println("Provide the data path and base output path as arguments");
            System.exit(1);
        }

        File dataFile = new File(args[0]);
        File outputDir = new File(args[1]);
        boolean doRepalce = args.length > 2;

        NegativeScriptGenerator generator = new NegativeScriptGenerator(doRepalce);
        generator.generate(dataFile, outputDir);
    }
}
