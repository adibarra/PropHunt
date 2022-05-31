import java.awt.Rectangle;

//Alec Ibarra
public class Connector {
	
	private Rectangle area = new Rectangle();
	private int room1;
	private int room2;
	
	public Connector(Rectangle area, int room1, int room2)
	{
		this.area = area;
		this.room1 = room1;
		this.room2 = room2;
	}

	public Rectangle getArea() {
		return area;
	}

	public void setArea(Rectangle area) {
		this.area = area;
	}

	public int getRoom1() {
		return room1;
	}

	public void setRoom1(int room1) {
		this.room1 = room1;
	}

	public int getRoom2() {
		return room2;
	}

	public void setRoom2(int room2) {
		this.room2 = room2;
	}
	
	

}
