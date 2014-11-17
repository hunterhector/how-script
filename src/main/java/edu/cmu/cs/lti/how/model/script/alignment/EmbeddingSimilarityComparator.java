package edu.cmu.cs.lti.how.model.script.alignment;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.sStu.StringComparator;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 11/17/14
 * Time: 12:17 AM
 */
public class EmbeddingSimilarityComparator extends StringComparator {
    private Map<String, double[]> sent2Rep;

    private Table<String, String, Double> cache;

    public EmbeddingSimilarityComparator(Map<String, double[]> sent2Rep) {
        this.sent2Rep = sent2Rep;
        cache = HashBasedTable.create();
    }

    @Override
    public double compare(String a, String b) {
        if (cache.contains(a, b)) {
            return cache.get(a, b);
        } else {
            double cos = cosine(sent2Rep.get(a), sent2Rep.get(b));
            cache.put(a, b, cos);
            return cos;
        }
    }

    private double cosine(double[] rep1, double[] rep2) {
        double prod = 0;
        double len1 = 0;
        double len2 = 0;
        for (int i = 0; i < rep1.length; i++) {
            prod += rep1[i] * rep2[i];
            len1 += rep1[i] * rep1[i];
            len2 += rep2[i] * rep2[i];
        }
        return prod / Math.sqrt(len1 * len2);
    }
}
