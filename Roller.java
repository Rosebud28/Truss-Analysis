import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class Roller implements Drawable {
	Node location;
	double direction,value1,value2;
	private Load load1,load2;
	Matrix points;
	
	public Roller(Node location,double direction){
		this.location = location;
		this.direction = direction;
		load1 = new Load(location.getLocation(),new Point(0,0));
		load2 = new Load(location.getLocation(),new Point(0,0));
		points = new Matrix(2,4);
		points.setElement(-10, 0, 0);
		points.setElement(-4, 1, 0);
		points.setElement(10, 0, 1);
		points.setElement(-4, 1, 1);
		points.setElement(-4, 0, 2);
		points.setElement(-2, 1, 2);
		points.setElement(4, 0, 3);
		points.setElement(-2, 1, 3);
	}
	
	public Node getLocation(){
		return location;
	}
	
	public void setDirection(double direction){
		this.direction = Math.toRadians(direction);
	}
	
	public double getDirection(){
		return Math.toDegrees(direction);
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if(o==null)
			return false;
	    if ((o instanceof Roller)) {
	      if (((Roller)o).getLocation().equals(this.getLocation())) {
	        return true;
	      }

	      return false;
	    }

	    return false;
	}
	
	public void setValue1(double value){
		this.value1 = value;
	}
	
	public double getValue1(double value){
		return value1;
	}
	
	public void setValue2(double value){
		this.value2 = value;
	}
	
	public double getValue2(double value){
		return value2;
	}
	
	public double getSin(){
		return Math.sin(direction+Math.PI/2);
	}
	
	public double getCos(){
		return Math.cos(direction+Math.PI/2);
	}
	
	public double getTan(){
		return Math.tan(direction+Math.PI/2);
	}
	
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.DARK_GRAY);
		g2d.rotate(-direction,location.getLocation().x,location.getLocation().y);
		g.drawLine((int)points.getElement(0, 0)+location.getLocation().x, -(int)points.getElement(1, 0)+location.getLocation().y, 
				(int)points.getElement(0, 1)+location.getLocation().x, -(int)points.getElement(1, 1)+location.getLocation().y);
		g.drawOval((int)points.getElement(0, 2)+location.getLocation().x-2, -(int)points.getElement(1, 2)+location.getLocation().y+2, 4, 4);
		g.drawOval((int)points.getElement(0, 3)+location.getLocation().x-2, -(int)points.getElement(1, 3)+location.getLocation().y+2, 4, 4);
		if(value1!=0.0||value2!=0.0){
			load1.setPointOfAction(location.getLocation());
			if(value1>0) {
				load1.setForceDirection(Math.toDegrees(Math.PI/2));
				load1.setForceMagnitude(value1/getSin());
			}
			else {
				load1.setForceDirection(Math.toDegrees(-Math.PI/2));
				load1.setForceMagnitude(-value1/getSin());
			}
			load1.draw(g2d);
		}
		g2d.rotate(direction,location.getLocation().x,location.getLocation().y);
	}
}