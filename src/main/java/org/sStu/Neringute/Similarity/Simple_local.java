package org.sStu.Neringute.Similarity;

/**
 * Simple local aligner, returns the best score only.
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 * @author Audrius Meskauskas
 * @version 1.0
 */

public class Simple_local
    extends Simple_global
    implements org.sStu.Aligner {

  public void align() {
      double best = -Double.MIN_VALUE; // the score of the optimal alignment.

    int i, j;
    V = new double[m + 1][]; // char numeration from 1

    // fill zero row
    V[0] = new double[n + 1];

      double a, b, c, max;
    byte np;

    for (i = 1; i <= m; i++) { // loop over columns
      V[i] = new double[n + 1]; // char numeration from 1

      for (j = 1; j <= n; j++) { // loop over rows

        a = V[i - 1][j] + S1_(i);
        b = V[i][j - 1] + S2_(j);
        c = V[i - 1][j - 1] + s(i, j);

        max = max(0, a, b, c);
        V[i][j] = max;

        if (V[i][j] > best) { // if better than the best, take this as a best.
          best = max;
        }

      } // end loop j

      // release the unused column:
      if (i > 0) {
        V[i - 1] = null;
      }
    }
    Score = best;

    if (DISCARD_V) {
      V = null;
    }
  }

}
