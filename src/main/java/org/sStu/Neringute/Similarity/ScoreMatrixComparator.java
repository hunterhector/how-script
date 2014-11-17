package org.sStu.Neringute.Similarity;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.sStu.StringComparator;


/**
 * Char comparator is used with score matrixes.
 * It returns matrix[a][b].
 *
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 */

public class ScoreMatrixComparator extends StringComparator {
    /**
     * The score matrix.
     */
//  public char[][] matrix;

    public Table<String, String, Double> matrix = HashBasedTable.create();

    public ScoreMatrixComparator(Table<String, String, Double> scoreMatrix) {
        matrix = scoreMatrix;
    }

    /**
     * @returns the score from matrix (matrix[a][b]).
     * Compares characters returning.
     * If one or both the chars are outside the score matrix size
     * returns DefaultMatchValue if they are equal, DefaultMismatchValue otherwise.
     */
    public double compare(String a, String b) throws ArrayIndexOutOfBoundsException {
        try {
            return matrix.get(a, b);
        } catch (ArrayIndexOutOfBoundsException ex) {
            if (a == b) return DefaultMatchValue;
            else return DefaultMismatchValue;
        }
    }

    public int DefaultMatchValue = 2;
    public int DefaultMismatchValue = -1;

}