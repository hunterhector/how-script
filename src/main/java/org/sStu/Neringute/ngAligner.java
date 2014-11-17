package org.sStu.Neringute;

/**
 * <p>The topmost object for creating different aligners.
 * Contains the methods, shared by the most of the algorithms.</p>
 * <p>Description: Package for working with sequences</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Audrius Meskauskas</p>
 *
 * @author Audrius Meskauskas
 * @version 1.1
 */

public class ngAligner {

    /**
     * The value of the first, unused character in the char arrays,
     * where the char numeration begins from 1.
     */
    public static final String NULL_POSITION = "*";

    /**
     * Convert java charsequence to the char array where the
     * numeration is from
     * one, the first character is not used.
     */
    public String[] toStringArray(String[] x) {
        String[] c;
        c = new String[x.length + 1];
        c[0] = NULL_POSITION;
        int p = 1;
        for (int i = 0; i < x.length; i++) {
            c[p++] = x[i];
        }
        return c;
    }

    public void align() {
    }

    ;
}
