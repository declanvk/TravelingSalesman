import java.awt.Point;

/**
 * Wrapper around a awt.Point
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

	public String toString() {
		return "(" + location.x + "," + location.y + ")";
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

	public double distanceTo(City c) {
		return location.distance(c.location);
	}
}
