package edu.cmu.cs.lti.how.model.script.alignment;

import ch.usi.inf.sape.hac.HierarchicalAgglomerativeClusterer;
import ch.usi.inf.sape.hac.agglomeration.AgglomerationMethod;
import ch.usi.inf.sape.hac.agglomeration.CentroidLinkage;
import ch.usi.inf.sape.hac.dendrogram.*;
import ch.usi.inf.sape.hac.experiment.DissimilarityMeasure;
import ch.usi.inf.sape.hac.experiment.Experiment;
import com.google.common.collect.ArrayListMultimap;
import org.apache.commons.io.FileUtils;
import org.sStu.AlignerFactory;
import org.sStu.ScoreOnlyAligner;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

    private Map<String, double[]> sent2Rep;

    //fast aligner for hac
    private ScoreOnlyAligner aligner;


    public ScriptAlignmentCluster(File eventRepreFile, File allSentsFile, File mappingFile) throws IOException {
        logger.setLevel(Level.INFO);
        info("Loading data");
        loadALlEventRepre(eventRepreFile, allSentsFile, mappingFile);
        info("Prepare aligner");
        getAligner();
        info("Prepare output");


    }

    public void cluster(File outputFile) throws IOException {
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        //implement HAC that calls alignment here
        List<String> allScripts = new ArrayList<>(filename2Sents.keySet());
        Experiment experiment = new ScriptAlignExperiment(allScripts.size());
        DissimilarityMeasure dissimilarityMeasure = new ScriptEmbeddingDissmilarityMeasure(allScripts, aligner, filename2Sents);
        AgglomerationMethod agglomerationMethod = new CentroidLinkage();
        DendrogramBuilder dendrogramBuilder = new DendrogramBuilder(experiment.getNumberOfObservations());
        HierarchicalAgglomerativeClusterer clusterer = new HierarchicalAgglomerativeClusterer(experiment, dissimilarityMeasure, agglomerationMethod);
        clusterer.cluster(dendrogramBuilder);
        Dendrogram dendrogram = dendrogramBuilder.getDendrogram();
        dumpDendrogramWithFileName(" ", dendrogram.getRoot(), outputFile, allScripts);
    }

    private void dumpDendrogramWithFileName(final String indent, final DendrogramNode node, File outputFile, List<String> allScripts) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

        if (node == null) {
            writer.write(indent + "<null>");
        } else if (node instanceof ObservationNode) {
            System.out.println(indent + "Observation: " + node);
        } else if (node instanceof MergeNode) {
            System.out.println(indent + "Merge:");
            dumpDendrogramWithFileName(indent + "  ", ((MergeNode) node).getLeft(), outputFile, allScripts);
            dumpDendrogramWithFileName(indent + "  ", ((MergeNode) node).getRight(), outputFile, allScripts);
        }
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
        factory.setComparator(new EmbeddingSimilarityComparator(sent2Rep));
        aligner = factory.createAlignmentRangeAligner();
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
            double[] v = loadVector(repLine);
            sent2Rep.put(sentLine, v);
        }

        info("Finish loading");
    }

    private void info(String msg) {
        logger.info(msg);
    }

    private double[] loadVector(String line) {
        String[] vec = line.split(" ");

        double[] vecInt = new double[vec.length];

        for (int i = 0; i < vec.length; i++) {
            vecInt[i] = Double.parseDouble(vec[i]);
        }

        return vecInt;
    }

    public static void main(String args[]) throws IOException, SAXException, ParserConfigurationException {
        if (args.length < 2) {
            System.err.println("Provide the data path and base output path as arguments");
            System.exit(1);
        }

//        String[] scriptFileExtentions = {".txt"};
        File eventRepreFile = new File(args[0]);
        File allSentsFile = new File(args[1]);
        File mappingFile = new File(args[2]);
        File outputFile = new File(args[3]);

        ScriptAlignmentCluster aligner = new ScriptAlignmentCluster(eventRepreFile, allSentsFile, mappingFile);
        aligner.info("Start clustering");
        aligner.cluster(outputFile);
    }

}
