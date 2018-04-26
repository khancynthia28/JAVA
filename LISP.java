import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import java.io.*;
import java.util.*;
import java.lang.*;

public class LISP {

  static boolean evalError = false;
  static boolean divError = false;
  static boolean listError = false;

  static public void main(String argv[]) {    
    System.out.print("LISP> ");
    do {
      String input = readInput().trim();
      if (input.equals("exit")) 
        break;
      else input += ";";
      try {
        CharStream in = CharStreams.fromString(input);
        LISPLexer lexer = new LISPLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LISPParser parser = new LISPParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy());
        LISPNode tree = (LISPNode) parser.lispStart().value;
        Integer level = new Integer(1);
        displayTree(tree,level);
        evalError = false;
        double answer = evalTree(tree);
        if (evalError) {
          if (divError) {
            divError = false;
            System.out.println("\nEVALUATION ERROR: Division Error\n");
            }
          else if (listError) {
            listError = false;
            System.out.println("\nEVALUATION ERROR: Not enough items in the list\n");
          }
        }
        else
          System.out.println("\nThe value is "+answer+"\n");
      } catch (Exception e) {
          System.out.println("\nSYNTAX ERROR\n");
          //e.printStackTrace();
        }
    } while (true); 
  }

  static double evalTree(LISPNode t) {
    if (t.getNodeType().equals("num")) {
      return t.getValue();
    }
    else if (t.getNodeType().equals("plus")) {
      double v1 = evalTree(t.getChild1());
      if (evalError) return 0;
      double v2 = evalTree(t.getChild2());
      if (evalError) return 0;
      return v1+v2;
    }
    else if (t.getNodeType().equals("minus")) {
      double v1 = evalTree(t.getChild1());
      if (evalError) return 0;
      double v2 = evalTree(t.getChild2());
      if (evalError) return 0;
      return v1-v2;
    }
    else if (t.getNodeType().equals("multiply")) {
      double v1 = evalTree(t.getChild1());
      if (evalError) return 0;
      double v2 = evalTree(t.getChild2());
      if (evalError) return 0;
      return v1*v2;
    }
    else if (t.getNodeType().equals("divide")) {
      double v1 = evalTree(t.getChild1());
      if (evalError) return 0;
      double v2 = evalTree(t.getChild2());
      if (evalError) return 0;
      if (v2 != 0)
        return v1/v2;
      else {
        evalError = true;
        divError = true;
        return 0;
      }
    }
    else if (t.getNodeType().equals("car")) {
      LISPNode p = t.getListValue();
      if (p.getNodeType().equals("cdr")) {
        int count = 0;
        while (p.getNodeType().equals("cdr")){
            count++;
            p = p.getListValue();
        }
        while ((count > 0) && (p!=null)){
          count--;
          p = p.getNext();
        }
        if (p!=null) {
          double v = evalTree(p);
          return v;
        }
        else {
         evalError = true;
         listError = true;
         return 0;
        }
      }
      else {
        double v = evalTree(t.getListValue());
        return v;
      }
    }
    else { 
      evalError = true;
      return 0;
    }
  }

  static void displayTree(LISPNode t,Integer level) {
    for (int i=1; i<level.intValue(); i++)
      System.out.print("    ");
    System.out.print("NodeType="+t.getNodeType()+"  ");
    if (t.getNodeType().equals("num"))
      System.out.println("Value="+t.value);
    else if (t.getNodeType().equals("plus") || 
             t.getNodeType().equals("minus") ||
             t.getNodeType().equals("multiply") ||
             t.getNodeType().equals("divide")) { 
      System.out.println();
      int lval = level.intValue();
      lval++;
      level = new Integer(lval);
      displayTree(t.getChild1(),level);
      displayTree(t.getChild2(),level);
    }
    else if (t.getNodeType().equals("car") ||
             t.getNodeType().equals("cdr")){
      System.out.println("");
      int lval = level.intValue();
      lval++;
      level = new Integer(lval);
      LISPNode p = t.getListValue();
      while (p!= null){
        displayTree(p, level);
        p=p.getNext();
      }
    }
  }

  static String readInput() {
     try {
       StringBuffer buffer = new StringBuffer();
       System.out.flush();
       int c = System.in.read();
       while(c != ';' && c != -1) {
         if (c != '\n') 
           buffer.append((char)c);
         else {
           buffer.append(" ");
           System.out.print("LISP> ");
           System.out.flush();
         }
         c = System.in.read();
       }
       return buffer.toString().trim();
     } catch (IOException e) {
         return "";
       }
   }

}
