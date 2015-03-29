import java.util.ArrayList;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome> {

	// these can be set externally
	public static int POP_SIZE;
	public static boolean MUTATION, ELITISM;

	// the genes
	private ArrayList<City> cities = new ArrayList<City>();

	private boolean DEBUG = false;

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
		this.pathLength = calculateFitness();
	}

	public void addCity(int x, int y) {
		City c = new City(x, y);
		cities.add(c);
		pathLength = calculateFitness();
	}

	public City getCity(int i) {
		return cities.get(i);
	}

	int size() {
		if (cities != null)
			return cities.size();
		else
			return 0;
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
	
	public Chromosome breed(Chromosome c) {
		Chromosome child = new Chromosome();
		
		return null;
	}

	public double fitness() {
		return pathLength;
	}

	private double calculateFitness() {
		double totalDistance = 0;
		for (int i = 0; i < cities.size(); i++)
			totalDistance += cities.get(i).distanceTo(
					cities.get((i + 1) % cities.size()));
		return totalDistance;
	}

	void append(int x, int y) {
		City c = new City(x, y);
		cities.add(c);
	}

	public String toString() {
		return new Double(pathLength).toString();
	}
}
