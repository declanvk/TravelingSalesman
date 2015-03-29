import java.awt.Point;

/**
 * Nodes in a singly linked list.
 *
 * @version 1.0
 * @since 01/12/00
 */
public class City {

	private final Point location;

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

	@Override
	public boolean equals(Object obj) {
		City c = (City) obj;
		return c.location.equals(this.location);
	}

}
