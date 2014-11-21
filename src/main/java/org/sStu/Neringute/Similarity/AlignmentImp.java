package org.sStu.Neringute.Similarity;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.sStu.Alignment;

/**
 * Alignment implementation.
 *
 * @author Audrius Meskauskas
 * @version 1.0
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 */

public class AlignmentImp
        implements Alignment, Comparable {

    String[] sequences;
    String middle;
    double score;

    public AlignmentImp(String[] al, double _score) {
        sequences = new String[2];
        sequences[0] = al[0];
        sequences[1] = al[2];
        middle = al[1];
        score = _score;
    }

    public CharSequence getSequence(int number) throws
            ArrayIndexOutOfBoundsException {
        return sequences[number];
    }

    public CharSequence getMiddleString() {
        return middle;
    }

    public double getAlignmentScore() {
        return score;
    }

    public String toString() {
        return "\n" + sequences[0] + "\n" + middle + "\n" + sequences[1];
    }

    @Override
    public int compareTo(Object o) {
        AlignmentImp theOther = (AlignmentImp) o;
        return new CompareToBuilder()
                .append(this.sequences, theOther.sequences)
                .toComparison();
    }
}