import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/*
 * A Collection of Chromosomes together with means of 
 *    ranking
 *    breeding
 *    mutating
 *    
 * Also controls the use of
 *    mutation
 *    elitism
 *    
 */
class Generation {

	private static final int MUTATION_CHANCE = 100;
	private static final int MUTATION_NUM = 1;
	private static final float MUTATION_PERCENT = (float) 0.07;
	public static int POP_SIZE; // # of chromosomes in a generation
	public static boolean MUTATION, ELITISM;
	public Chromosome[] chromosomes;
	public Chromosome firstRoute;
	public Chromosome bestRoute;
	private Random rGen = new Random();
	private boolean DEBUG = false;
	public int number = 0;

	public Generation() {
		chromosomes = new Chromosome[POP_SIZE];
		for (int i = 0; i < POP_SIZE; i++)
			chromosomes[i] = new Chromosome();
		bestRoute = null;
		firstRoute = null;
	}

	public Generation(Chromosome ch) {
		chromosomes = new Chromosome[POP_SIZE];
		chromosomes[0] = ch;
		bestRoute = ch;
		firstRoute = ch;
		for (int i = 1; i < POP_SIZE; i++) {
			chromosomes[i] = ch.duplicate().scramble();
			if (DEBUG)
				System.out.println("gen " + chromosomes[i]);
		}

		if (DEBUG)
			System.out.println(this);
		if (DEBUG)
			System.out.println(bestRoute.fitness());
	}

	public void nextGen() {
		number++;
		rank();

		select();

		breed();

		mutate();

		rank();

		bestRoute = chromosomes[0];
		if (DEBUG)
			System.out.println(this);

	}

	public double getFitness() {
		if (chromosomes[0] == null)
			return 0;
		else {
			rank();
			return chromosomes[0].fitness();
		}

	}

	private void rank() {
		Arrays.sort(chromosomes);
	}

	public void breed() {
		// TODO clever code needed here
	}

	public void mutate() {
		if (rGen.nextInt(MUTATION_CHANCE) < 10)
			for (int i = 0; i < MUTATION_NUM; i++)
				chromosomes[rGen.nextInt(chromosomes.length)].mutate(MUTATION_PERCENT);
	}

	public void select() {
		// TODO clever code needed here
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < chromosomes.length; i++)
			s += chromosomes[i].fitness() + "\n ";
		return s;
	}
}