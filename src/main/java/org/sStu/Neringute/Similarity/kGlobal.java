package org.sStu.Neringute.Similarity;

/**
 * Global alignment when the expected number of the
 * differences is at most k. Returns single alignment only.
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 * @author Audrius Meskauskas
 * @version 1.0
 */

public class kGlobal
    extends rGlobal
    implements org.sStu.SingleAlingmentAligner {


  public void align() {
    int i, j;
    V = new double[m + 1][]; // char numeration from 1
    ptr = new byte[m + 1][];

    // fill zero row
    V[0] = new double[n + 1];
    ptr[0] = new byte[n + 1];
    for (j = 0; j <= n; j++) {
      if (!NO_END_GAP) {
        V[0][j] = V0j(j);
      }
      ptr[0][j] = UP;
    }

    double a;
      double b;
      double c;
      double max;
      byte np;
    int n_K, _K;

    for (i = 1; i <= m; i++) { // loop over columns
      V[i] = new double[n + 1]; // char numeration from 1

      ptr[i] = new byte[n + 1];
      if (!NO_END_GAP) {
        V[i][0] = Vi0(i); // zero row

      }
      ptr[i][0] = LEFT;

      // limited boundaries where to compute:
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
        np = 0;

        if (a == max && j > _K) {
          np |= LEFT;
        }
        if (b == max && j > _K) {
          np |= UP;
          // (do not leave the computed area)

        }
        if (c == max) {
          np |= UP_LEFT;

        }
        ptr[i][j] = np;
        V[i][j] = max;

      }
      if (DISCARD_V) {
        if (i > 0) {
          V[i - 1] = null;

        }
      }
    } // end of the loop
    ptr[0][0] = STOP;
    Score = V[m][n];
    if (DISCARD_V) {
      V = null;
    }
  }

}
