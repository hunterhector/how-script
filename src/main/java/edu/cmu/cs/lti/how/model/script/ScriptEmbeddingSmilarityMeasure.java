package edu.cmu.cs.lti.how.model.script;

import com.google.common.collect.ArrayListMultimap;
import org.sStu.ScoreOnlyAligner;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 11/17/14
 * Time: 12:52 AM
 */
public class ScriptEmbeddingSmilarityMeasure implements SimilarityMeasure {
    List<String> allScriptNames;

    ArrayListMultimap<String, String> filename2Sents;
    ScoreOnlyAligner aligner;

    public ScriptEmbeddingSmilarityMeasure(List<String> allScriptNames, ScoreOnlyAligner aligner, ArrayListMultimap<String, String> filename2Sents) {
        this.aligner = aligner;
        this.allScriptNames = allScriptNames;
        this.filename2Sents = filename2Sents;
    }

    @Override
    public double computeSimilarity(int observation1, int observation2) {

        List<String> script1 = filename2Sents.get(allScriptNames.get(observation1));
        List<String> script2 = filename2Sents.get(allScriptNames.get(observation2));

//        System.out.println("Aligning ");
//        System.out.println(script1);
//        System.out.println(script2);

        aligner.setSequence(script1.toArray(new String[script1.size()]), 0);
        aligner.setSequence(script2.toArray(new String[script2.size()]), 1);

        aligner.align();

//        System.out.println("Score "+ aligner.getAlignmentScore());

        return aligner.getAlignmentScore();
    }
}
