package edu.cmu.cs.lti.how.script.alignment;

import com.google.common.collect.ArrayListMultimap;
import edu.cmu.cs.lti.collections.BidirectionalValueSortedMap;
import edu.cmu.cs.lti.how.model.script.ScriptCluster;
import edu.cmu.cs.lti.how.model.script.ScriptClusterLeaveNode;
import edu.cmu.cs.lti.how.model.script.ScriptClusterNode;
import edu.cmu.cs.lti.how.model.script.ScriptClusterNonTerminalNode;
import edu.cmu.cs.lti.utils.DebugUtils;
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
import java.util.*;
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

//    private TreeMap<Pair<Integer, Integer>, Pair<Double, Alignment>> sortedAlignments = new ValueComparableMap<>(Ordering.natural().reverse());

    private BidirectionalValueSortedMap<Pair<Integer, Integer>, Pair<Double, Alignment>> sortedAlignments = new BidirectionalValueSortedMap<>();

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

//        int temp = 5;
        for (String scriptName : allScriptNames) {
            allScripts.add(new ScriptClusterLeaveNode(filename2Events.get(scriptName), event2Rep));

//            temp--;
//            allScripts.add(new ScriptClusterLeaveNode(filename2Events.get(scriptName), event2Rep));
//            allScripts.add(new ScriptClusterLeaveNode(filename2Events.get(scriptName), event2Rep));
//            if (temp < 0) {
//                break;
//            }
        }

        boolean[] deleted = new boolean[allScripts.size()];
        int numDeleted = 0;

        info("Start clustering");
        double lastMax = 1;

        info("Calculate all pairwise similarity and store...");
        calAllPair(allScripts);

        while (lastMax > cutoff && numDeleted < allScripts.size() - 1) {
            lastMax = cluster(allScripts, deleted);
            //we delete one at a time;
            numDeleted++;
            info("Number of deleted nodes " + numDeleted);
        }
        info("Cut off at " + lastMax);
        info("Number of deleted nodes  " + numDeleted);

        List<ScriptClusterNode> clusteredNodes = new ArrayList<ScriptClusterNode>();
        for (int i = 0; i < allScripts.size(); i++) {
            if (!deleted[i]) {
                clusteredNodes.add(allScripts.get(i));
            }
        }

        info("Number of remaining clusters " + clusteredNodes.size());

        return new ScriptCluster(clusteredNodes);
    }

    public double cluster(List<ScriptClusterNode> allScripts, boolean[] deleted) {
        Pair<Double, Alignment> bestAlignmentResult;
        List<Pair<Integer, Integer>> pairs = new ArrayList<>();

        while (true) {
            Map.Entry<Pair<Double, Alignment>, Collection<Pair<Integer, Integer>>> best = sortedAlignments.pollLastEntry();
            if (best == null) {
                return -1;
            } else {
                bestAlignmentResult = best.getKey();
                for (Pair<Integer, Integer> pair : best.getValue()) {
                    //might have already been merged
                    if (!deleted[pair.getRight()] && !deleted[pair.getLeft()]) {
                        pairs.add(pair);
                    }
                }
                if (!pairs.isEmpty()) {
                    break;
                }
            }
        }

        merge(allScripts, deleted, pairs, bestAlignmentResult.getRight(), bestAlignmentResult.getLeft());
        return bestAlignmentResult.getLeft();
    }

    private void merge(List<ScriptClusterNode> allScripts, boolean[] deleted, List<Pair<Integer, Integer>> mergePairs, Alignment bestAlignment, double score) {
        System.err.println("Current best " + score);
        List<Set<Integer>> mergeClosureSets = new ArrayList<>();

        for (Pair<Integer, Integer> mergePair : mergePairs) {
            int mergei = mergePair.getLeft();
            int mergej = mergePair.getRight();
            System.err.println("[Script " + mergei + "]" + allScripts.get(mergei));
            System.err.println("[Script " + mergej + "]" + allScripts.get(mergej));

            boolean existed = false;
            for (Set<Integer> mergeSet : mergeClosureSets) {
                if (mergeSet.contains(mergei) || mergeSet.contains(mergej)) {
                    mergeSet.add(mergei);
                    mergeSet.add(mergej);
                    existed = true;
                    break;
                }
            }

            if (!existed) {
                TreeSet<Integer> mergeSet = new TreeSet<>();
                mergeSet.add(mergei);
                mergeSet.add(mergej);
                mergeClosureSets.add(mergeSet);
            }
        }

        for (Set<Integer> mergeSet : mergeClosureSets) {
            int previous = -1;
            for (int merge : mergeSet) {
                if (previous == -1) {
                    previous = merge;
                } else {
                    System.err.println("Merging " + previous + " and " + merge);
                    allScripts.set(previous, new ScriptClusterNonTerminalNode(allScripts.get(previous), allScripts.get(merge), bestAlignment));
                    deleted[merge] = true;
                    allScripts.set(merge, null);
                    System.err.println(merge + " is deleted ");
                }
            }
            if (previous != -1) {
                updateAlignment(allScripts, previous, deleted);
            }
        }
    }


    private void updateAlignment(List<ScriptClusterNode> allScripts, int indexToUpdate, boolean[] deleted) {
        logger.info("Update pairwise related to " + indexToUpdate);

        for (int i = 0; i < indexToUpdate; i++) {
            if (!deleted[i]) {
                Alignment alignment = align(allScripts.get(i), allScripts.get(indexToUpdate));
                double[][] seq0 = allScripts.get(i).getSequence();
                double[][] seq1 = allScripts.get(indexToUpdate).getSequence();
                int longerLen = seq0.length > seq1.length ? seq0.length : seq1.length;
                double score = alignment.getAlignmentScore() / longerLen;
                sortedAlignments.put(Pair.of(i, indexToUpdate), Pair.of(score, alignment));
            }
        }

        for (int j = indexToUpdate + 1; j < allScripts.size(); j++) {
            if (!deleted[j]) {
                Alignment alignment = align(allScripts.get(indexToUpdate), allScripts.get(j));
                double[][] seq0 = allScripts.get(indexToUpdate).getSequence();
                double[][] seq1 = allScripts.get(indexToUpdate).getSequence();
                int longerLen = seq0.length > seq1.length ? seq0.length : seq1.length;
                double score = alignment.getAlignmentScore() / longerLen;
                sortedAlignments.put(Pair.of(indexToUpdate, j), Pair.of(score, alignment));
            }
        }
    }

    private void calAllPair(List<ScriptClusterNode> allScripts) {
        logger.info("Calculate pairwise alignment score for all " + allScripts.size() + " scripts");
//        allAlignments = new Alignment[allScripts.size()][allScripts.size()];
        for (int i = 0; i < allScripts.size() - 1; i++) {
            if (i % 100 == 0) {
                DebugUtils.printMemInfo(logger, "Iter: " + i);
            }
            for (int j = i + 1; j < allScripts.size(); j++) {
                Alignment alignment = align(allScripts.get(i), allScripts.get(j));

                double[][] seq0 = allScripts.get(i).getSequence();
                double[][] seq1 = allScripts.get(j).getSequence();
                int longerLen = seq0.length > seq1.length ? seq0.length : seq1.length;
                double score = alignment.getAlignmentScore() / longerLen;

                sortedAlignments.put(Pair.of(i, j), Pair.of(score, alignment));
            }
        }
    }

    private Alignment align(ScriptClusterNode nodei, ScriptClusterNode nodej) {
        aligner.setSequence(nodei.getSequence(), 0);
        aligner.setSequence(nodej.getSequence(), 1);
        aligner.align();
        return aligner.getBestAlignment();
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
//        double length = 0;
        for (int i = 0; i < vectorStrs.length; i++) {
            double vi = Double.parseDouble(vectorStrs[i]);
            v[i] = vi;
//            length += vi * vi;
        }
//        length = Math.sqrt(length);
//        for (int i = 0; i < vectorStrs.length; i++) {
//            v[i] = v[i] / length;
//        }

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
