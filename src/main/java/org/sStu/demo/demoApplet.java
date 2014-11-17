package org.sStu.demo;

import org.sStu.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>Title: Sequences study</p>
 * <p>Description: Package for working with sequences</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Audrius Meskauskas</p>
 *
 * @author Audrius Meskauskas
 * @version 1.0
 */

public class demoApplet
        extends JApplet {
    private boolean isStandalone = false;
    JPanel jPanel5 = new JPanel();
    JRadioButton rComprehensive = new JRadioButton();
    JRadioButton rBestAl = new JRadioButton();
    JRadioButton rScore = new JRadioButton();
    JLabel jLabel4 = new JLabel();
    JLabel jLabel5 = new JLabel();
    JRadioButton rGlobal = new JRadioButton();
    JRadioButton rLocal = new JRadioButton();
    JCheckBox cbEnd_Gap_p = new JCheckBox();
    JCheckBox cbGapped = new JCheckBox();
    JLabel jLabel6 = new JLabel();
    JPanel jPanel6 = new JPanel();
    GridLayout gridLayout2 = new GridLayout();
    JLabel jLabel7 = new JLabel();
    JSpinner taGap = new JSpinner();
    JLabel lGap = new JLabel();
    JSpinner taMismatch = new JSpinner();
    JLabel jLabel9 = new JLabel();
    JSpinner taMatch = new JSpinner();
    JCheckBox cbShowEnds = new JCheckBox();
    JButton bAlign = new JButton();
    JPanel jPanel7 = new JPanel();
    GridLayout gridLayout1 = new GridLayout();
    JPanel jPanel1 = new JPanel();
    JLabel jLabel2 = new JLabel();
    JTextArea jTextArea2 = new JTextArea();
    BorderLayout borderLayout2 = new BorderLayout();
    JScrollPane jScrollPane1 = new JScrollPane();
    JLabel jLabel1 = new JLabel();
    JPanel jPanel2 = new JPanel();
    JTextArea jTextArea1 = new JTextArea();
    JScrollPane jScrollPane2 = new JScrollPane();
    JPanel jPanel3 = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel4 = new JPanel();
    JTextArea jResult = new JTextArea();
    JLabel lbResult = new JLabel();
    BorderLayout borderLayout3 = new BorderLayout();
    JScrollPane jScrollPane3 = new JScrollPane();
    BorderLayout borderLayout4 = new BorderLayout();
    ButtonGroup Output = new ButtonGroup();
    ButtonGroup LG = new ButtonGroup();
    JLabel lbAlClass = new JLabel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();

    //Get a parameter value
    public String getParameter(String key, String def) {
        return isStandalone ? System.getProperty(key, def) :
                (getParameter(key) != null ? getParameter(key) : def);
    }

    //Construct the applet
    public demoApplet() {
    }

    //Initialize the applet
    public void init() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Component initialization
    private void jbInit() throws Exception {
        this.setSize(new Dimension(500, 500));
        jPanel5.setLayout(gridBagLayout1);
        rComprehensive.setText("Comprehensive");
        rComprehensive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                paraAltered(e);
            }
        });
        rBestAl.setSelected(true);
        rBestAl.setText("Best alingment");
        rBestAl.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                paraAltered(e);
            }
        });
        rScore.setText("Score only");
        rScore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                paraAltered(e);
            }
        });
        jLabel4.setFont(new Font("Dialog", 1, 11));
        jLabel4.setText("Output:");
        jLabel5.setFont(new Font("Dialog", 1, 11));
        jLabel5.setText("Type");
        rGlobal.setSelected(true);
        rGlobal.setText("Global");
        rGlobal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rLocal_actionPerformed(e);
            }
        });
        rLocal.setText("Local");
        rLocal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rLocal_actionPerformed(e);
            }
        });
        cbEnd_Gap_p.setMargin(new Insets(2, 12, 2, 2));
        cbEnd_Gap_p.setText("End gap penalty");
        cbEnd_Gap_p.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                paraAltered(e);
            }
        });
        cbGapped.setText("Gapped");
        cbGapped.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cbGapped_actionPerformed(e);
            }
        });
        jLabel6.setFont(new Font("Dialog", 1, 11));
        jLabel6.setText("Scores");
        jPanel6.setLayout(gridLayout2);
        jLabel7.setText("Match");

        gridLayout2.setColumns(2);
        gridLayout2.setRows(0);
        lGap.setText("Gap");
        taGap.setValue(new Integer(-2));
        taGap.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                paraAltered(e);
            }
        });
        taMismatch.setValue(new Integer(-1));
        taMismatch.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                paraAltered(e);
            }
        });
        jLabel9.setText("Mismatch");
        cbShowEnds.setMargin(new Insets(2, 2, 2, 2));
        cbShowEnds.setText("Show ends");
        cbShowEnds.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                paraAltered(e);
            }
        });
        taMatch.setValue(new Integer(2));
        taMatch.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                paraAltered(e);
            }
        });
        bAlign.setText("Align now");
        bAlign.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bAlign_actionPerformed(e);
            }
        });
        jPanel1.setLayout(gridLayout1);
        jPanel1.setPreferredSize(new Dimension(176, 100));
        jLabel2.setFont(new Font("Dialog", 1, 11));
        jLabel2.setToolTipText("");
        jLabel2.setText(" Second sequence");
        //jTextArea2.setText("this cat must be in gif format");
        jTextArea2.setText("abba baba kvakva");
        jTextArea2.setLineWrap(true);
        jLabel1.setFont(new Font("Dialog", 1, 11));
        jLabel1.setText(" First sequence");
        jPanel2.setLayout(borderLayout1);
        //jTextArea1.setText("gif image format suits for a cat picture");
        jTextArea1.setText("baba abba");
        jTextArea1.setLineWrap(true);
        jPanel3.setLayout(borderLayout2);
        jPanel4.setLayout(borderLayout3);
        lbResult.setFont(new Font("Dialog", 1, 11));
        lbResult.setHorizontalAlignment(SwingConstants.CENTER);
        lbResult.setText("Result");
        jPanel7.setLayout(borderLayout4);
        lbAlClass.setText("Aligner class: ");
        lbAlClass.setVerticalTextPosition(SwingConstants.CENTER);
        jResult.setFont(new Font("Monospaced", 0, 12));
        jResult.setText("");
        cbJava.setText("Show java code");
        cbJava.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cbJava_actionPerformed(e);
            }
        });
        jButton1.setMargin(new Insets(0, 14, 0, 14));
        jButton1.setText("Paste");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jButton1_actionPerformed(e);
            }
        });
        jButton2.setMargin(new Insets(0, 14, 0, 14));
        jButton2.setText("Paste");
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jButton2_actionPerformed(e);
            }
        });
        jButton3.setMargin(new Insets(0, 14, 0, 14));
        jButton3.setText("Copy");
        jButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jButton3_actionPerformed(e);
            }
        });
        jPanel10.setLayout(borderLayout5);
        jPanel8.setLayout(borderLayout6);
        jPanel9.setLayout(borderLayout7);
        borderLayout4.setVgap(4);
        this.getContentPane().add(jPanel5, BorderLayout.EAST);
        jPanel5.add(jLabel4, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 73, 0));
        jPanel5.add(rComprehensive, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 12, 0));
        jPanel5.add(rBestAl, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 18, 0));
        jPanel5.add(rScore, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 36, 0));
        jPanel5.add(cbShowEnds, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        jPanel5.add(jLabel5, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 86, 0));
        jPanel5.add(rGlobal, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        jPanel5.add(cbEnd_Gap_p, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 5), 0, 0));
        jPanel5.add(rLocal, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        jPanel5.add(cbGapped, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 15, 0, 5), 50, 0));
        jPanel5.add(jLabel6, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 74, 0));
        jPanel5.add(jPanel6, new GridBagConstraints(0, 12, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 23, 0));
        jPanel6.add(jLabel7, null);
        jPanel6.add(taMatch, null);
        jPanel6.add(jLabel9, null);
        jPanel6.add(taMismatch, null);
        jPanel6.add(lGap, null);
        jPanel6.add(taGap, null);
        jPanel5.add(bAlign, new GridBagConstraints(0, 13, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        jPanel5.add(cbJava, new GridBagConstraints(0, 14, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(jPanel7, BorderLayout.CENTER);
        jPanel4.add(jScrollPane3, BorderLayout.CENTER);
        jScrollPane3.getViewport().add(jResult, null);
        jPanel7.add(jPanel4, BorderLayout.CENTER);
        jPanel7.add(jPanel1, BorderLayout.NORTH);
        jPanel2.add(jScrollPane1, BorderLayout.CENTER);
        jPanel2.add(jPanel8, BorderLayout.NORTH);
        jPanel8.add(jLabel1, BorderLayout.CENTER);
        jPanel8.add(jButton1, BorderLayout.EAST);
        jScrollPane1.getViewport().add(jTextArea1, null);

        jPanel1.add(jPanel2, null);
        jPanel1.add(jPanel3, null);

        jPanel3.add(jScrollPane2, BorderLayout.CENTER);
        jPanel3.add(jPanel9, BorderLayout.NORTH);
        jPanel9.add(jLabel2, BorderLayout.CENTER);
        jPanel9.add(jButton2, BorderLayout.EAST);
        jScrollPane2.getViewport().add(jTextArea2, null);
        LG.add(rGlobal);
        LG.add(rLocal);
        Output.add(rComprehensive);
        Output.add(rBestAl);
        Output.add(rScore);
        jPanel4.add(lbAlClass, BorderLayout.SOUTH);
        jPanel4.add(jPanel10, BorderLayout.NORTH);
        jPanel10.add(lbResult, BorderLayout.CENTER);
        jPanel10.add(jButton3, BorderLayout.EAST);
        cbGapped_actionPerformed(null);
    }

    //Start the applet
    public void start() {
    }

    //Stop the applet
    public void stop() {
    }

    //Destroy the applet
    public void destroy() {
    }

    //Get Applet information
    public String getAppletInfo() {
        return "Applet Information";
    }

    //Get parameter info
    public String[][] getParameterInfo() {
        return null;
    }

    //Main method
    public static void main(String[] args) {
        demoApplet applet = new demoApplet();
        applet.isStandalone = true;
        JFrame frame = new JFrame();
        //EXIT_ON_CLOSE == 3
        frame.setDefaultCloseOperation(3);
        frame.setTitle("Applet Frame");
        frame.getContentPane().add(applet, BorderLayout.CENTER);
        applet.init();
        applet.start();
        frame.setSize(640, 480);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((d.width - frame.getSize().width) / 2,
                (d.height - frame.getSize().height) / 2);
        frame.setVisible(true);
    }

    //static initializer for setting look & feel
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void rLocal_actionPerformed(ActionEvent e) {

        boolean g = !rLocal.isSelected();
        cbEnd_Gap_p.setEnabled(g);
        paraAltered(e);
    }

    void cbGapped_actionPerformed(ActionEvent e) {
        boolean b = cbGapped.isSelected();
        taGap.setEnabled(b);
        lGap.setEnabled(b);
        paraAltered(e);
    }

    org.sStu.AlignerFactory f = new org.sStu.AlignerFactory();
    JCheckBox cbJava = new JCheckBox();
    JPanel jPanel8 = new JPanel();
    JButton jButton1 = new JButton();
    JPanel jPanel9 = new JPanel();
    JButton jButton2 = new JButton();
    JPanel jPanel10 = new JPanel();
    JButton jButton3 = new JButton();
    BorderLayout borderLayout5 = new BorderLayout();
    BorderLayout borderLayout6 = new BorderLayout();
    BorderLayout borderLayout7 = new BorderLayout();

    void paraAltered(Object e) {

        f.setGap_weight(get(taGap));
        f.setMatch_weight(get(taMatch));
        f.setMismatch_weigth(get(taMismatch));

        f.setEnd_gap_penalty(cbEnd_Gap_p.isSelected());
        f.setGapped(cbGapped.isSelected());
        f.setShow_Ends(cbShowEnds.isSelected());

        f.setGlobal(rGlobal.isSelected());

        if (cbJava.isSelected()) {
            cbJava_actionPerformed(null);
            return;
        }

        Object al;

        if (rScore.isSelected()) {
            al = sDemo();
        } else if (rBestAl.isSelected()) {
            al = a1Demo();
        } else {
            al = rDemo();

        }
        lbAlClass.setText("Aligner class: " + al.getClass().getName());
    }

    void align(Aligner aligner) {
        aligner.setSequence(jTextArea1.getText().split(""), 0);
        aligner.setSequence(jTextArea2.getText().split(""), 1);
        aligner.align();
    }

    AlignmentRangeAligner rDemo() {
        AlignmentRangeAligner a = f.createAlignmentRangeAligner();
        align(a);
        jResult.setText("Best score: " + a.getAlignmentScore() + ". Alignments:");
        Alignment[] als = a.getAlignments();
        for (int i = 0; i < als.length; i++) {
            jResult.append("\n\nScore:" + als[i].getAlignmentScore() +
                    als[i].toString());
        }
        jResult.setSelectionStart(0);
        jResult.setSelectionEnd(0);
        return a;
    }

    SingleAlingmentAligner a1Demo() {
        SingleAlingmentAligner a = f.createSingleAlingmentAligner();
        align(a);
        jResult.setText("Alignment score: " + a.getAlignmentScore() +
                "\nBest alignment: \n");
        jResult.append(a.getBestAlignment().toString());

        return a;
    }

    ScoreOnlyAligner sDemo() {
        ScoreOnlyAligner a = f.createScoreOnlyAligner();
        align(a);
        jResult.setText("Alignment score: " + a.getAlignmentScore());
        return a;
    }

    int get(JSpinner t) {
        try {
            return Integer.parseInt(t.getValue().toString().trim());
        } catch (NumberFormatException ex) {
            return f.UNSET;
        }
    }

    void bAlign_actionPerformed(ActionEvent e) {
        paraAltered(e);
    }

    void cbJava_actionPerformed(ActionEvent e) {
        if (cbJava.isSelected()) {
            jResult.setText(JavaGenerator.generate(this));
            jResult.setSelectionStart(0);
            jResult.setSelectionEnd(0);
        }
    }

    void jButton1_actionPerformed(ActionEvent e) {
        jTextArea1.setText("");
        jTextArea1.paste();
    }

    void jButton2_actionPerformed(ActionEvent e) {
        jTextArea2.setText("");
        jTextArea2.paste();
    }

    void jButton3_actionPerformed(ActionEvent e) {
        jResult.selectAll();
        jResult.copy();
        jResult.setSelectionEnd(0);
    }

    void paraAltered(ChangeEvent e) {
        paraAltered((Object) e);

    }

}
