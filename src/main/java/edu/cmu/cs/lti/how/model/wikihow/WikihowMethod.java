package edu.cmu.cs.lti.how.model.wikihow;

import edu.cmu.cs.lti.how.utils.Joiners;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 9/19/14
 * Time: 11:15 PM
 */
public class WikihowMethod implements Serializable{
    public WikihowMethod(Element topNode){
        steps = new ArrayList<WikihowStep>();

        NodeList children = topNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeName().equals("step")) {
                try{
                    steps.add(new WikihowStep(child));
                }catch(Exception e){

                }
            }
        }
    }

    private List<WikihowStep> steps;

    public List<WikihowStep> getSteps() {
        return steps;
    }

    public void setSteps(List<WikihowStep> steps) {
        this.steps = steps;
    }

    public String toString(){
        return steps.size()+" steps: \n"+Joiners.nlJoiner.join(steps);
    }
}
