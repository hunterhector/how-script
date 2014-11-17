package org.sStu;

/**
 * Defines an aligner that returns the score
 * of the best alignment only. This allows to use faster algoritms,
 * increasing the performance up to four times. If the score
 * is above the level of interest, the alignment can be
 * re-computed using other aligners.
 *
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @author Audrius Meskauskas
 * @see http://www.gnu.org/licenses/gpl.txt
 * @version 1.0
 */

public interface ScoreOnlyAligner
    extends Aligner {

        /** Get the score of this alignment
         * @return The score of the alingment.
         */
  double getAlignmentScore();

}