import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome> {

	// these can be set externally
	public static int POP_SIZE;
	public static boolean MUTATION, ELITISM;

	// the genes
	private ArrayList<City> cities = new ArrayList<City>();
	private ArrayList<City> original = new ArrayList<City>(); // array for reset
																// function
	Random rGen = new Random();
	private double pathLength;

	public Chromosome() {
		cities = new ArrayList<City>();
		this.pathLength = 0;
	}

	public Chromosome(ArrayList<City> or) {
		original = or;
		cities = or;
		recalculateNeighbors();
		this.pathLength = calculateFitness();
	}

	public void addCity(int x, int y) {
		City c = new City(x, y);
		cities.add(c);
		recalculateNeighbors();
		pathLength = calculateFitness();
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
		//TODO
		return new Chromosome(myGenes);
	}

	@Override
	public int compareTo(Chromosome o) {
		if (o.fitness() == this.fitness())
			return 0;
		else
			return o.fitness() > this.fitness() ? -1 : 1;
	}

	public Chromosome duplicate() {
		Chromosome copy = new Chromosome(cities);
		return copy;
	}

	public Chromosome breed(Chromosome other) {
		City[] a = this.cities.toArray(new City[this.cities.size()]);
		City[] b = other.cities.toArray(new City[other.cities.size()]);

		if (a.length != b.length)
			return null;

		City[][] work = new City[a.length][a.length - 1];

		

		return null;
	}

	public double fitness() {
		return pathLength;
	}

	private void recalculateNeighbors() {
		City temp = null;
		for (int i = 0; i < cities.size(); i++) {
			temp = cities.get(i);
			temp.setNeighbors(this.hashCode(),
					cities.get(((((i - 1) % 2) + 2) % 2)),
					cities.get(((((i + 1) % 2) + 2) % 2)));
		}
	}

	private double calculateFitness() {
		double totalDistance = 0;
		City temp;
		for (int i = 0; i < cities.size(); i++) {
			temp = cities.get(i);
			totalDistance += temp.distanceTo(temp.getNeighbors(this.hashCode())
					.getNext());
		}
		return totalDistance;
	}

	@Override
	public boolean equals(Object obj) {
		Chromosome c = (Chromosome) obj;
		if (this.cities.size() != c.cities.size())
			return false;
		else
			for (int i = 0; i < cities.size(); i++)
				if (!this.cities.get(i).equals(c.cities.get(i)))
					return false;
		return true;
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
	}
}
