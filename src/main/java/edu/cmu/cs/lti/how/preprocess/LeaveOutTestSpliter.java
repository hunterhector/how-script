package edu.cmu.cs.lti.how.preprocess;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 10/10/14
 * Time: 8:49 PM
 */
public class LeaveOutTestSpliter {
    public static void main(String args[]) throws IOException {
        if (args.length < 2) {
            System.err.println("Provide the data path, base output path and probablity of training data as arguments");
            System.exit(1);
        }

        File intputDir = new File(args[0]);
        File outputDir = new File(args[1]);
        double p = new Double(args[2]);


        Random rand = new Random();


        final String[] SUFFIX = {"xml"};


        File trainDir = new File(outputDir.getAbsolutePath() + "/train_dev/");
        File testDir = new File(outputDir.getAbsolutePath() + "/test/");

        for (File src : FileUtils.listFiles(
                intputDir,
                SUFFIX,
                true
        )) {
            if (rand.nextDouble() > p) {
                FileUtils.copyFileToDirectory(src, trainDir);
            } else {
                FileUtils.copyFileToDirectory(src, testDir);
            }
        }
    }
}
