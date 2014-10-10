package edu.cmu.cs.lti.how.model.script;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 10/9/14
 * Time: 3:58 PM
 */
public class AlignedSequence {
    List<List<TIntObjectHashMap<EventMention>>> sparseSequenceDist = new ArrayList<List<TIntObjectHashMap<EventMention>>>();

    private double alignmentScore;

    public AlignedSequence() {

    }

    public int numAligned() {
        return sparseSequenceDist.size();
    }

    public void setAlignmentScore(double score){
        this.alignmentScore = score;
    }

}
