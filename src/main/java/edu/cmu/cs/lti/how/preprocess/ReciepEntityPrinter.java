package edu.cmu.cs.lti.how.preprocess;

import edu.cmu.cs.lti.how.model.wikihow.ContentElement;
import edu.cmu.cs.lti.how.model.wikihow.TextBlock;
import edu.cmu.cs.lti.how.model.wikihow.TextElement;
import edu.cmu.cs.lti.how.model.wikihow.WikihowPage;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Print out the Ingredient and Things You Will Need part from Wikihow, which are the main entities
 * involved in the cooking scripts
 * <p/>
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 10/24/14
 * Time: 11:33 PM
 */
public class ReciepEntityPrinter {

    private final static String noisyHeader = "Edit ";

    public void print(File dataPath, File outputDir) throws ParserConfigurationException, IOException, SAXException {
        WikihowXmlParser parser = new WikihowXmlParser(dataPath, false);


        while (parser.hasNext()) {
            WikihowPage page = parser.parseNext();
            if (page != null) {
                String name = page.getOriginalFileName();

                System.out.println("Outputing entities in "+page.getTitle());
                List<String> ingredientList = getIngredients(page);
                List<String> thingsList = getThings(page);

                if (ingredientList.size() != 0) {
                    FileUtils.writeLines(new File(outputDir.getAbsolutePath() + "/ingredients/" + name + ".txt"), ingredientList);
                }

                if (thingsList.size() != 0) {
                    FileUtils.writeLines(new File(outputDir.getAbsolutePath() + "/things/" + name + ".txt"), thingsList);
                }
            }

        }
    }


    private List<String> getThings(WikihowPage page) {
        List<ContentElement> things = page.getThings();
        List<String> thingsList = new ArrayList<>();

        if (things != null) {
            for (ContentElement thing : things) {
                for (TextBlock b : thing.getTextBlocks()) {
                    for (TextElement e : b.getList()) {
                        String anno = e.getAnnotation("n");
                        if (anno != null) {
                            if (anno.equals("p")) {
                                //heuristically, this is the serve size
                                String servingSize = "[Serving_size:] " + clean(e.getText());
                                thingsList.add(servingSize);
                            } else if (anno.equals("h3")) {
                                //heuristically, this is the ingredient header
                                thingsList.add("[FOR:] " + clean(e.getText()));
                            }
                        } else {
                            //list of real ingredients
                            thingsList.add(" -- " + clean(e.getText()));
                        }
                    }
                }
            }
        }

        return thingsList;
    }

    private List<String> getIngredients(WikihowPage page) {
        List<ContentElement> ingredients = page.getIngredients();
        ArrayList<String> ingredientList = new ArrayList<>();

        if (ingredients != null) {
            for (ContentElement ingredient : ingredients) {
                for (TextBlock b : ingredient.getTextBlocks()) {
                    for (TextElement e : b.getList()) {
                        String anno = e.getAnnotation("n");
                        if (anno != null) {
                            if (anno.equals("p")) {
                                //heuristically, this is the serve size
                                String servingSize = "[Serving_size:] " + clean(e.getText());
                                ingredientList.add(servingSize);
                            } else if (anno.equals("h3")) {
                                //heuristically, this is the ingredient header
                                ingredientList.add("[FOR:] " + clean(e.getText()));
                            }
                        } else {
                            //list of real ingredients
                            ingredientList.add(" -- " + clean(e.getText()));
                        }
                    }
                }
            }
        }

        return ingredientList;
    }

    private String clean(String origin) {
        origin = origin.trim();
        if (origin.startsWith(noisyHeader)) {
            origin = origin.substring(noisyHeader.length());
        }
        return origin;
    }


    public static void main(String args[]) throws IOException, SAXException, ParserConfigurationException {
        if (args.length < 2) {
            System.err.println("Provide the data path and base output path as arguments");
            System.exit(1);
        }

        File dataFile = new File(args[0]);
        File outputDir = new File(args[1]);

        ReciepEntityPrinter generator = new ReciepEntityPrinter();
        generator.print(dataFile, outputDir);
    }

}
