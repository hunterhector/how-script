package org.sStu.Neringute.Similarity;

/**
 * Global alignment when the expected number of the
 * differences is at most k. Returns the score of the best alignment only.
 *
 * @author Audrius Meskauskas
 * @version 1.0
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 */

public class Simple_kGlobal
        extends Simple_global
        implements org.sStu.ScoreOnlyAligner {


    public void align() {

        int i, j;
        V = new double[m + 1][]; // char numeration from 1

        // fill zero row
        V[0] = new double[n + 1];

        double a, b, c, max;
        byte np;
        int n_K, _K;

        for (i = 1; i <= m; i++) { // loop over columns
            V[i] = new double[n + 1]; // char numeration from 1

            if (!NO_END_GAP) {
                V[i][0] = Vi0(i); // zero row

                // limited boundaries where to compute:
            }
            _K = i - K;
            if (_K < 1) {
                _K = 1;
            }
            n_K = i + K;
            if (n_K > n) {
                n_K = n;

            }
            for (j = _K; j <= n_K; j++) { // loop over rows

                a = V[i - 1][j] + S1_(i);
                b = V[i][j - 1] + S2_(j);
                c = V[i - 1][j - 1] + s(i, j);

                max = max(a, b, c);

                V[i][j] = max;

            }
            if (DISCARD_V) {
                if (i > 0) {
                    V[i - 1] = null;

                }
            }
        } // end of the loop
        Score = V[m][n];
        if (DISCARD_V) {
            V = null;
        }
    }

}
