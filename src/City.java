import java.awt.Point;
import java.util.HashMap;

/**
 * Nodes in a singly linked list.
 *
 * @version 1.0
 * @since 01/12/00
 */
public class City {

	public static int COUNT_INSTANCE = 0;

	protected class CLink {
		private final City prev;
		private final City next;

		public CLink(City p, City n) {
			this.prev = p;
			this.next = n;
		}

		public City getPrev() {
			return prev;
		}

		public City getNext() {
			return next;
		}
	}

	private final Point location;

	private final HashMap<Integer, City.CLink> neighbors = new HashMap<>();

	public City() {
		this(0, 0);
	}

	public City(int x, int y) {
		this.location = new Point(x, y);
		COUNT_INSTANCE++;
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

	public void setNeighbors(int key, City p, City n) {
		neighbors.put(key, new City.CLink(p, n));
	}

	public CLink getNeighbors(int key) {
		return neighbors.get(key);
	}

	public boolean removeNeighbor(int key) {
		return neighbors.remove(key) != null;
	}

	@Override
	public boolean equals(Object obj) {
		City c = (City) obj;
		return c.location.equals(this.location);
	}

}
