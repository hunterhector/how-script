package edu.cmu.cs.lti.how.script.alignment;

import edu.cmu.cs.lti.how.model.script.ScriptCluster;
import edu.cmu.cs.lti.how.model.script.ScriptClusterLeaveNode;
import edu.cmu.cs.lti.how.model.script.ScriptClusterNode;
import edu.cmu.cs.lti.how.model.script.ScriptClusterNonTerminalNode;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 12/3/14
 * Time: 10:01 PM
 */
public class AlignmentReader {
    File outDir;

    String basename;

    int counter;

    public AlignmentReader(File outDir, String basename) {
        this.outDir = outDir;
        this.basename = basename;
    }

    public void writeAlignment(List<String[]> alignments, int n) throws IOException {
        Writer w = new FileWriter(new File(outDir, basename + "_" + counter + ".csv"));
        counter++;

//        System.out.println("Dumping");
//        for (String[] alignment : alignments) {
//            System.out.println(Arrays.toString(alignment));
//        }

        for (int i = 0; i < n; i++) {
            StringBuilder sb = new StringBuilder();
            for (String[] alignment : alignments) {
                sb.append(alignment[i]);
                sb.append("\t,\t");
            }
            sb.append("\n");
            writeline(w, "", sb.toString());
        }
        w.close();
    }

    public void writeNode(ScriptClusterLeaveNode node) throws IOException {
        Writer w = new FileWriter(new File(outDir, "single_" + basename + "_" + counter));
        counter++;
        for (String event : node.getEvents()) {
            writeline(w, "", event);
        }
        w.close();
    }

    public void check(ScriptClusterNode node, int depth) throws IOException {
        String prefix = StringUtils.repeat("  ", depth);

        if (node.isLeave()) {
            ScriptClusterLeaveNode leaveNode = (ScriptClusterLeaveNode) node;
            writeNode(leaveNode);
        } else {
            ScriptClusterNonTerminalNode nonTerminalNode = (ScriptClusterNonTerminalNode) node;

            if (nonTerminalNode.getSource().size() <= 20) {
//                System.out.println(nonTerminalNode.getObservationCount());
                writeAlignment(getAlignments(nonTerminalNode), nonTerminalNode.getSource().size());
            } else {
                check(node.getLeft(), depth + 1);
                check(node.getRight(), depth + 1);
            }
        }
    }

    private List<String[]> getAlignments(ScriptClusterNonTerminalNode node) {
        List<Pair<Integer, Integer>> source = node.getSource();

        int n = source.size();

        int[] leftFrom = new int[n];
        int[] rightFrom = new int[n];

        for (int i = 0; i < n; i++) {
            leftFrom[i] = source.get(i).getLeft();
            rightFrom[i] = source.get(i).getRight();
        }

//        System.out.println("Left from: " + Arrays.toString(leftFrom));

        List<String[]> leftAlignments = getAlignments(node.getLeft(), n, leftFrom, 1);

//        System.out.println("Right from: " + Arrays.toString(rightFrom));

        List<String[]> rightAlignments = getAlignments(node.getRight(), n, rightFrom, 1);

        List<String[]> alignments = new ArrayList<>();
        alignments.addAll(leftAlignments);
        alignments.addAll(rightAlignments);
        return alignments;
    }

    private List<String[]> getAlignments(ScriptClusterNode node, int n, int[] from, int depth) {
        String prefix = StringUtils.repeat("  ", depth);

        List<String[]> alignments = new ArrayList<>();

        if (node.isLeave()) {
            String[] events = new String[n];
            ScriptClusterLeaveNode lNode = (ScriptClusterLeaveNode) node;
            for (int i = 0; i < n; i++) {
                if (from[i] >= 0) {
                    events[i] = lNode.getEvents()[from[i]].trim();
                } else {
                    events[i] = "-";
                }
            }
            alignments.add(events);
        } else {
            ScriptClusterNonTerminalNode nonTerminalNode = (ScriptClusterNonTerminalNode) node;
            List<Pair<Integer, Integer>> sources = nonTerminalNode.getSource();

            int[] leftFrom = new int[n];
            int[] rightFrom = new int[n];

            for (int i = 0; i < n; i++) {
                if (from[i] >= 0) {
//                    System.out.println("From "+ i + " is "+ from[i]);
                    Pair<Integer, Integer> source = sources.get(from[i]);
//                    System.out.println(source);
                    leftFrom[i] = source.getLeft();
                    rightFrom[i] = source.getRight();
                } else {
                    leftFrom[i] = -1;
                    rightFrom[i] = -1;
                }
            }
//            System.out.println(prefix + "Left from: " + Arrays.toString(leftFrom));

            List<String[]> leftAlignments = getAlignments(node.getLeft(), n, leftFrom, depth + 1);

//            System.out.println(prefix + "Right from: " + Arrays.toString(rightFrom));

            List<String[]> rightAlignments = getAlignments(node.getRight(), n, rightFrom, depth + 1);

            alignments.addAll(leftAlignments);
            alignments.addAll(rightAlignments);
        }

        return alignments;
    }

    private void writeline(Writer writer, String prefix, String content) throws IOException {
        write(writer, prefix, content + "\n");
    }

    private void write(Writer writer, String prefix, String content) throws IOException {
        writer.write(prefix + content);
    }

    public static void main(String[] args) throws IOException {
        String basename = "alignment_cluster";
        File outDir = new File("data/cluster_out");
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        ScriptCluster cluster = (ScriptCluster) SerializationUtils.deserialize(new FileInputStream(new File("data/alignment_out.ser")));

        for (int i = 0; i < cluster.getCluster().size(); i++) {
            ScriptClusterNode node = cluster.getCluster().get(i);
            AlignmentReader reader = new AlignmentReader(outDir, basename + i);
            reader.check(node, 0);
        }
    }
}
