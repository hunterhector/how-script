package edu.cmu.cs.lti.how.model.script;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 11/19/14
 * Time: 3:33 PM
 */
public class ScriptCluster implements Serializable {
    private static final long serialVersionUID = -5213424086505747065L;

    private List<ScriptClusterNode> clusters = new ArrayList<>();

    public ScriptCluster(List<ScriptClusterNode> c) {
        this.clusters.addAll(c);
    }

    public void addNode(ScriptClusterNode n) {
        clusters.add(n);
    }

    public List<ScriptClusterNode> getCluster() {
        return clusters;
    }
}
