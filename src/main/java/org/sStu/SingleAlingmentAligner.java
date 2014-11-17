package org.sStu;

/**
 * Defines an aligner that returns one (best) alignment and its score.
 * These aligners are used when only the best alignment is the point
 * of interest. They have the intermediate performace between ScoreOnly-
 * and AlinmentRand- aligners.
 *
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 * @author Audrius Meskauskas
 * @version 1.0
 */

public interface SingleAlingmentAligner
    extends ScoreOnlyAligner {

        /** Get the best alignment.
         * @return The best alignment
         */
  Alignment getBestAlignment();

}