package org.sStu.Neringute.Similarity;

import java.io.PrintStream;
import java.util.Arrays;

/**
 * This class performs global alignments (with and without
 * the end gap penalty) and also provides basis for the
 * local alignments.
 * Description: Package for working with sequences
 *
 * @author Audrius Meskauskas
 * @version 1.0
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 */

public class Global
        extends Simple_global
        implements org.sStu.SingleAlingmentAligner {

    int d;
    /**
     * The table of pointers.
     */
    public byte[][] ptr;

    /**
     * Pointer in the cell points upward.
     */
    public static final byte LEFT = 1;
    /**
     * Pointer in the cell points left.
     */
    public static final byte UP = 2;
    /**
     * Pointer in the cell points up-left
     */
    public static final byte UP_LEFT = 4;
    /**
     * End of trace.
     */
    public static final byte STOP = 8;

    /**
     * Column where the end of the optimal alignment is located.
     */
    public int ma = -1;
    /**
     * Row where the end of the optimal alignment is located.
     */
    public int na = -1;

    public void align() {
        int i, j;
        V = new double[m + 1][]; // char numeration from 1
        ptr = new byte[m + 1][];

        // The position from where the optimal alignment starts.
        // Is only changed if the no_end_gap is true.
        ma = m;
        na = n;

        double best = Integer.MIN_VALUE; // the score of the best alignment
        // the first available value will be immediately assinged.

        // fill zero row
        V[0] = new double[n + 1];
        ptr[0] = new byte[n + 1];
        for (j = 0; j <= n; j++) {
            if (!NO_END_GAP) {
                V[0][j] = V0j(j);
            }
            ptr[0][j] = UP;
        }

        double a, b, c, max;
        byte np;

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
                        if (V[i][j] > best) {
                            ma = i;
                            na = j;
                            best = V[i][j];
                        }
                    }
                }
            } // end loop j
            // release the unused column:
            if (DISCARD_V) {
                if (i > 0) {
                    V[i - 1] = null;
                }
            }
        } // end loop i
        ptr[0][0] = STOP;

        if (NO_END_GAP) {
            Score = best;
        } else {
            Score = V[ma][na];

        }
        if (DISCARD_V) {
            V = null;
        }
    }

    public void print_ptr(PrintStream out) {
        out.println("\nVECTORS\n");
        if (V == null) {
            align();
        }
        out.print(" ");

        for (int j = 0; j <= n; j++) {
            out.print("   " + S2[j]);
        }

        for (int i = 0; i < ptr.length; i++) {
            out.println();
            out.print(S1[i] + "   ");
            for (int j = 0; j < ptr[i].length; j++) {
                out.print(ptr_format(ptr[i][j]));
            }
        }
    }

    ;

    String ptr_format(int ptr) {
        StringBuffer b = new StringBuffer(4);
        if ((ptr & UP_LEFT) != 0) {
            b.append('\\');
        }
        if ((ptr & UP) != 0) {
            b.append('<');
        }
        if ((ptr & LEFT) != 0) {
            b.append('^');
        }
        if (ptr == STOP) {
            b.append('#');
        }
        while (b.length() < 4) {
            b.append(' ');
        }
        return b.toString();
    }

    public void print_V(PrintStream out) {
        super.print_V(out);
        print_ptr(out);

    }

    ;

    /**
     * Prints the alignment.
     */
    public void print_alignment(PrintStream out, boolean show_ends) {
        out.println();
        CharSequence A[] = getAlignment(ma, na, show_ends);
        out.println(A[0]);
        out.println(A[1]);
        out.println(A[2]);
    }

    /**
     * Get alignment starting from the cell a_i, a_j
     */
    public CharSequence[] getAlignment(int a_i, int a_j, boolean show_ends) {
        byte[] w = new byte[m + n + 1];
        int N = 0;

        int c = a_i;
        int r = a_j;

        // find from where to go:

        byte v = ptr[c][r];
        byte A;

        int i, j;

        // add the right side of aligment, where one of the sequences is
        // against spaces.
        if (show_ends) {
            if (c == m && r < n) { // in the last column
                for (int p = 0; p < (n - r); p++) {
                    w[N++] = UP;
                }
            } else if (c < m && r == n) { // in the last row
                for (int p = 0; p < (m - c); p++) {
                    w[N++] = LEFT;
                }
            }
        }

        while (show_ends || (r > 0 && c > 0)) {
            v = ptr[c][r];

            if ((v & UP_LEFT) != 0) {
                A = UP_LEFT;
            } else if ((v & UP) != 0) {
                A = UP;
            } else if ((v & LEFT) != 0) {
                A = LEFT;
            } else {
                A = STOP;

            }
            if (A == UP_LEFT) {
                c--;
                r--;
            } else if (A == LEFT) {
                c--;
            } else if (A == UP) {
                r--;
            } else {
                break;
            }
            w[N++] = A;
        }
        ;

        //w[N++]=UP_LEFT;
        // flip horizontal:
        int[] K = new int[N];
        for (i = 0; i < N; i++) {
            K[N - i - 1] = w[i];
        }

        i = c + 1;
        j = r + 1;

        StringBuffer A1 = new StringBuffer(N);
        StringBuffer A2 = new StringBuffer(N);
        StringBuffer AL = new StringBuffer(N);

        for (int p = 0; p < N; p++) {
            Switch:

            switch (K[p]) {
                case LEFT: {
                    String str1 = S1[i++];
                    A1.append(str1 + " ");
                    A2.append(fill(str1.length(), '-') + " ");
                    AL.append(fill(str1.length(), ' ') + " ");
                    break;
                }
                case UP: {
                    String str2 = S2[j++];

                    A1.append(str2 + ' ');
                    A2.append(fill(str2.length(), '-') + " ");
                    AL.append(fill(str2.length(), ' ') + " ");
                    break;
                }
                case UP_LEFT: {
                    String str1 = S1[i++];
                    String str2 = S2[j++];

                    int len = str1.length() > str2.length() ? str1.length() : str2.length();
                    if (!str1.equals(str2)) {
                        AL.append(fill(len, '.') + " ");
                    } else {
                        AL.append(fill(len, '|') + " ");
                    }

                    A1.append(str1 + fill(len - str1.length(), ' ') + " ");
                    A2.append(str2 + fill(len - str2.length(), ' ') + " ");
                    break;
                }
            }
        }
        return new CharSequence[]{
                A1, AL, A2};
    }

    private String fill(int n, char c) {
        char[] charArray = new char[n];
        Arrays.fill(charArray, c);
        return new String(charArray);
    }

    public org.sStu.Alignment getBestAlignment() {
        if (ma < 0) ma = m;
        if (na < 0) na = n;
        return new AlignmentImp(getAlignment(ma, na, SHOW_ENDS), Score);
    }

}