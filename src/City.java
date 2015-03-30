import java.awt.Point;

/**
 * Nodes in a singly linked list.
 *
 * @version 1.0
 * @since 01/12/00
 */
public class City {

	private final Point location;
	
	private City prev;
	private City next;

	public City() {
		this(0, 0, null, null);
	}

	public City(int x, int y, City p, City n) {
		this.location = new Point(x, y);
		this.prev = p;
		this.next = n;
	}

	public double distanceTo(City c) {
		return location.distance(c.location);
	}

	public String toString() {
		return location.toString();
	}

	public int getxLoc() {
		return location.x;
	}

	public int getyLoc() {
		return location.y;
	}
	
	public City getPrev() {
		return prev;
	}
	
	public void setPrev(City p) {
		this.prev = p;
	}
	
	public City getNext() {
		return prev;
	}
	
	public void setNext(City n) {
		this.next = n;
	}

	@Override
	public boolean equals(Object obj) {
		City c = (City) obj;
		return c.location.equals(this.location);
	}

}
