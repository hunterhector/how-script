package edu.cmu.cs.lti.how.script.alignment;

import com.google.common.collect.ArrayListMultimap;
import edu.cmu.cs.lti.how.model.script.ScriptClusterLeaveNode;
import edu.cmu.cs.lti.how.model.script.ScriptClusterNode;
import edu.cmu.cs.lti.how.model.script.ScriptClusterNonTerminalNode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.sStu.AlignerFactory;
import org.sStu.Alignment;
import org.sStu.SingleAlingmentAligner;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 10/9/14
 * Time: 3:58 PM
 */
public class ScriptAlignmentCluster {
    Logger logger = Logger.getLogger(ScriptAlignmentCluster.class.getName());

    private ArrayListMultimap<String, String> filename2Sents;

    private Map<String, String> sent2Rep;

    //fast aligner for hac
    private SingleAlingmentAligner aligner;

    public ScriptAlignmentCluster(File eventRepreFile, File allSentsFile, File mappingFile) throws IOException {
        logger.setLevel(Level.INFO);
        info("Loading data");
        loadALlEventRepre(eventRepreFile, allSentsFile, mappingFile);
        info("Prepare aligner");
        getAligner();
    }

    public ScriptClusterNode hac() {
        info("Start clustering");

        List<String> allScriptNames = new LinkedList<>(filename2Sents.keySet());
        List<ScriptClusterNode> allScripts = new LinkedList<>();

        for (String scriptName : allScriptNames) {
            allScripts.add(new ScriptClusterLeaveNode(filename2Sents.get(scriptName), sent2Rep));
        }

        while (allScripts.size() != 1) {
            cluster(allScripts);
        }

        return allScripts.get(0);
    }

    public void cluster(List<ScriptClusterNode> allScripts) {
        double maxScore = Double.MIN_VALUE;
        int mergei = -1, mergej = -1;
        Alignment bestAlignment = null;
        logger.info("Finding best from " + allScripts.size() + " scripts");

        for (int i = 0; i < allScripts.size() - 1; i++) {
            double progressPercent = i * 1.0 / allScripts.size();
            updateProgress(progressPercent);
            for (int j = i + 1; j < allScripts.size(); j++) {
                aligner.setSequence(allScripts.get(i).getSequence(), 0);
                aligner.setSequence(allScripts.get(j).getSequence(), 1);
                aligner.align();

                //normalize by the length of the aligned string
                double score = aligner.getAlignmentScore() / aligner.getBestAlignment().getMiddleString().length();
                Alignment alignment = aligner.getBestAlignment();
                if (score > maxScore) {
                    maxScore = score;
                    mergei = i;
                    mergej = j;
                    bestAlignment = alignment;
                }
            }
        }

        //clear the progress bar line
        System.err.println();

        allScripts.set(mergei, new ScriptClusterNonTerminalNode(allScripts.get(mergei), allScripts.get(mergej), bestAlignment));
        allScripts.remove(mergej);
    }

    private void updateProgress(double progressPercentage) {
        final int width = 50; // progress bar width in chars
        System.err.print("\r[");
        int i = 0;
        for (; i <= (int) (progressPercentage * width); i++) {
            System.err.print(".");
        }
        for (; i < width; i++) {
            System.err.print(" ");
        }
        System.err.print("]");
    }


    private void getAligner() {
        AlignerFactory factory = new AlignerFactory();
        factory.setGap_weight(0);
        factory.setMatch_weight(1);
        factory.setMismatch_weigth(-1);
        factory.setGlobal(true);
        factory.setEnd_gap_penalty(false);
        factory.setGapped(false);
        factory.setShow_Ends(false);
        factory.setComparator(new EmbeddingSimilarityComparator());
        aligner = factory.createSingleAlingmentAligner();
    }

    private void loadALlEventRepre(File eventRepreFile, File allSentsFile, File mappingFile) throws IOException {
        filename2Sents = ArrayListMultimap.create();
        sent2Rep = new HashMap<>();

        info("Loading filename to argument event string mapping...");
        List<String> mappingStrs = FileUtils.readLines(mappingFile);

        for (String line : mappingStrs) {
            if (line.trim().equals("")) {
                continue;
            }
            String[] parts = line.trim().split("\t", 5);
            filename2Sents.put(parts[0], parts[4]);
        }

        info("Loading representations by sentences...");
        List<String> allSents = FileUtils.readLines(allSentsFile);
        List<String> allReps = FileUtils.readLines(eventRepreFile);
        for (int i = 0; i < allReps.size(); i++) {
            String repLine = allReps.get(i);
            String sentLine = allSents.get(i).trim().split("\t", 3)[2];
//            double[] v = loadVector(repLine);
            sent2Rep.put(sentLine, repLine);
        }
        info("Finish loading");
    }

    private void info(String msg) {
        logger.info(msg);
    }

    public static void main(String args[]) throws IOException, SAXException, ParserConfigurationException {
        if (args.length < 2) {
            System.err.println("Provide the data path and base output path as arguments");
            System.exit(1);
        }

        File eventRepreFile = new File(args[0]);
        File allSentsFile = new File(args[1]);
        File mappingFile = new File(args[2]);
        File outputFile = new File(args[3]);

        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

        ScriptAlignmentCluster aligner = new ScriptAlignmentCluster(eventRepreFile, allSentsFile, mappingFile);
        aligner.info("Start clustering");
        ScriptClusterNode root = aligner.hac();
        SerializationUtils.serialize(root, new FileOutputStream(outputFile));
    }
}
