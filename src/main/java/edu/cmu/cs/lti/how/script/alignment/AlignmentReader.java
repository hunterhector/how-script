package edu.cmu.cs.lti.how.script.alignment;

import edu.cmu.cs.lti.how.model.script.ScriptCluster;
import edu.cmu.cs.lti.how.model.script.ScriptClusterLeaveNode;
import edu.cmu.cs.lti.how.model.script.ScriptClusterNode;
import edu.cmu.cs.lti.how.model.script.ScriptClusterNonTerminalNode;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhengzhongliu
 * Date: 12/3/14
 * Time: 10:01 PM
 */
public class AlignmentReader {
    Writer writer;

    public AlignmentReader(Writer writer) {
        this.writer = writer;
    }

    public void check(ScriptClusterNode node, int depth) throws IOException {
        String prefix = StringUtils.repeat("  ", depth);

        if (node.isLeave()) {
            ScriptClusterLeaveNode leaveNode = (ScriptClusterLeaveNode) node;
            for (String event : leaveNode.getEvents()) {
                writeline(prefix, event);
            }
        } else {
            ScriptClusterNonTerminalNode nonTerminalNode = (ScriptClusterNonTerminalNode) node;
            writeline(prefix, "Score: " + nonTerminalNode.getAlignScore());
            writeline(prefix, "Alignment: " + nonTerminalNode.getSource().toString());
            writeline(prefix, "Branching Left");
            check(node.getLeft(), depth + 1);
            writeline(prefix, "Branching Right");
            check(node.getRight(), depth + 1);
        }
    }

    private void writeline(String prefix, String content) throws IOException {
        write(prefix, content + "\n");
    }

    private void write(String prefix, String content) throws IOException {
        writer.write(prefix + content);
    }

    public static void main(String[] args) throws IOException {
        String basename = "alignment_cluster";

        File outDir = new File("data/cluster_out");

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        ScriptCluster cluster = (ScriptCluster) SerializationUtils.deserialize(new FileInputStream(new File("data/alignment_out.ser")));

        for (int i = 0; i < cluster.getCluster().size(); i++) {
            ScriptClusterNode node = cluster.getCluster().get(i);
            PrintWriter writer = new PrintWriter(new FileWriter(new File(outDir, basename + i)), true);
            AlignmentReader reader = new AlignmentReader(writer);
            reader.check(node, 0);
            writer.close();
        }
    }
}
