package org.sStu;

/**
 * Defines a most comprehensive (but slowest) alingner able to return the
 * range of suboptimal alignments when required.
 *
 * @author Audrius Meskauskas
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 * @version 1.0
 */

public interface AlignmentRangeAligner
    extends ScoreOnlyAligner {

        /** Get the alignments. Must be called after align() was called.
         * @return The sorted array of alignments (higher scores first).
         */
  Alignment[] getAlignments();

}