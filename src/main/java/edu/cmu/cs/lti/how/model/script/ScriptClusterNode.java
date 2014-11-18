package edu.cmu.cs.lti.how.model.script;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 11/17/14
 * Time: 2:58 PM
 */
public interface ScriptClusterNode extends Serializable {
    public ScriptClusterNode getLeft();

    public ScriptClusterNode getRight();

    public int getObservationCount();

    public boolean isLeave();

    public String[] getSequence();
}
