import java.awt.Color;
import java.awt.Graphics;

public class Truss
  implements Drawable
{
  private Node node1;
  private Node node2;
  private double value;
  private boolean valueDisplay;

  public Truss(Node node1, Node node2)
  {
    this.node1 = node1;
    this.node2 = node2;
    valueDisplay = false;
  }

  public void draw(Graphics g)
  {
    g.setColor(Color.BLUE);
    g.drawLine(this.node1.getLocation().x, this.node1.getLocation().y, 
      this.node2.getLocation().x, this.node2.getLocation().y);
    if(valueDisplay){
    	if(value<0){
    		g.setColor(Color.RED);
    	}
    	else if(value==0){
    		g.setColor(Color.GREEN);
    	}
	    g.drawString(""+(int)(value*100)/100.0, (node1.getLocation().x+node2.getLocation().x)/2+4, 
	    		 (node1.getLocation().y+node2.getLocation().y)/2+4);
    }
  }

  public Node getNode1() {
    return this.node1;
  }

  public Node getNode2() {
    return this.node2;
  }
  
  public void setValue(double value){
	  this.value = value;
  }
  
  public double getValue(){
	  return value;
  }
  
  public void setValueDisplay(boolean trueorfalse){
	  valueDisplay = trueorfalse;
  }
  
  public double getSin(){
	  return Math.abs(node1.getLocation().y-node2.getLocation().y)/
			  Math.sqrt((node1.getLocation().y-node2.getLocation().y)*(node1.getLocation().y-node2.getLocation().y)
					  +(node1.getLocation().x-node2.getLocation().x)*(node1.getLocation().x-node2.getLocation().x));
  }
  
  public double getCos(){
	  return Math.abs(node1.getLocation().x-node2.getLocation().x)/
			  Math.sqrt((node1.getLocation().y-node2.getLocation().y)*(node1.getLocation().y-node2.getLocation().y)+
					  (node1.getLocation().x-node2.getLocation().x)*(node1.getLocation().x-node2.getLocation().x));
  }

  public boolean equals(Object truss) {
    if (truss == null) return false;
    if (this == truss) return true;
    if (!(truss instanceof Truss)) return false;
    Truss temp = (Truss)truss;
    if (((temp.node1.equals(this.node1)) && (temp.node2.equals(this.node2))) || (
      (temp.node1.equals(this.node2)) && (temp.node2.equals(this.node1))))
      return true;
    return false;
  }
}