package edu.cmu.cs.lti.how.model.script;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 11/17/14
 * Time: 3:00 PM
 */
public class ScriptClusterLeaveNode implements ScriptClusterNode {
    private static final long serialVersionUID = -7941927533152785871L;

    String[] events;
    String[] sequences;

    public ScriptClusterLeaveNode(List<String> events, Map<String, String> sent2Rep) {
        this.events = events.toArray(new String[events.size()]);
        sequences = new String[events.size()];
        for (int i = 0; i < events.size(); i++) {
            sequences[i] = sent2Rep.get(events.get(i));
        }
    }

    @Override
    public ScriptClusterNode getLeft() {
        return null;
    }

    @Override
    public ScriptClusterNode getRight() {
        return null;
    }

    @Override
    public int getObservationCount() {
        return 1;
    }

    @Override
    public boolean isLeave() {
        return true;
    }


    public String[] getEvents() {
        return events;
    }


    @Override
    public String[] getSequence() {
        return sequences;
    }
}