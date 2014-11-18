/*
 * This file is licensed to You under the "Simplified BSD License".
 * You may not use this software except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/bsd-license.php
 * 
 * See the COPYRIGHT file distributed with this work for information
 * regarding copyright ownership.
 */
package edu.cmu.cs.lti.how.model.script;


/**
 * Computes the dissimilarity between two observations in an experiment.
 *
 * @author Matthias.Hauswirth@usi.ch
 */
public interface SimilarityMeasure {

    public double computeSimilarity(int observation1, int observation2);

}
