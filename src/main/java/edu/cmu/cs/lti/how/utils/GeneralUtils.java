package edu.cmu.cs.lti.how.utils;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 9/20/14
 * Time: 1:16 AM
 */
public class GeneralUtils {
    public static String getBaseName(String fileName){
        String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
        return tokens[0];
    }
    public static String replaceMultipleNewlinesWithOne(String str){
        return str.replaceAll("[\r\n]+", "\n");
    }

}
