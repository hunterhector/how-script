package org.sStu.Neringute.Similarity;

import java.util.TreeSet;

/**
 * @author Audrius Meskauskas
 * @version 1.0
 *          The set that keeps the certain limited amount (top ten by default)
 *          of its items.
 * @copyright: Copyright (c) 2003 Audrius Meskauskas, General public license (GPL)
 * @see http://www.gnu.org/licenses/gpl.txt
 */

public class LimitedSet {

    /**
     * The score of the worst element.
     */
    double worstScore = -Double.MIN_VALUE;

    public LimitedSet() {
        this(20);
    }

    ;

    /**
     * The maximal limit of the collection size.
     */
    public final int Limit;

    /**
     * The set that actually holds the collection. All methods except
     * the add method can be used.
     */
    public TreeSet set;

    public LimitedSet(int limit) {
        Limit = limit;
        set = new TreeSet();
    }

    /**
     * Truncate the set till its limit if it have been overgrown.
     */
    public void Truncate(int limit) {
        while (set.size() > limit) {
            set.remove(set.last());
        }
    }

    /**
     * Adds an object to the set.
     */
    public void add(rGlobal.Alignment a) {
        if (a.Score < worstScore) {
//            System.out.println("Rejected " + a.Score + " " + worstScore);
            return; // this one is bad.
        }

        if (set.size() < Limit || (a.compareTo(set.last())) < 0) {
            set.add(a);
            Truncate(Limit);
//            System.out.println("Worst is " + worstScore);
            worstScore = ((rGlobal.Alignment) set.last()).Score;
        }
    }

    public void clear() {
        worstScore = -Double.MIN_VALUE;
        set.clear();
    }


    public rGlobal.Alignment best() {
        return (rGlobal.Alignment) set.first();
    }

}