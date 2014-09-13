import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class FixNode implements Drawable {
	private Node location;
	private double direction,value,value2;
	private Matrix points;
	private Load load1,load2;
	
	public FixNode(Node location,double direction){
		this.location = location;
		this.direction = direction;
		load1 = new Load(location.getLocation(),new Point(0,0));
		load2 = new Load(location.getLocation(),new Point(0,0));
		points = new Matrix(2,8);
		points.setElement(-10, 0, 0);
		points.setElement(-4, 1, 0);
		points.setElement(10, 0, 1);
		points.setElement(-4, 1, 1);
		points.setElement(-4, 0, 2);
		points.setElement(-4, 1, 2);
		points.setElement(-4, 0, 3);
		points.setElement(-8, 1, 3);
		points.setElement(0, 0, 4);
		points.setElement(-4, 1, 4);
		points.setElement(0, 0, 5);
		points.setElement(-8, 1, 5);
		points.setElement(4, 0, 6);
		points.setElement(-4, 1, 6);
		points.setElement(4, 0, 7);
		points.setElement(-8, 1, 7);
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
	
	public double getSin(){
		return Math.sin(direction+Math.PI/2);
	}
	
	public double getCos(){
		return Math.cos(direction+Math.PI/2);
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if(o==null)
			return false;
	    if ((o instanceof FixNode)) {
	      if (((FixNode)o).getLocation().equals(this.getLocation())) {
	        return true;
	      }

	      return false;
	    }

	    return false;
	 }
	
	public void setValue1(double value){
		this.value = value;
	}
	
	public void setValue2(double value){
		this.value2 = value;
	}
	
	public double getValue(double value){
		return value;
	}
	
	public double getValue2(double value){
		return value2;
	}
	
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.DARK_GRAY);
		Matrix newMatrix = points;
		g2d.rotate(-direction,location.getLocation().x,location.getLocation().y);
		for(int i=0;i<8;i+=2){
			g2d.drawLine((int)newMatrix.getElement(0, i)+location.getLocation().x, -(int)newMatrix.getElement(1, i)+location.getLocation().y, 
					(int)newMatrix.getElement(0, i+1)+location.getLocation().x, -(int)newMatrix.getElement(1, i+1)+location.getLocation().y);
		}
		g2d.rotate(direction,location.getLocation().x,location.getLocation().y);
		if(value!=0.0){
			load1.setPointOfAction(location.getLocation());
			if(value>0){
				load1.setForceDirection(0);
				load1.setForceMagnitude(value);
			}
			else{
				load1.setForceDirection(180);
				load1.setForceMagnitude(-value);
			}
			load1.draw(g2d);
		}
		if(value2!=0.0){
			load2.setPointOfAction(location.getLocation());
			if(value2>0){
				load2.setForceDirection(90);
				load2.setForceMagnitude(value2);
			}
			else{
				load2.setForceDirection(-90);
				load2.setForceMagnitude(-value2);
			}
			load2.draw(g2d);
		}
	}
}
