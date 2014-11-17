package org.sStu.Neringute.Similarity;

/**
 * <p>Finds the optimal local alignment</p>
 * <p>Description: Package for working with sequences</p>
 *
 * @author Audrius Meskauskas
 * @version 1.0
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 */

public class Local
        extends Global
        implements org.sStu.SingleAlingmentAligner {

    public void align() {
        ma = na = 0; // where the optimal aligmnent starts
        double best = -Integer.MIN_VALUE; // the score of the optimal alignment.

        int i, j;
        V = new double[m + 1][]; // char numeration from 1
        ptr = new byte[m + 1][];

        // fill zero row
        V[0] = new double[n + 1];
        ptr[0] = new byte[n + 1];
        for (j = 0; j <= n; j++) {
            ptr[0][j] = UP;
        }

        double a, b, c, max;
        byte np;

        for (i = 1; i <= m; i++) { // loop over columns
            V[i] = new double[n + 1]; // char numeration from 1

            ptr[i] = new byte[n + 1];
            // no end gap penalty
            ptr[i][0] = LEFT;

            for (j = 1; j <= n; j++) { // loop over rows

                a = V[i - 1][j] + S1_(i);
                b = V[i][j - 1] + S2_(j);
                c = V[i - 1][j - 1] + s(i, j);

                max = max(0, a, b, c);
                np = 0;

                if (a == max) {
                    np |= LEFT;
                }
                if (b == max) {
                    np |= UP;
                }
                if (c == max) {
                    np |= UP_LEFT;
                }
                if (max == 0) {
                    np |= STOP;

                }
                ptr[i][j] = np;
                V[i][j] = max;

                if ((np & UP_LEFT) != 0 && S1[i] == S2[j]) {
                    if (V[i][j] > best) { // if better than the best, take this as a best.
                        ma = i;
                        na = j;
                        best = max;
                    }
                }
            } // end loop j

            // release the unused column:
            if (i > 0) {
                V[i - 1] = null;
            }
        }
        ptr[0][0] = STOP;
        Score = best;

        if (DISCARD_V) {
            V = null;
        }
    }

}