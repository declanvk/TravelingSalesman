import java.awt.Point;

/**
 * Nodes in a singly linked list.
 *
 * @version 1.0
 * @since 01/12/00
 */
public class City {

	private final Point location;
	private City next; // next in list

	public City() {
		this(0, 0);
	}

	public City(int x, int y) {
		this.location = new Point(x, y);
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

	public City getNext() {
		return next;
	}

	public void setNext(City next) {
		this.next = next;
	}

	public void setyLoc(int y) {
		location.move(location.x, y);
	}

	public void setxLoc(int x) {
		location.move(x, location.y);
	}
}
