package org.sStu.Neringute.Similarity;

import java.io.PrintStream;

/**
 * Computes a simple edit distance (the score of gaps is
 * equal to the score of mismatches = 1). Also,
 * the aligment itself cannot be retrieved.
 *
 * @author Audrius Meskauskas
 * @version 1.0
 * @copyright Audrius Meskauskas, GPL license.
 * @see http://www.gnu.org/licenses/gpl.txt
 */

public class Simple_global
        extends generalSimilarityBasedAligner
        implements org.sStu.ScoreOnlyAligner {

    /**
     * Initial value V(0,j).
     *
     * @todo: if all values against the space are
     * equally aligned, this could be replaced just
     * by multiplication.
     */
    int V0j(int j) {
        if (NO_END_GAP) {
            return 0;
        }
        int S = 0;
        for (int k = 1; k <= j; k++) {
            S += S2_(k);
        }
        return S;
    }

    /**
     * Initial value V(i,0)
     */
    int Vi0(int i) {
        if (NO_END_GAP) {
            return 0;
        }
        int S = 0;
        for (int k = 1; k <= i; k++) {
            S += S1_(k);
        }
        return S;
    }

    /**
     * Compute the similarity matrix bottom up.
     */
    public void align() {

        double best = Integer.MIN_VALUE; // the score of the best alignment
        // the first available value will be immediately assinged.

        Score = Integer.MIN_VALUE;
        int i, j;
        V = new double[m + 1][]; // char numeration from 1

        // fill zero row
        V[0] = new double[n + 1];
        if (!NO_END_GAP) {
            for (j = 0; j <= n; j++) {
                V[0][j] = V0j(j);
            }
        }

        for (i = 1; i <= m; i++) {
            V[i] = new double[n + 1]; // char numeration from 1
            if (!NO_END_GAP) {
                V[i][0] = Vi0(i); // zero row
            }
            for (j = 1; j <= n; j++) {
                V[i][j] =
                        max(V[i - 1][j - 1] + s(i, j),
                                V[i - 1][j] + S1_(i),
                                V[i][j - 1] + S2_(j)
                        );

                // in the case with no end gap penalty the optimal alignment
                // can be found anywhere in the last column or the last row.
                if (NO_END_GAP) {
                    if (j == n || i == m) { // last row or last column
                        if (V[i][j] > best) {
                            best = V[i][j];
                        }
                    }
                }

            }
            // release the unused column:
            if (i > 0) {
                V[i - 1] = null;
            }
        }
        if (NO_END_GAP) {
            Score = best;
        } else {
            Score = V[m][n];

        }
    }

    /**
     * Find the maximal value in the array.
     */
    int max(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public void print_V(PrintStream out) {
        out.println("\nBest score " + Score);
        // Inactivate as the table is off:
        if (DISCARD_V) {
            out.println(
                    "The V matrix was discarded, set DISCARD_D to true if you need it.");
            return;
        }

        if (V == null) {
            align();
        }
        out.print("   ");
        for (int j = 0; j <= n; j++) {
            out.print("   " + S2[j]);
        }
        for (int i = 0; i < V.length; i++) {
            out.println();
            out.print(S1[i] + "   ");
            for (int j = 0; j < V[i].length; j++) {
                out.print(format(V[i][j]));
            }
        }
    }

    ;

    String format(double v) {
        StringBuffer s = new StringBuffer(Double.toString(v));
        while (s.length() < 4) {
            s.insert(0, ' ');
        }
        return s.toString();
    }

    public double getAlignmentScore() {
        return Score;
    }

}
