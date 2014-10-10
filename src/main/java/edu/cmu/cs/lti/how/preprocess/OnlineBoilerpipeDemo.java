package edu.cmu.cs.lti.how.preprocess;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 9/5/14
 * Time: 9:06 PM
 */
public class OnlineBoilerpipeDemo {
    public static void main(String[] args) throws IOException, BoilerpipeProcessingException {
        URL randomUrl  = new URL("http://www.wikihow.com/Special:Randomizer");

        HttpURLConnection con = (HttpURLConnection)(randomUrl.openConnection());
        con.setInstanceFollowRedirects( false );
        con.connect();
        String location = con.getHeaderField( "Location" );

        URL url = new URL("http://www.wikihow.com/Bake-a-Cake");

        System.out.println(url);

        System.out.println(ArticleExtractor.INSTANCE.getText(url));
    }
}