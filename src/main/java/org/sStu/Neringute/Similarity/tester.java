package org.sStu.Neringute.Similarity;

import org.sStu.Neringute.*;

/**
 * <p>Title: Sequences study</p>
 * <p>Description: Package for working with sequences</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Audrius Meskauskas</p>
 * @author Audrius Meskauskas
 * @version 1.0
 */

public class tester {
  public tester() {
  }

  public static String randomSequence() {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < 1000; i++) {
      char c = (char) ('a' + (char) ( (Math.random() * (int) ('z' - 'a'))));
      b.append(c);
    }
    String s = b.toString();
    return s;
  }

  public static void main(String[] args) {
    String s1 = randomSequence() + "sis asilas ilgai ilsisi" + randomSequence();
    String s2 = randomSequence() + "ilgai asilas sis ilsisizabc" +
        randomSequence();
    ngAligner[] e =
        new ngAligner[] {
        new krGlobal(),
        new rGlobal(),
        new rgGlobal(),
        new Global(),
        new Simple_global(),
        new Local(),
        new rLocal(),
        new rgLocal()
    };

    for (int i = 0; i < e.length; i++) {
      // set the sequences
    }

    for (int c = 0; c < 10; c++) {
      if (c % 1 == 0) {
        System.out.print(".");
      }
      for (int i = 0; i < e.length; i++) {
        e[i].align();
      }
    }
  }

}