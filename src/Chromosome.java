import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

	public Chromosome(ArrayList<City> or) {
		this.cities = or;
		recalculateNeighbors();
		this.pathLength = calculateFitness();
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
		City[] mapper = new City[other.cities.size()];
		for (int i = 0; i < containers.length; i++)
			containers[i] = new HashSet<City>();

		int i = 0;
		City.CLink aLink = null;
		City.CLink bLink = null;
		for (City o : other.cities) {
			aLink = o.getNeighbors(this.hashCode());
			bLink = o.getNeighbors(other.hashCode());

			containers[i].add(aLink.getNext());
			containers[i].add(aLink.getPrev());
			containers[i].add(bLink.getNext());
			containers[i].add(bLink.getPrev());

			mapper[i] = o;

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
			if (chosen == null) {
				int p = 0;
				do {
					p = rGen.nextInt(containers.length);
					chosen = containers[p];
				} while(k.contains(mapper[p]));
			}

			int j = 0;
			for (; j < containers.length; j++)
				if (containers[j] == chosen)
					break;
			n = mapper[j];
		}
		return new Chromosome(k);
	}

	public double fitness() {
		return pathLength;
	}

	private void recalculateNeighbors() {
		City temp = null;

		for (int i = 0; i < cities.size(); i++) {
			temp = cities.get(i);
			temp.setNeighbors(this.hashCode(), cities.get(((((i - 1) % cities
					.size()) + cities.size()) % cities.size())), cities
					.get(((((i + 1) % cities.size()) + cities.size()) % cities
							.size())));
		}
	}

	private double calculateFitness() {
		double totalDistance = 0;
		for (int i = 0; i < cities.size(); i++)
			totalDistance += cities.get(i).getNeighbors(this.hashCode())
					.getDistNext();

		return totalDistance;
	}

	public String toString() {
		return new Double(pathLength).toString();
	}

	public Chromosome mutate() {
		ArrayList<City> temp = (ArrayList<City>) this.cities.clone();
		Collections.shuffle(temp);
		return new Chromosome(temp);
	}

	public void clearCities() {
		for (City c : cities)
			c.removeNeighbor(this.hashCode());
	}
}
