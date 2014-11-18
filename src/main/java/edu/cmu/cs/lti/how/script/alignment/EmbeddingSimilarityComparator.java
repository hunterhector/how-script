package edu.cmu.cs.lti.how.script.alignment;

import org.sStu.StringComparator;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 11/17/14
 * Time: 12:17 AM
 */
public class EmbeddingSimilarityComparator extends StringComparator {
    @Override
    public double compare(String a, String b) {
        return cosine(rep2Double(a), rep2Double(b));
    }

    private double[] rep2Double(String r) {
        String[] rs = r.split(" ");

        double[] rep = new double[rs.length];

        for (int i = 0; i < rs.length; i++) {
            rep[i] = Double.parseDouble(rs[i]);
        }
        return rep;
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
