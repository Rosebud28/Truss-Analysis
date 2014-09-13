import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Node
  implements Drawable
{
  private int DIAMETER;
  private Point point;
  private Color color;
  private ArrayList<Truss> trussArray;
  private Load load;
  private FixNode fixNode;
  private Roller roller;

  public Node(Point point, int diameter)
  {
    this.DIAMETER = diameter;
    this.point = point;
    this.color = Color.BLACK;
    this.trussArray = new ArrayList<Truss>();
    this.load = null;
    fixNode=null;
    roller=null;
  }
  
  public Node(Point point, Color color){
	  this.DIAMETER = 8;
	  this.point = point;
	  this.color = color;
	  this.trussArray = new ArrayList<Truss>();
  }

  public Node(Point point) {
    this.DIAMETER = 8;
    this.point = point;
    this.color = Color.BLACK;
    this.trussArray = new ArrayList<Truss>();
  }

  public void draw(Graphics g)
  {
    g.setColor(this.color);
    g.fillOval(this.point.x - this.DIAMETER / 2, this.point.y - this.DIAMETER / 2, this.DIAMETER, this.DIAMETER);
    if (load != null) {
		load.draw(g);
	}
    if(fixNode!=null){
    	fixNode.draw(g);
    }
    if(roller!=null){
    	roller.draw(g);
    }
  }

  public void changeLocation(Point point) {
    if(load!=null){
    	this.load.setPointOfAction(point);
    	int dx = point.x-this.point.x;
    	int dy = point.y-this.point.y;
    	load.setEndPoint(new Point(load.getEndPoint().x+dx,load.getEndPoint().y+dy));
    }
    this.point = point;
  }

  public Point getLocation() {
    return this.point;
  }

  public boolean contains(Point p) {
    return Math.sqrt((p.getX() - this.point.x) * (p.getX() - this.point.x) + 
      (p.getY() - this.point.y) * (p.getY() - this.point.y)) <= this.DIAMETER / 2;
  }

  public void addTruss(Truss truss) {
    this.trussArray.add(truss);
  }

  public void removeTruss(Truss truss) {
    for (int i = 0; i < this.trussArray.size(); i++)
      if (truss.equals(this.trussArray.get(i)))
        this.trussArray.remove(i);
  }
  
  public boolean addFixNode(FixNode fixNode){
	  if(this.fixNode==null) {
		  this.fixNode= fixNode;
		  roller=null;
		  return true;
	  }
	  return false;
  }
  
  public void removeFixNode(){
	  fixNode = null;
  }
  
  public FixNode getFixNode(){
	  return fixNode;
  }
  
  public Roller getRoller(){
	  return roller;
  }
  
  public boolean addRoller(Roller roller){
	  if(this.roller==null) {
		  this.roller= roller;
		  fixNode = null;
		  return true;
	  }
	  return false;
  }
  
  public void removeRoller(){
	  roller=null;
  }

  public ArrayList<Truss> getTrussArray()
  {
    return this.trussArray;
  }

  public void setLoad(Load load) {
    this.load = load;
  }

  public Load getLoad() {
    return this.load;
  }

  public void removeLoad() {
    this.load = null;
  }

  public Node getTheOtherNode(Truss truss) {
    if (truss.getNode1().equals(this)) {
      return truss.getNode2();
    }

    return truss.getNode1();
  }

  public void setColor(Color color) {
    this.color = color;
  }
  

  public boolean equals(Object o) {
    if (this == o) return true;
    if ((o instanceof Node)) {
      if (((Node)o).getLocation().equals(this.point)) {
        return true;
      }

      return false;
    }

    return false;
  }


}