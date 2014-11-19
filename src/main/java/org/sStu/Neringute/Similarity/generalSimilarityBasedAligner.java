package org.sStu.Neringute.Similarity;

import org.sStu.Neringute.ngAligner;
import org.sStu.StringComparator;

/**
 * <p>Title: Sequences study</p>
 * <p>Description: Package for working with sequences</p>
 *
 * @author Audrius Meskauskas
 * @version 1.0
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 */

public abstract class generalSimilarityBasedAligner
        extends ngAligner
        implements org.sStu.Aligner {
    /** All parameters and important variables, including parameters
     *  and variable for the derived classes,
     are collected here. */

    /**
     * Score for match.
     */
    public int MATCH_WEIGHT = 2;
    /**
     * Score for mismatch.
     */
    public int MISMATCH_WEIGHT = -1;
    /**
     * Score for gaps (if supported).
     */
    public int GAP_WEIGHT = -2;
    /**
     * If true, the unaligned ends are included.
     */
    public boolean SHOW_ENDS = false;

    /**
     * Maximal number of the differences (if supported).
     */
    public int K = 3;

    /**
     * No-end-gap mode (if supported).
     */
    public boolean NO_END_GAP = true;
    /**
     * D[i][j]=score for aligment of S1[1..i] against S2[1..j]
     */
    public double[][] V;
    /**
     * The computed score of the finished aligment (D[m][n].
     */
    public double Score;

    /**
     * If true, the matrix D is discarded during the computation,
     * saving space. Only the last line is stored.
     */
    public boolean DISCARD_V = true;

    /**
     * The first sequence. The numeration is from
     * one, the first character is not used.
     */
    public double[][] S1;
    /**
     * The second sequence. The numeration is from
     * one, the first character is not used.
     */
    public double[][] S2;

    /**
     * Length of S1
     */
    public int m;
    /**
     * Length of S2
     */
    public int n;

    /** (end of the shared parameter area. */

    /**
     * Create the distance computator for computing the edit distance
     * between these two char sequences.
     */

    public generalSimilarityBasedAligner() {
    }

    /**
     * Set the sequence with the given number to the given value.
     * For the double alignment, the valid values are 0 and 1.
     * If multiple sequences are compared against one, this one
     * (main) sequence should have the number 0. This ensures
     * optimal preprocessing.
     * For multiple alingments, a larger values may be allowed.
     * Some implementations may call preprocessing routines after
     * the main sequence is set.
     */
    public void setSequence(double[][] seq, int number) {
        switch (number) {
            case 0:
                S1 = appendFirstPosition(seq);
                m = seq.length;
                break;
            case 1:
                S2 = appendFirstPosition(seq);
                n = seq.length;
                break;
            default:
                throw new ArrayIndexOutOfBoundsException("0 and 1 are allowed, but " +
                        number +
                        " passed as the sequence number.");
        }
    }

    /**
     * The comparator, if set, is responsible for the sequence comparison at the given position.
     */
    public StringComparator comparator = null;

    /**
     * Math/mismatch score at these positions.
     */
    public double s(int i, int j) {
        // if comparator is set, rely on this comparator
        if (comparator != null) {
            return comparator.compare(S1, i, S2, j);
        }

        // else use default comparison:
        if (S1[i].equals(S2[j])) {
            return MATCH_WEIGHT;
        } else {
            return MISMATCH_WEIGHT;
        }
    }

    /**
     * Match at S1[i] against space.
     * The default method returns GAP_WEIGHT.
     */
    public int S1_(int i) {
        return GAP_WEIGHT;
    }

    /**
     * Match at S1[i] against space. GAP_WEIGHT.
     */
    public int S2_(int j) {
        return GAP_WEIGHT;
    }

    /**
     * Maximal value of these 2 (delegates to Math.max).
     */
    public static final double max(double a, double b) {
        return a > b ? a : b;
        // return Math.max(a,b); // slower
    }

    /**
     * Maximal value of these three.
     */
    public static final double max(double a, double b, double c) {

        double max = a;
        if (b > max) {
            max = b;
        }
        if (c > max) {
            max = c;
        }
        return max;
        // return Math.max(a, Math.max(b,c)); // slower
    }

    /**
     * Maximal value of these four.
     */
    public static final double max(double a, double b, double c, double d) {

        double max = a;
        if (b > max) {
            max = b;
        }
        if (c > max) {
            max = c;
        }
        if (d > max) {
            max = d;
        }
        return max;
        //return Math.max(Math.max(a,b), Math.max(c,d)); // slower
    }

    /**
     * Output message
     */
    public void p(String msg) {
        System.out.println(msg);
    }

    public abstract void align();

}