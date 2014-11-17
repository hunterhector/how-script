package org.sStu.demo;

import org.sStu.AlignerFactory;

import java.lang.reflect.Method;

public class JavaGenerator {

  static StringBuffer b = new StringBuffer();
  static Class[] NO_ARGS = new Class[0];

  void p(String s) {
    b.append("  " + s + ";\n");
  }

  public static synchronized String generate(demoApplet app) {
    AlignerFactory f = app.f;
    b.setLength(0);
    b.append(
        "import org.sStu.*;\n\n" +
        "class example { \n" +
        "   public static void main(String[] args) {\n" +
        "    AlignerFactory factory = new AlignerFactory();\n\n");

    // simplified java bean like style
    Method[] m = f.getClass().getMethods();

    Methods:
        for (int i = 0; i < m.length; i++) {
      if (m[i].getName().startsWith("set")) {
        try {
          Class[] pTypes = m[i].getParameterTypes();
          if (! (pTypes.length == 1) || ! (pTypes[0].isPrimitive())) {
            continue Methods;
          }

          String prfx = pTypes[0].getName().equals("boolean") ? "is" : "get";

          String getName = prfx + m[i].getName().substring(3);

          Method get = f.getClass().getMethod(getName, NO_ARGS);
          Object parameter = get.invoke(f, NO_ARGS);
          if (!parameter.toString().equals("" + f.UNSET)) {
            b.append("    factory." + m[i].getName() + "(" + parameter + ");\n");
          }
        }
        catch (SecurityException ex) {
          b.append("\nSecurity manager prevents code generation.\n");
          return b.toString();
        }
        catch (Exception ex) {
          continue Methods;
        }
      }
    }

    b.append("\n");

    String acn;

    if (app.rScore.isSelected()) {
      acn = "ScoreOnlyAligner";
    }
    else
    if (app.rBestAl.isSelected()) {
      acn = "SingleAlingmentAligner";
    }
    else {
      acn = "AlignmentRangeAligner";

    }
    b.append("    " + acn + " aligner = factory.create" + acn + "();\n");
    b.append("    aligner.setSequence(\"" + app.jTextArea1.getText() +
             "\",0);\n");
    b.append("    aligner.setSequence(\"" + app.jTextArea2.getText() +
             "\",1);\n");
    b.append("    aligner.align();\n");
    b.append("\n");
    b.append("    System.out.println(aligner.getAlignmentScore());\n");

    if (acn.equals("SingleAlingmentAligner")) {
      b.append("   Alignment alignment = aligner.getBestAlignment();\n");
      b.append("   System.out.println(alignment.toString());\n");
    }
    else
    if (acn.equals("AlignmentRangeAligner")) {
      b.append("\n    Alignment[] alignments = aligner.getAlignments();\n");
      b.append("    for (int i = 0; i<alignments.length; i++)\n");
      b.append("     System.out.println(alignments[i].toString());\n");
    }
    b.append("  }\n}\n");

    return b.toString();
  }
}
