package org.sStu;

/**
 * Class for comparing two characters in the sequence.
 *
 * @author Audrius Meskauskas
 * @version 1.0
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 */

public abstract class StringComparator {

    /**
     * Return the score of comparison of the two characters. Implementing classed
     * must override this method.
     *
     * @param a,b - the characters to compare
     * @return The score of comparison of the two characters a and b
     */
    public abstract double compare(double[] a, double[] b);

    /**
     * Compares characters at A[i] and B[j]. Sophisticated
     * comparison algorithms may use comparison strategies
     * that take the value of the neighbouring characters into account.
     * The default method calls compare(char a, char b),
     *
     * @param A[i], B[j] - the characters to compare. The numeration in the
     *              arrays A and B starts from 1, the zero element is not used.
     * @return The score of comparison of the two characters a and b
     */
    public double compare(double[][] A, int i, double[][] B, int j) {
        return compare(A[i], B[j]);
    }

}