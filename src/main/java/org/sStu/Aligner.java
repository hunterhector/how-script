package org.sStu;

/**
 * Implements abstract aligner. It has no methods for retrieving
 * the results, these are provided by subinterfaces.
 * @author Audrius Meskauskas
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 * @version 1.0
 */

public interface Aligner {

  /** Compute the alignment now. This method is
   * placed separately to perform the most time
   * intensive step at the desired time (can be
   * important during parallel computing). It is NOT required
   * to call this method. If not previously called,
   * it must be called automatically when the properties
   * of the alignment are requested.
   */
  void align();

  /** Set the sequence with the given number to the given value.
   * For the double alignment, the valid values are 0 and 1.
   * If multiple sequences are compared against one, this one
   * (main) sequence should have the number 0. This ensures
   * optimal preprocessing.
   * For multiple alingments, a larger values may be allowed.
   * Some implementations may call preprocessing routines after
   * the main sequence is set.
   * @param seq Sequence to set. By default, any character is allowed in the sequences. The
   * alignments are case sensitive by default
   * @param number The number of the sequence in alignment. For two sequence alignments, this
   * parameter can only take values 0 and 1. Optimizations and pre-processing are
   * implemented supposing that the sequence 0 is longer and more frequently used.
   */
  public void setSequence(String[] seq, int number);

}