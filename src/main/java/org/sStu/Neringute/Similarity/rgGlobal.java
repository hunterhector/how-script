package org.sStu.Neringute.Similarity;

/**
 * <p>Title: Sequences study</p>
 * <p>Description: Package for working with sequences</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Audrius Meskauskas</p>
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 * @author Audrius Meskauskas
 * @version 1.0
 */

public class rgGlobal
    extends rgLocal
    implements org.sStu.AlignmentRangeAligner {

  int Vi0(int i) {
    return (Wg + i * Ws);
  }

  int V0j(int j) {
    return (Wg + j * Ws);
  }

  public void align() { //
    Wg = GAP_WEIGHT;
    alignments.clear();

    int i, j;
    V = new double[m + 1][]; // char numeration from 1

    E = new double[m + 1][];
    F = new double[m + 1][];

    ptr = new byte[m + 1][];

    // fill zero row
    V[0] = new double[n + 1];
    // E[0] = new int[n+1]; // E[0] is not used
    F[0] = new double[n + 1];

    // intialization
    ptr[0] = new byte[n + 1];
    for (j = 0; j <= n; j++) {
      ptr[0][j] = UP;
      V[0][j] = V0j(j); // redundant
      F[0][j] = F0j(j);
      // E[0] is not used
    }

    for (i = 1; i <= m; i++) { // loop over columns
      V[i] = new double[n + 1]; // char numeration from 1
      E[i] = new double[n + 1]; // char numeration from 1
      F[i] = new double[n + 1]; // char numeration from 1

      // initialization
      V[i][0] = Vi0(i);
      E[i][0] = Ei0(i);
      // F[i][0] is not used

      ptr[i] = new byte[n + 1];
      // no end gap penalty
      ptr[i][0] = LEFT;

      double f, e, g, max;
      byte np;

      for (j = 1; j <= n; j++) { // loop over rows

        e = E[i][j] = E(i, j);
        f = F[i][j] = F(i, j);
        g = G(i, j);

        max = max(f, e, g);
        np = 0;

        if (e == max) {
          np |= LEFT;
        }
        if (f == max) {
          np |= UP;
        }
        if (g == max) {
          np |= UP_LEFT;
        }
        if (max == 0) {
          np |= STOP;

        }
        ptr[i][j] = np;
        V[i][j] = max;

        if (i == n || j == m) {
          alignments.add(new Alignment(i, j));

        }
      } // end loop j

      // release the unused column:
      if (DISCARD_V) {
        if (i > 0) {
          V[i - 1] = E[i - 1] = F[i - 1] = null;
        }
      }
    }
    ptr[0][0] = STOP;

    Score = alignments.best().Score;

    if (DISCARD_V) {
      V = E = F = null;
    }
  }

}