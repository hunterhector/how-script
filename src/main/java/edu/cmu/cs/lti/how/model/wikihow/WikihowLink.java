package edu.cmu.cs.lti.how.model.wikihow;

import edu.cmu.cs.lti.how.utils.GeneralUtils;
import org.w3c.dom.Element;

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

    public WikihowLink(Element node) {
        super(node);
    }

    public String getLink(){
        return getAnnotation("href");
    }

    public URL getWikihowLink() throws MalformedURLException {
        return new URL(String.format("%s/%s",wikihowDomainName, GeneralUtils.getBaseName(getLink())));
    }

    public String toString(){
        return super.toString()+" ["+getLink()+"]";
    }
}
