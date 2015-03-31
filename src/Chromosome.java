import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome> {

	// these can be set externally
	public static int POP_SIZE;
	public static boolean MUTATION, ELITISM;

	// the genes
	private final ArrayList<City> cities;

	private final Random rGen = new Random();
	private double pathLength;

	private final int key;

	public Chromosome(ArrayList<City> or) {
		this.cities = or;
		recalculateNeighbors();
		this.pathLength = calculateFitness();
		this.key = this.hashCode();
		System.out.println(key);
		System.out.println(this.hashCode());
	}

	public City getCity(int i) {
		return cities.get(i);
	}

	public int size() {
		return cities.size();
	}

	/*
	 * forms a random arrangement of City objects for a new Chromosome
	 */
	public Chromosome scramble() {
		ArrayList<City> myGenes = new ArrayList<City>();

		for (int i = 0; i < cities.size(); i++) {
			myGenes.add(cities.get(i));
		}

		for (int i = 0; i < 50; i++) {
			// swap two genes at random
			int geneA = rGen.nextInt(cities.size());
			int geneB = rGen.nextInt(cities.size());
			City tempNode = myGenes.get(geneA);
			myGenes.set(geneA, myGenes.get(geneB));
			myGenes.set(geneB, tempNode);
		}
		// TODO
		return new Chromosome(myGenes);
	}

	@Override
	public int compareTo(Chromosome o) {
		if (o.fitness() == this.fitness())
			return 0;
		else
			return o.fitness() > this.fitness() ? -1 : 1;
	}

	public Chromosome breed(Chromosome other) {
		if (this.cities.size() != other.cities.size())
			return null;

		HashSet<City>[] containers = new HashSet[other.cities.size()];

		int i = 0;
		City.CLink aLink = null;
		City.CLink bLink = null;
		for (City o : other.cities) {
			aLink = o.getNeighbors(this.key);
			bLink = o.getNeighbors(other.key);

			containers[i].add(aLink.getNext());
			containers[i].add(aLink.getPrev());
			containers[i].add(bLink.getNext());
			containers[i].add(bLink.getPrev());

			i++;
		}

		HashSet<City> chosen = null;
		ArrayList<City> k = new ArrayList<>();
		City n = rGen.nextBoolean() ? this.cities.get(0) : other.cities.get(0);
		while (k.size() < other.cities.size()) {
			k.add(n);
			chosen = null;
			for (HashSet<City> o : containers)
				if (o.contains(n)) {
					o.remove(n);
					if (chosen == null)
						chosen = o;
					else if (chosen.size() == o.size())
						chosen = rGen.nextBoolean() ? chosen : o;
					else if (chosen.size() > o.size())
						chosen = o;
				}
			int p = 0;
			do {
				n = (City) chosen.toArray()[p];
			} while (k.contains(n));
		}
		return new Chromosome(k);
	}

	public double fitness() {
		return pathLength;
	}

	private void recalculateNeighbors() {
		City temp = null;
		
		System.out.println(this.hashCode());
		System.out.println("Re: " + key);
		for (int i = 0; i < cities.size(); i++) {
			temp = cities.get(i);
			temp.setNeighbors(
					key,
					cities.get(((((i - 1) % cities.size()) + cities.size()) % cities
							.size())),
					cities.get(((((i + 1) % cities.size()) + cities.size()) % cities
							.size())));
		}
	}

	private double calculateFitness() {
		double totalDistance = 0;
		for (int i = 0; i < cities.size(); i++)
			totalDistance += cities.get(i).getNeighbors(key).getDistNext();

		return totalDistance;
	}

	public String toString() {
		return new Double(pathLength).toString();
	}

	public void mutate(float mutationPercent) {
		int numToMutate = Math.round(mutationPercent * cities.size());
		int a = 0;
		int b = 0;
		for (int i = 0; i < numToMutate; i++) {
			do {
				a = rGen.nextInt(cities.size());
				b = rGen.nextInt(cities.size());
			} while (a != b);
			City temp = cities.get(b);
			cities.set(b, cities.get(a));
			cities.set(a, temp);
		}
		
		recalculateNeighbors();
	}

	public void clearCities() {
		for (City c : cities)
			c.removeNeighbor(key);
	}
}
