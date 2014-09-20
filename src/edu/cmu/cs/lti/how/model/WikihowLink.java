package edu.cmu.cs.lti.how.model;

import edu.cmu.cs.lti.how.utils.GeneralUtils;
import org.w3c.dom.Node;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 9/19/14
 * Time: 11:41 PM
 */
public class WikihowLink extends TextElement{
    private String wikihowDomainName = "www.wikihow.com";

    public WikihowLink(Node topNode) {
        super(topNode);
    }

    public String getLink(){
        return getAnnotation("href");
    }

    public URL getWikihowLink() throws MalformedURLException {
        return new URL(String.format("%s/%s",wikihowDomainName, GeneralUtils.getBaseName(getLink())));
    }



}