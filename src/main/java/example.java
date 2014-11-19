import org.sStu.AlignerFactory;
import org.sStu.AlignmentRangeAligner;

class example {
    public static void main(String[] args) {
        AlignerFactory factory = new AlignerFactory();

        factory.setGap_weight(0);
        factory.setMatch_weight(1);
        factory.setMismatch_weigth(-1);
        factory.setGlobal(true);
        factory.setEnd_gap_penalty(false);
        factory.setGapped(false);
        factory.setShow_Ends(false);

//    ScoreOnlyAligner aligner = factory.createScoreOnlyAligner();
        AlignmentRangeAligner aligner = factory.createAlignmentRangeAligner();
        test(aligner);
        test(aligner);
        test(aligner);
        test(aligner);
        test(aligner);
        test(aligner);

    }

    private static void test(AlignmentRangeAligner aligner) {
//        aligner.setSequence("prepare cut fry stir finish".split(" "), 0);
//        aligner.setSequence("prepare bake finish".split(" "), 1);
//        aligner.align();
//        for (Alignment a : aligner.getAlignments()) {
//            System.out.println("Alignment result:");
//            System.out.println(a);
//            System.out.println(a.getAlignmentScore());
//            System.out.println(aligner.getAlignmentScore());
//            break;
//        }
    }
}
