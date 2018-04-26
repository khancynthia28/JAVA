public class LISPNode {
  String nodetype; // "num", "plus', "minus", "multiply", "divide", "car", "cdr"
  double value;
  LISPNode child1, child2, listValue, next;

  LISPNode () {
  }

  public void setNodeType(String n) {
    nodetype = n;
  }

  public void setValue(double v) {
    value = v;
  }

  public void setChild1(LISPNode n) {
    child1 = n;
  }

  public void setChild2(LISPNode n) {
    child2 = n;
  }

  public void setListValue(LISPNode n) {
    listValue = n;
  }

  public void setNext(LISPNode n) {
    next = n;
  }

  public String getNodeType() {
    return nodetype;
  }

  public double getValue() {
    return value;
  }

  public LISPNode getChild1() {
    return child1;
  }

  public LISPNode getChild2() {
    return child2;
  }

  public LISPNode getListValue() {
    return listValue;
  }

  public LISPNode getNext() {
    return next;
  }

}
