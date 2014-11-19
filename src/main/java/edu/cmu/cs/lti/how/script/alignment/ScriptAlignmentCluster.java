package edu.cmu.cs.lti.how.script.alignment;

import com.google.common.collect.ArrayListMultimap;
import edu.cmu.cs.lti.how.model.script.ScriptCluster;
import edu.cmu.cs.lti.how.model.script.ScriptClusterLeaveNode;
import edu.cmu.cs.lti.how.model.script.ScriptClusterNode;
import edu.cmu.cs.lti.how.model.script.ScriptClusterNonTerminalNode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang3.tuple.Pair;
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

    private ArrayListMultimap<String, String> filename2Events;

    private Map<String, double[]> event2Rep;

    private double cutoff = 0.5;

    //fast aligner for hac
    private SingleAlingmentAligner aligner;

    public ScriptAlignmentCluster(File eventRepreFile, File allSentsFile, File mappingFile, double cutoff) throws IOException {
        logger.setLevel(Level.INFO);
        info("Loading data");
        loadALlEventRepre(eventRepreFile, allSentsFile, mappingFile);
        info("Prepare aligner");
        getAligner();

        if (cutoff >= 0) {
            this.cutoff = cutoff;
            info("Setting cutoff as " + cutoff);
        }
    }

    public ScriptCluster hac() {
        List<String> allScriptNames = new LinkedList<>(filename2Events.keySet());
        List<ScriptClusterNode> allScripts = new LinkedList<>();

        info("Add script nodes");
        for (String scriptName : allScriptNames) {
            allScripts.add(new ScriptClusterLeaveNode(filename2Events.get(scriptName), event2Rep));

        }

        info("Start clustering");
        double lastMax = 1;

        while (lastMax > cutoff && allScripts.size() > 1) {
            lastMax = cluster(allScripts, lastMax);
        }
        info("Cut off at " + lastMax);

        return new ScriptCluster(allScripts);
    }

    public double cluster(List<ScriptClusterNode> allScripts, double lastMax) {
        Pair<Pair<Integer, Integer>, Pair<Alignment, Double>> best = findBest(allScripts, lastMax);
        int mergei = best.getKey().getLeft();
        int mergej = best.getKey().getRight();
        Alignment bestAlignment = best.getValue().getLeft();
        double score = best.getValue().getRight();

        System.err.println();
        System.err.println("Current best " + score);
        System.err.println("[Script " + mergei + "]" + allScripts.get(mergei));
        System.err.println("[Script " + mergej + "]" + allScripts.get(mergej));

        allScripts.set(mergei, new ScriptClusterNonTerminalNode(allScripts.get(mergei), allScripts.get(mergej), bestAlignment));
        allScripts.remove(mergej);

        return score;
    }

    private Pair<Pair<Integer, Integer>, Pair<Alignment, Double>> findBest(List<ScriptClusterNode> allScripts, double limit) {
        double maxScore = Double.MIN_VALUE;
        int mergei = -1, mergej = -1;
        Alignment bestAlignment = null;
        logger.info("Finding best from " + allScripts.size() + " scripts");

        for (int i = 0; i < allScripts.size() - 1; i++) {
            System.err.print("Iter: " + i + "\r");
            for (int j = i + 1; j < allScripts.size(); j++) {
                double[][] seq0 = allScripts.get(i).getSequence();
                double[][] seq1 = allScripts.get(j).getSequence();

                aligner.setSequence(allScripts.get(i).getSequence(), 0);
                aligner.setSequence(allScripts.get(j).getSequence(), 1);
                aligner.align();

                int longerLen = seq0.length > seq1.length ? seq0.length : seq1.length;

                //normalize by the length of the longer string
                double score = aligner.getAlignmentScore() / longerLen;

                Alignment alignment = aligner.getBestAlignment();
                if (score > maxScore) {
                    maxScore = score;
                    mergei = i;
                    mergej = j;
                    bestAlignment = alignment;
                    if (score >= limit) {
                        return Pair.of(Pair.of(mergei, mergej), Pair.of(bestAlignment, score));
                    }
                }
            }
        }

        return Pair.of(Pair.of(mergei, mergej), Pair.of(bestAlignment, maxScore));
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
        filename2Events = ArrayListMultimap.create();
        event2Rep = new HashMap<>();

        info("Loading filename to argument event string mapping...");
        List<String> mappingStrs = FileUtils.readLines(mappingFile);

        for (String line : mappingStrs) {
            if (line.trim().equals("")) {
                continue;
            }
            String[] parts = line.trim().split("\t", 5);

            filename2Events.put(parts[0], parts[4]);
        }

        info("Loading representations by sentences...");
        List<String> allSents = FileUtils.readLines(allSentsFile);
        List<String> allReps = FileUtils.readLines(eventRepreFile);
        for (int i = 0; i < allReps.size(); i++) {
            String eventStr = allSents.get(i).trim().split("\t", 3)[2];
            String repLine = allReps.get(i);
            double[] v = loadVector(repLine);
            event2Rep.put(eventStr, v);
        }
        info("Finish loading");
    }

    private double[] loadVector(String repLine) {
        String[] vectorStrs = repLine.split(" ");
        double[] v = new double[vectorStrs.length];
        for (int i = 0; i < vectorStrs.length; i++) {
            v[i] = Double.parseDouble(vectorStrs[i]);
        }
        return v;
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
        double cutoff = Double.parseDouble(args[4]);

        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

        ScriptAlignmentCluster aligner = new ScriptAlignmentCluster(eventRepreFile, allSentsFile, mappingFile, cutoff);
        ScriptCluster root = aligner.hac();
        SerializationUtils.serialize(root, new FileOutputStream(outputFile));
    }
}
