package edu.cmu.cs.lti.how.model.script.alignment;

import ch.usi.inf.sape.hac.experiment.Experiment;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 11/17/14
 * Time: 12:52 AM
 */
public class ScriptAlignExperiment implements Experiment {
    int numberOfScripts;

    public ScriptAlignExperiment(int numberOfScripts) {
        this.numberOfScripts = numberOfScripts;
    }

    @Override
    public int getNumberOfObservations() {
        return numberOfScripts;
    }
}
