package edu.cmu.cs.lti.how.alignment;

import edu.cmu.cs.lti.how.model.script.AlignedSequence;
import edu.cmu.cs.lti.how.model.script.EventMention;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 10/9/14
 * Time: 3:42 PM
 */
public abstract class SequenceAligner {
    /**
     * Align two sequence
     * @param sequence1
     * @param sequence2
     * @return
     */
    public abstract AlignedSequence pairwiseSequenceAlign(List<EventMention> sequence1, List<EventMention> sequence2);

    public abstract List<AlignedSequence> multiSequenceAlign(List<List<EventMention>> sequences);
}
