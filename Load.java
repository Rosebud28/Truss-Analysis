import java.awt.Graphics;
import java.awt.Point;

public class Load implements Drawable {
	private Point pointOfAction;
	private Point endPoint;
	private double force;

	public Load(Point node1, Point node2) {
		this.pointOfAction = node1;
		this.endPoint = node2;
		this.force = (Math.sqrt((this.pointOfAction.x - this.endPoint.x)
				* (this.pointOfAction.x - this.endPoint.x)
				+ (this.pointOfAction.y - this.endPoint.y)
				* (this.pointOfAction.y - this.endPoint.y)) / 10.0);
	}

	public void setPointOfAction(Point point) {
		this.pointOfAction = point;
	}

	public void setEndPoint(Point point) {
		this.endPoint = point;
	}

	public void setForceMagnitude(double force) {
		double ratio = force / this.force;
		int dx = (int) ((this.endPoint.x - this.pointOfAction.x) * ratio);
		int dy = (int) (-(this.endPoint.y - this.pointOfAction.y) * ratio);
		this.endPoint.setLocation(dx+pointOfAction.x, dy+pointOfAction.y);;
		this.force = force;
	}
	

	public void setForceDirection(Point cursorPoint) {
		double ratio = this.force
				* 10.0D
				/ Math.sqrt((this.pointOfAction.x - cursorPoint.x)
						* (this.pointOfAction.x - cursorPoint.x)
						+ (this.pointOfAction.y - cursorPoint.y)
						* (this.pointOfAction.y - cursorPoint.y));
		int x = (int) (this.pointOfAction.x + (this.endPoint.x - this.pointOfAction.x)
				* ratio);
		int y = (int) (this.pointOfAction.y + (this.endPoint.y - this.pointOfAction.y)
				* ratio);
		this.endPoint.setLocation(x, y);
	}

	public void setForceDirection(double degree) {
		double radian = Math.toRadians(degree);
		double sin = -Math.sin(radian);
		double cos = Math.cos(radian);
		int x = (int) (cos * this.force * 10.0 + this.pointOfAction.x);
		int y = (int) (sin * this.force * 10.0 + this.pointOfAction.y);
		this.endPoint.setLocation(x, y);
	}
	
	public double getSin(){
		return Math.sin(getForceDirection());
	}
	
	public double getCos(){
		return Math.cos(getForceDirection());
	}

	public Point getPointOfAction() {
		return this.pointOfAction;
	}

	public Point getEndPoint() {
		return this.endPoint;
	}

	public double getForce() {
		return this.force;
	}

	public double getForceDirection(){
		double dx = endPoint.x-pointOfAction.x;
		double dy = -endPoint.y+pointOfAction.y;
		if(dx==0){
			if(dy>0){
				return Math.PI/2;
			}
			else
				return -Math.PI/2;
		}
		else{
			if(dx<0){
			if(dy>0){	
				return Math.atan(dy/dx)+Math.PI;
			}
			else
				return Math.atan(dy/dx)-Math.PI;
			}
			else
				return Math.atan(dy/dx);
		}
	}
	 
	public void draw(Graphics g) {
		double dtheta = Math.PI/8;
		g.drawLine(this.pointOfAction.x, this.pointOfAction.y, this.endPoint.x,
				this.endPoint.y);
		double dx = 1.5*force*Math.cos(dtheta+getForceDirection());
		double dy = -1.5*force*Math.sin(dtheta+getForceDirection());
		g.drawLine(this.endPoint.x, this.endPoint.y,
				this.endPoint.x - (int)dx, this.endPoint.y - (int)dy);
		dx = 1.5*force*Math.cos(-dtheta+getForceDirection());
		dy = -1.5*force*Math.sin(-dtheta+getForceDirection());
		g.drawLine(this.endPoint.x, endPoint.y,
				this.endPoint.x - (int)dx, this.endPoint.y - (int)dy);
		if(endPoint.y-pointOfAction.y>0){
			g.drawString(""+((int)(force*10))/10.0,
					endPoint.x, endPoint.y+10);
		}
		else{
			g.drawString(""+((int)(force*10))/10.0,
					endPoint.x, endPoint.y);
		}
	}
}