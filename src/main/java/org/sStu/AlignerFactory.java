package org.sStu;

import com.google.common.collect.Table;
import org.sStu.Neringute.Similarity.*;

/**
 * AlignerFactory creates and returns various aligners with the given parameters.>
 *
 * @author Audrius Meskauskas
 * @version 1.0
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, GPL license.
 * @see http://www.gnu.org/licenses/gpl.txt
 */

public class AlignerFactory {

    /**
     * Constructs the AlignerFactory. The same instance of AlignerFactory can later
     * produce any desired number of aligners.
     */
    public AlignerFactory() {
    }

    /**
     * Request the global aligner. By default, the factory returs the local
     * aligner
     */
    protected boolean Global;

    /**
     * Request aligner that aligns both sequences over the whole length. By
     * defaultthe insertions and deletions at the beginning and end of
     * the sequences do not  contribute gap penalties. The parameter
     * only makes sense for global alignments.
     */
    protected boolean Gapped;

    /**
     * The gap weigth (used only in gapped aligners). The default value is
     * usually -2.
     */
    protected int Gap_weight = UNSET;

    /**
     * Weigth of mismatch. Used only if user comparator or score matrix are
     * not set.
     */
    protected int Mismatch_weigth = UNSET;

    /**
     * Set the score matrix. If the matrix is set, the Match amd Mismatch
     * weigths are ignored, and the values from the matrix are used. The
     * size of this array must be sufficient to cover the values of all
     * expected characters. User comparator, if set, can get this matrix
     * using protected variable Score_matrix
     */
    protected int[][] Score_matrix;

    /**
     * Match weight. Used only if the score matrix or user comparator are not
     * set. The default value is 2.
     */
    protected int Match_weight = UNSET;

    /**
     * Set the class responsible for the character comparison. In this way it
     * is possible completely override the comparison strategy.
     */
    protected StringComparator comparator;

    /**
     * Get type of requested alingment (global or local). Global aligners compare the
     * sequences over the whole length. Local aligners are looking for specific regions
     * of high similarity.
     *
     * @return the type of the requested alignment (true for global, false for local)
     */
    public boolean isGlobal() {
        return Global;
    }

    /**
     * Set the type of the requested alignment. The default is local.
     *
     * @param Global the type of the requested alignment (true for global, false for local.
     */
    public void setGlobal(boolean Global) {
        this.Global = Global;
    }

    /**
     * Check if the requested aligner supports the gap conception. Gap is any number of
     * spaces and has a single score.
     *
     * @return the type of the requested alignment (true for global, false for local)
     * @see setGap_weight
     */
    public boolean isGapped() {
        return Gapped;
    }

    /**
     * Request the gapped aligner, counting gaps and not just spaces. These aligners
     * are typically much slower.  Gap is any number of spaces and has a single score.
     *
     * @param MindGaps true for gapper aligner, false (default) for the simple aligner.
     */
    public void setGapped(boolean MindGaps) {
        this.Gapped = MindGaps;
    }

    /**
     * Normally the unaligned start and end of the global alignment are not
     * included into report. It is possible to force to include them by setting
     * this flag.
     */
    protected boolean Show_Ends = false;

    /**
     * Normally the unaligned ends of sequences are not included in report. It is
     * possible to force to include them by setting this flag. Unaligned ends may
     * contibute or not contribute to the alignment score, depending from the flag
     * setEnd_gap_penalty.
     *
     * @return True if the ends of the global alignment must be included into report.
     * @see setEnd_gap_penalty
     */
    public boolean isShow_Ends() {
        return Show_Ends;
    }

    /**
     * Normally the unaligned start and end of the global alignment are not
     * included into report. It is possible to force to include them by setting
     * this flag.
     *
     * @param show true if ends must be included
     */
    public void setShow_Ends(boolean show) {
        this.Show_Ends = show;
    }

    /**
     * Normally the unaligned start and end of the global alignment do not contribute
     * to the score. They contribute if this flag is set.
     */
    protected boolean End_gap_penalty = false;

    /**
     * Normally the unaligned start and end of the global alignment do not contribute
     * to the score. They contribute if this flag is set.
     *
     * @return true if the unaligned start and end of the global alignment contribute to the score.
     */
    public boolean isEnd_gap_penalty() {
        return End_gap_penalty;
    }

    /**
     * Normally the unaligned start and end of the global alignment do not contribute
     * to the score. They contribute if this flag is set.
     *
     * @param penalty true if the unaligned start and end of the global alignment contribute to the score.
     */
    public void setEnd_gap_penalty(boolean penalty) {
        this.End_gap_penalty = penalty;
    }

    /**
     * Return the gap weight. Gap weight is used in gapped aligners only.
     *
     * @return Gap weight.
     */
    public int getGap_weight() {
        return Gap_weight;
    }

    /**
     * Set gap weight for the gapped aligner. This has effect only if the
     * gapped aligner is requested.
     *
     * @param Gap_weight gap weight
     */
    public void setGap_weight(int Gap_weight) {
        this.Gap_weight = Gap_weight;
    }

    /**
     * Return the Mismatch_weigth.
     *
     * @return the mismatch weigth.
     */
    public int getMismatch_weigth() {
        return Mismatch_weigth;
    }

    /**
     * Set mismatch weigth to the new value. This value is ignored
     * if the score matrix or the whole character comparator are
     * replaced. The default value is -1.
     *
     * @param Mismatch_weigth Mismatch weigth
     */
    public void setMismatch_weigth(int Mismatch_weigth) {
        this.Mismatch_weigth = Mismatch_weigth;
    }

    //

    /**
     * Maximal number of differences. Limiting number of differences
     * increases the computation speed.
     *
     * @todo: At the given stage of development, this option is only
     * supported for global alignments.
     */
    protected int MaxDifferences = UNSET;

    /**
     * Return the maximal expected number of differences.
     *
     * @return Maximal expected number of differences in alignment. This is not supported in
     * the current version.
     */
    public int getMaxDifferences() {
        return MaxDifferences;
    }

    /**
     * Set mismatch weigth to the new value. This value is ignored
     * if the score matrix or the whole character comparator are
     * replaced. The default value is -1.
     *
     * @param MaxDifferences Maximal expected number of differences in alignment. This is not supported in
     *                       the current version.
     */
    public void setMaxDifferences(int MaxDifferences) {
        this.MaxDifferences = MaxDifferences;
    }

    //

    /**
     * Set score matrix, defining comparison values for any expected pair of characters.
     * For characters outside the score matrix, the default match and mismatch values are
     * used.
     *
     * @param matrix Two dimensional of weights, default match and mismatch values.;
     */
    public void setScore_matrix(Table<String, String, Double> matrix) {
        ScoreMatrixComparator smc = new ScoreMatrixComparator(matrix);
        setComparator(smc);
    }

    /**
     * Return the match weight.
     *
     * @return the match weight.
     */
    public int getMatch_weight() {
        return Match_weight;
    }

    /**
     * Set match weight to the new value. The default value is 1. It is used only score matrix
     * or user char comparator are set.
     *
     * @param Match_weight Match weigth
     */
    public void setMatch_weight(int Match_weight) {
        this.Match_weight = Match_weight;
    }

    /**
     * Return the comparator.
     *
     * @return the comparator.
     */
    public StringComparator getComparator() {
        return comparator;
    }

    /**
     * Replace the default char comparator, implementig user-defined strategy of
     * the character comparison.
     *
     * @param comparator New comparator that must implement CharComparator interface.
     */
    public void setComparator(StringComparator comparator) {
        this.comparator = comparator;
    }

    /**
     * Create the fastest version of aligner, able to return the best score only.
     *
     * @return Time-effective aligner that returns the best score only.
     */
    public ScoreOnlyAligner createScoreOnlyAligner() {

        ScoreOnlyAligner a;
        if (Gapped) {
            if (Global) {
                a = new rgGlobal();
            } else {
                a = new rgLocal();
            }
        } else { // not gapped
            if (Global) { // not gapped global
                if (MaxDifferences >= 0) {
                    Simple_kGlobal ak = new Simple_kGlobal();
                    ak.K = MaxDifferences;
                    a = ak;
                } else {
                    a = new Simple_global();
                }
            } else {
                a = new Simple_local(); // not gapped local
            }
        }

        setParameters((generalSimilarityBasedAligner) a);

        return a;
    }

    /**
     * Create the intermediate version of aligner, able to return the single best alignment.
     *
     * @return Aligner able to return the best alignment.
     */
    public SingleAlingmentAligner createSingleAlingmentAligner() {
        SingleAlingmentAligner a;
        if (Gapped) {
            if (Global) {
                a = new rgGlobal();
            } else {
                a = new rgLocal();
            }
        } else { // not gapped
            if (Global) {
                if (MaxDifferences >= 0) {
                    kGlobal ak = new kGlobal();
                    ak.K = MaxDifferences;
                    a = ak;
                } else {
                    a = new Global();
                }
            } else {
                a = new Local();
            }
        }

        setParameters((generalSimilarityBasedAligner) a);

        return a;

    }

    /**
     * Create the most comperhensive version of aligner, able to return range of suboptimal alignments.
     *
     * @return Aligner, able to return a range of suboptimal alignments.
     */
    public AlignmentRangeAligner createAlignmentRangeAligner() {
        AlignmentRangeAligner a;
        if (Gapped) {
            if (Global) {
                a = new rgGlobal();
            } else {
                a = new rgLocal();
            }
        } else { // not gapped
            if (Global) {
                if (MaxDifferences >= 0) {
                    krGlobal ak = new krGlobal();
                    ak.K = MaxDifferences;
                    a = ak;
                } else {
                    a = new rGlobal();
                }
            } else {
                a = new rLocal();
            }
        }

        setParameters((generalSimilarityBasedAligner) a);

        return a;

    }

    /**
     * Create the aligner expecting the known limited number of differences between
     * two sequences. Only global ungapped aligners are supported in this mode.
     *
     * @param a Implementation specific aligner class for that the parameters must be set.
     */

    protected void setParameters(generalSimilarityBasedAligner a) {
        if (Gap_weight != UNSET) {
            a.GAP_WEIGHT = Gap_weight;
        }
        if (Match_weight != UNSET) {
            a.MATCH_WEIGHT = Match_weight;
        }
        if (Mismatch_weigth != UNSET) {
            a.MISMATCH_WEIGHT = Mismatch_weigth;

        }
        a.NO_END_GAP = !End_gap_penalty;
        a.SHOW_ENDS = Show_Ends;

        if (a.comparator instanceof ScoreMatrixComparator) {
            ScoreMatrixComparator c = (ScoreMatrixComparator) a.comparator;
            c.DefaultMatchValue = Match_weight;
            c.DefaultMismatchValue = Mismatch_weigth;
        }

        a.comparator = comparator;
    }

    /**
     * Special value indicating that the value has not been set. The default
     * recommended value for the chosen aligner will be used.
     */
    public static int UNSET = Integer.MIN_VALUE;

}
