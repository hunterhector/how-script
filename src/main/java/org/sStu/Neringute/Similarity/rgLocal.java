package org.sStu.Neringute.Similarity;

/**
 * Gapped local alignment
 * <p>Company: Audrius Meskauskas</p>
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 * @author Audrius Meskauskas
 * @version 1.0
 */

public class rgLocal
    extends rLocal
    implements org.sStu.AlignmentRangeAligner {

  /** Weight of the gap. */
  public int Wg = GAP_WEIGHT;
  public int Ws = Wg;

  /** More matrices must be defined. */
  double[][] E = null;
    double[][] F = null;

  public void align() {
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

    double f, e, g, max;
    byte np;

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

      for (j = 1; j <= n; j++) { // loop over rows

        e = E[i][j] = E(i, j);
        f = F[i][j] = F(i, j);
        g = G(i, j);

        max = max(f, e, g, 0);
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

        // ignore alignments that end by gap or mismatch
        if ( (np & UP_LEFT) != 0 && S1[i] == S2[j]) {
          alignments.add(new Alignment(i, j));

        }
      } // end loop j

      E[i - 1] = F[i - 1] = null;
      // release the unused column:
      if (DISCARD_V) {
        if (i > 0) {
          V[i - 1] = null;
        }
      }
    }
    ptr[0][0] = STOP;

    Score = alignments.best().Score;

    E = F = null;
    if (DISCARD_V) {
      V = null;
    }
  }

  // Boundary values:

  // This function is not called, as array is initialized to 0 anyway
  int Vi0(int i) {
    return 0;
    // Wg+i*Ws if end gaps are not free
  }

  // This function is not called, as array is initialized to 0 anyway
  int V0j(int j) {
    return 0;
    // Wg+j*Ws if end gaps are not free
  }

  int Ei0(int i) {
    return Wg + i * Ws;
  }

  int F0j(int j) {
    return Wg + j * Ws;
  }

  double V(int i, int j) {
    return max(E(i, j), F(i, j), G(i, j));
  }

  double G(int i, int j) {
    if (S1[i] == S2[j]) {
      return V[i - 1][j - 1] + MATCH_WEIGHT; // V is supposed to be precomputed
    }
    else {
      return V[i - 1][j - 1] + MISMATCH_WEIGHT;
    }
  }

  double E(int i, int j) {
    return max(E[i][j - 1], V[i][j - 1] + Wg) + Ws;
  }

  double F(int i, int j) {
    return max(F[i - 1][j], V[i - 1][j] + Wg) + Ws;
  }

  public org.sStu.Alignment getBestAlignment() {
    return new AlignmentImp( ( (Alignment) alignments.best()).
                            getAlignmentStrings(SHOW_ENDS), Score);
  }

}