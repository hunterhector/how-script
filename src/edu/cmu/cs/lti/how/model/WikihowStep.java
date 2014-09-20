package edu.cmu.cs.lti.how.model;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 9/19/14
 * Time: 11:16 PM
 */
public class WikihowStep implements Serializable {
    public WikihowStep(Node topNode) throws Exception {
        NodeList children = topNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (child.getNodeName().equals("em")){
                step = new ContentElement(child,true);
            }else if (child.getNodeName().equals("details")){
                details = new ContentElement(child);
            }
        }

        if (step == null || details == null){
            throw new Exception("Step or detail not presented");
        }
    }

    private ContentElement step;
    private ContentElement details;

    public ContentElement getStep() {
        return step;
    }

    public void setStep(ContentElement step) {
        this.step = step;
    }

    public ContentElement getDetails() {
        return details;
    }

    public void setDetails(ContentElement details) {
        this.details = details;
    }

    @Override
    public String toString(){
        return "\t[STEP] "+step+"\n"+"\t[DETAILS] "+details;
    }
}
