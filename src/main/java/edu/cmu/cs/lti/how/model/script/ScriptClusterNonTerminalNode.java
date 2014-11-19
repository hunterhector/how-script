package edu.cmu.cs.lti.how.model.script;

import org.apache.commons.lang3.tuple.Pair;
import org.sStu.Alignment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 11/17/14
 * Time: 3:03 PM
 */
public class ScriptClusterNonTerminalNode implements ScriptClusterNode {
    private static final long serialVersionUID = -4100926196981790956L;

    private final ScriptClusterNode leftNode;
    private final ScriptClusterNode rightNode;
    private final int numOberservations;

    private final double alignScore;

    private double[][] mergedSequences;

    private List<Pair<Integer, Integer>> source;

    public ScriptClusterNonTerminalNode(ScriptClusterNode l, ScriptClusterNode r, Alignment alignment) {
        this.leftNode = l;
        this.rightNode = r;
        numOberservations = l.getObservationCount() + r.getObservationCount();
        this.alignScore = alignment.getAlignmentScore();

//        System.out.println(alignScore);
//
//        if (l.isLeave()) {
//            ScriptClusterLeaveNode leaveL = (ScriptClusterLeaveNode) l;
//            System.out.println(Arrays.toString(leaveL.getEvents()));
//        }
//
//        if (r.isLeave()) {
//            ScriptClusterLeaveNode leaveR = (ScriptClusterLeaveNode) r;
//            System.out.println(Arrays.toString(leaveR.getEvents()));
//        }
//
//        System.out.println("Count " + l.getSequence().length + " " + r.getSequence().length);

        CharSequence ls = alignment.getSequence(0);
        CharSequence rs = alignment.getSequence(1);
        CharSequence ms = alignment.getMiddleString();

        CharSequence longerSeq;
        double[][] longerSeqContent;

        boolean leftLonger = true;
        if (ls.length() > rs.length()) {
            longerSeq = ls;
            longerSeqContent = l.getSequence();
        } else {
            longerSeq = rs;
            longerSeqContent = r.getSequence();
            leftLonger = false;
        }

        int mergedLength = longerSeqContent.length;

        for (int i = 0; i < ms.length(); i++) {
            char c = ms.charAt(i);
            if (c == ' ') {
                mergedLength += 1;
            }
        }

//        System.out.println("Merged length " + mergedLength);

        mergedSequences = new double[mergedLength][];

//        System.out.println(alignment);

        source = new ArrayList<>();

        int lenBefore = longerSeq.charAt(0) - '1';

        //before the aligned part
        for (int i = 0; i < lenBefore; i++) {
            mergedSequences[i] = longerSeqContent[i];
            if (leftLonger) {
                source.add(Pair.of(i, -1));
            } else {
                source.add(Pair.of(-1, i));
            }
        }

        //the aligned part
        int leftPointer = 0;
        int rightPointer = 0;
        for (int i = 0; i < ms.length(); i++) {
            char c = ms.charAt(i);
            if (c == '|') {
                //exact match
                leftPointer = ls.charAt(i) - '1';
                rightPointer = rs.charAt(i) - '1';
                mergedSequences[lenBefore + i] = l.getSequence()[leftPointer];
                source.add(Pair.of(leftPointer, rightPointer));
            } else if (c == ' ') {
                if (ls.charAt(i) != '-') {
                    leftPointer = ls.charAt(i) - '1';
                    mergedSequences[lenBefore + i] = l.getSequence()[leftPointer];
                    source.add(Pair.of(leftPointer, -1));
                } else {
                    rightPointer = rs.charAt(i) - '1';
                    mergedSequences[lenBefore + i] = r.getSequence()[rightPointer];
                    source.add(Pair.of(-1, rightPointer));
                }
            } else if (c == '.') {
                leftPointer = ls.charAt(i) - '1';
                rightPointer = rs.charAt(i) - '1';
                mergedSequences[lenBefore + i] = weightedAverageSequences(l, r, leftPointer, rightPointer);
                source.add(Pair.of(leftPointer, rightPointer));
            }
        }

        //after aligned
        int lastIndex = (longerSeq.charAt(longerSeq.length() - 1) - '0');

        for (int i = lastIndex; i < longerSeqContent.length; i++) {
            mergedSequences[i] = longerSeqContent[i];
            if (leftLonger) {
                source.add(Pair.of(i, -1));
            } else {
                source.add(Pair.of(-1, i));
            }
        }
//        System.out.println("Merged output");
//        System.out.println(source);
//        System.out.println(mergedSequences.length);
    }

    private double[] weightedAverageSequences(ScriptClusterNode l, ScriptClusterNode r, int leftIndex, int rightIndex) {
        int cl = l.getObservationCount();
        int cr = r.getObservationCount();

        double weightLeft = ((double) cl) / (cl + cr);
        double weightRight = ((double) cr) / (cl + cr);

        double[] seq = l.getSequence()[leftIndex];
        double[] seqR =r.getSequence()[rightIndex];

        for (int i = 0; i < seq.length; i++) {
            seq[i] = seq[i] * weightLeft + seqR[i] * weightRight;
        }

        return seq;
    }

    private String double2Rep(double[] seq) {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for (double s : seq) {
            sb.append(sep);
            sb.append(s);
            sep = " ";
        }
        return sb.toString();
    }

    private double[] rep2Doulbe(String r) {
        String[] rs = r.split(" ");

        double[] rep = new double[rs.length];

        for (int i = 0; i < rs.length; i++) {
            rep[i] = Double.parseDouble(rs[i]);
        }
        return rep;
    }

    @Override
    public ScriptClusterNode getLeft() {
        return leftNode;
    }

    @Override
    public ScriptClusterNode getRight() {
        return rightNode;
    }

    @Override
    public int getObservationCount() {
        return numOberservations;
    }

    @Override
    public boolean isLeave() {
        return false;
    }

    @Override
    public double[][] getSequence() {
        return mergedSequences;
    }

    public double getAlignScore() {
        return alignScore;
    }

    public List<Pair<Integer, Integer>> getSource() {
        return source;
    }
}
