package org.sStu;

/**
 * Represents the alignment.
 * @author Audrius Meskauskas
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 * @version 1.0
 */

public interface Alignment {

    /** Get the given sequence, previously set with setSequence(..).
     * @return The sequence, containing additional '-'
     * symbols at insertions. All returned sequences
     * have the same length.
     * @throws ArrayIndexOutOfBoundsException if the number
     * is negative of refers above the valid range.
     * @param number The sequence number, counting from 0 (for two sequence alignment, only values 0 and 1 are valid.
     */
  CharSequence getSequence(int number) throws ArrayIndexOutOfBoundsException;

  /** Get the "middle line" of the alignment, explaining the comparison process.
   * @return the "middle line" of the alignment,
   * explaining the comparison process. For two
   * sequence alignment, it contains
   * the signal characters reporting comparison state ('|' from match,
   * '.' for mismatch, ' ' for space).
   */
  CharSequence getMiddleString();

  /** Return the score of the alignment. Must be called after the align() was called.
   * @return The score of this alignment.
   */
  double getAlignmentScore();

  /** Returns a string representation of the alignment.
   * @return String representation, containing first sequence, line feed, middle (explaining)
   * sequence, line feed again and then the second sequence.
   */
  public String toString();

}