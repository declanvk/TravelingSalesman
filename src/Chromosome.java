import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome> {

	// these can be set externally
	public static int POP_SIZE;
	public static boolean MUTATION, ELITISM;

	private final City[] cities;
	private final Random rGen = new Random();
	private final double pathLength;

	public Chromosome(City[] t) {
		this.cities = t;
		this.pathLength = calculateFitness();
	}

	public City getCity(int i) {
		return cities[i];
	}

	public int size() {
		return cities.length;
	}

	public Chromosome scramble() {
		City[] temp = cities.clone();
		fisherYatesShuffle(temp);
		return new Chromosome(temp);
	}

	@Override
	public int compareTo(Chromosome o) {
		if (o.fitness() == this.fitness())
			return 0;
		else
			return o.fitness() > this.fitness() ? -1 : 1;
	}

	private void fillSets(HashMap<City, HashSet<City>> a, Chromosome c) {
		HashSet<City> temp = null;
		City o = null;
		for (int i = 0; i < c.cities.length; i++) {
			o = c.cities[i];
			temp = a.get(o);
			if (temp == null) {
				a.put(o, new HashSet<City>());
				temp = a.get(o);
			}

			temp.add(c.cities[wrap(i + 1, c.cities.length)]);
			temp.add(c.cities[wrap(i - 1, c.cities.length)]);
		}
	}

	public Chromosome breed(Chromosome other) {
		if (this.cities.length != other.cities.length)
			return null;

		HashMap<City, HashSet<City>> a = new HashMap<>();
		fillSets(a, other);
		fillSets(a, this);

		City chosen = null;
		LinkedHashSet<City> k = new LinkedHashSet<City>();
		City n = rGen.nextBoolean() ? this.cities[0] : other.cities[0];
		while (k.size() < other.cities.length) {
			k.add(n);
			chosen = null;
			for (Entry<City, HashSet<City>> o : a.entrySet())
				if (o.getValue().contains(n)) {
					o.getValue().remove(n);
					if (chosen == null)
						chosen = o.getKey();
					else if (a.get(chosen).size() == o.getValue().size())
						chosen = rGen.nextBoolean() ? chosen : o.getKey();
					else if (a.get(chosen).size() > o.getValue().size())
						chosen = o.getKey();
				}

			if (chosen == null)
				for (Entry<City, HashSet<City>> h : a.entrySet())
					if (k.contains(h.getKey()))
						continue;
					else {
						chosen = h.getKey();
						break;
					}

			n = chosen;
		}

		return new Chromosome(k.toArray(new City[other.cities.length]));
	}

	public double fitness() {
		return pathLength;
	}

	private int wrap(int ind, int len) {
		return (((ind) % len) + len) % len;
	}

	private double calculateFitness() {
		double totalDistance = 0;
		for (int i = 0; i < cities.length; i++)
			totalDistance += cities[wrap(i + 1, cities.length)]
					.distanceTo(cities[i]);

		return totalDistance;
	}

	public String toString() {
		return new Double(pathLength).toString();
	}

	private <T> void fisherYatesShuffle(T[] t) {
		for (int i = 0; i < t.length; i++) {
			int j = rGen.nextInt(t.length - i);

			// swap
			T temp = t[i];
			t[i] = t[j];
			t[j] = temp;
		}
	}

	public Chromosome mutate() {
		City[] temp = cities.clone();
		int a = rGen.nextInt(cities.length);
		int b = 0;
		do {
			b = rGen.nextInt(cities.length);
		} while (b == a);
		
		//Swap
		City t = temp[a];
		temp[a] = temp[b];
		temp[b] = t;

		return new Chromosome(temp);
	}
}
