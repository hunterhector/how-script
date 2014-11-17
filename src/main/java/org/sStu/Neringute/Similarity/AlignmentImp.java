package org.sStu.Neringute.Similarity;

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
        implements Alignment {

    CharSequence[] sequences;
    CharSequence middle;
    double score;

    public AlignmentImp(CharSequence[] al, double _score) {
        sequences = new CharSequence[2];
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

}