package org.sStu.Neringute.Similarity;

import org.sStu.AlignmentRangeAligner;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * Aligner able to compute and output a range of
 * suboptimal global alignments.
 *
 * @author Audrius Meskauskas
 * @version 1.0
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 */

public class rGlobal
        extends Global
        implements AlignmentRangeAligner {

    /**
     * Stores certain fixed number of the best alignments.
     */
    public LimitedSet alignments = new LimitedSet();

    /**
     * Represents a single alignment.
     */
    class Alignment
            implements Comparable {
        public Alignment(int _c, int _r) {
            r = _r;
            c = _c;
            Score = V[c][r];
        }

        /**
         * The score of this alignment.
         */
        double Score;
        /**
         * End of the alignment in the ptr table (column).
         */
        int c;
        /**
         * End of the alignment in the ptr table (row).
         */
        int r;
        /**
         * Length of the alignment.
         */
        private int length = -1;

        /**
         * Get three lines, representing this alignment.
         */
        public CharSequence[] getAlignmentStrings(boolean show_ends) {
            return getAlignment(c, r, show_ends);
        }

        /**
         * The values with the higher scores go first.
         */
        public int compareTo(Object with) {
            Alignment x = (Alignment) with;
            if (Score != x.Score) {
                return x.Score > Score ? 1 : -1;
            }

            if (length() != x.length) {
                return length() - x.length();
            }

            if (c != x.c) {
                return c - x.c;
            } else {
                return r - x.r;
            }

        }

        /**
         * The length of alignment.
         *
         * @todo: this implementation is ineffective.
         */
        public int length() {
            if (length >= 0) {
                return length;
            }

            int i = c;
            int j = r;
            int v;

            length = 0;

            while (j > 0 && i > 0) {
                v = ptr[i][j];
                if ((v & UP_LEFT) != 0) {
                    i--;
                    j--;
                } else if ((v & UP) != 0) {
                    i--;
                } else if ((v & LEFT) != 0) {
                    j--;
                } else {
                    break;
                }
                length++;
            }
            ;

            return length;
        }

        static final int MOVE = UP + LEFT + UP_LEFT;

        /**
         * True if this alignment just extends alignment a,
         * and alignment a has the higher score. In this case,
         * the alignment a can be discarded, this aligment must
         * be kept instead.
         */

        public boolean Extends(Alignment a) {
      /*
                int nr = r;
                int nc = c;
                int v = ptr[c][r];
                int A;
                if ((v & UP_LEFT)!=0)  { nc--; nr--; }
                if ((v & UP)!=0)       nr--;
                if ((v & LEFT  )!=0)   nc--;
                if (nc==a.c && nr==a.r) return true;
                return false;
       */

            int nr = r;
            int nc = c;
            int v = ptr[c][r];
            int A;

            int vc = 0;
            int vr = 0;

            int bts = 0;

            if (nc == a.c && nr == a.r) {
                return true;
            }
            Backtracing:
            while (nc > 0 && nr > 0) {
                if ((v & UP_LEFT) != 0) {
                    vc = vr = -1;
                    if (nc - 1 == a.c && nr - 1 == a.r) {
                        return true;
                    }
                }

                if ((v & UP) != 0) {
                    vr = -1;
                    vc = 0;
                    if (nc == a.c && nr - 1 == a.r) {
                        return true;
                    }
                }

                if ((v & LEFT) != 0) {
                    vr = -0;
                    vc = -1;
                    if (nc - 1 == a.c && nr == a.r) {
                        return true;
                    }
                }

                nr += vr;
                nc += vc;

                bts++;
                if ((v & (MOVE)) == 0) {
                    break Backtracing;
                }
            }
            return false;

        }
    }

    public void align() {
        alignments.clear();

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

//        System.out.println(m + " " + n);

        for (i = 1; i <= m; i++) { // loop over columns
            V[i] = new double[n + 1]; // char numeration from 1

            ptr[i] = new byte[n + 1];
            if (!NO_END_GAP) {
                V[i][0] = Vi0(i); // zero row

            }
            ptr[i][0] = LEFT;

            for (j = 1; j <= n; j++) { // loop over rows

                a = V[i - 1][j] + S1_(i);
                b = V[i][j - 1] + S2_(j);
                c = V[i - 1][j - 1] + s(i, j);

//                System.out.println(s(i, j));

                max = max(a, b, c);

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
                ptr[i][j] = np;
                V[i][j] = max;

                // in the case with no end gap penalty the optimal alignment
                // can be found anywhere in the last column or the last row.
                if (NO_END_GAP) {
                    if (j == n || i == m) { // last row or last column
                        alignments.add(new Alignment(i, j));
//                        System.out.println("adding");
//                        System.out.println(alignments.set.size());
                    }
                }
                ;
            }
            if (DISCARD_V) {
                if (i > 0) {
                    V[i - 1] = null;

                }
            }
        } // end of the loop
        ptr[0][0] = STOP;

        if (!NO_END_GAP) { // in the case with end gap penalty

            // still able to retrieve the optimal alignment only
            alignments.add(new Alignment(m, n));

        }
        if (NO_END_GAP) {
            Score = ((Alignment) alignments.set.first()).Score;
        } else {
            Score = V[m][n];
        }
        if (DISCARD_V) {
            V = null;
        }
    }

    /**
     * Leave only the best alignment from each several
     * that are parts of each other.
     */
    public void discardRedundantAlignments() {
        TreeSet accepted = new TreeSet();
        Iterator iter = alignments.set.iterator();
        all:
        while (iter.hasNext()) {
            Alignment candidate = (Alignment) iter.next();
            Iterator fi = accepted.iterator();
            while (fi.hasNext()) {
                Alignment present = (Alignment) fi.next();
                if (present != candidate) {
                    if (present.Extends(candidate)) {
                        candidate = null; // reject
                        break;
                    }
                }
                ;
            }
            if (candidate != null) {
                accepted.add(candidate); // if it was not rejected
            }
        }
        System.out.println("From " + alignments.set.size() + " left " +
                accepted.size());
        alignments.set = accepted;
    }

    ;

    public void printAllAlignments() {
        //discardRedundantAlignments();
        Iterator iter = alignments.set.iterator();
        while (iter.hasNext()) {
            Alignment item = (Alignment) iter.next();
            System.out.println();
            System.out.println("********");
            CharSequence[] A =
                    item.getAlignmentStrings(true);
            System.out.println("Score: " + item.Score + " end " + item.c + ":" +
                    item.r + " at " +
                    S1[item.c] + "-" + S2[item.r]);
            if (!DISCARD_V) {
                System.out.println("Score at that cell is " + V[item.c][item.r]);
            }
            System.out.println(A[0]);
            System.out.println(A[1]);
            System.out.println(A[2]);
        }
    }

    public org.sStu.Alignment[] getAlignments() {
        org.sStu.Alignment[] r = new org.sStu.Alignment[alignments.set.size()];
        int i = 0;
        Iterator iter = alignments.set.iterator();
        while (iter.hasNext()) {
            Alignment item = (Alignment) iter.next();
            CharSequence[] A = item.getAlignmentStrings(SHOW_ENDS);
            r[i++] = new org.sStu.Neringute.Similarity.AlignmentImp(A, item.Score);
        }
        return r;
    }

}