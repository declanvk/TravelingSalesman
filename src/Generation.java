import java.util.Arrays;
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
	private static final int SELECT_NUM = 8;
	public static int POP_SIZE; // # of chromosomes in a generation
	public static boolean MUTATION, ELITISM;
	public Chromosome[] chromosomes;
	public Chromosome firstRoute;
	public Chromosome bestRoute;
	private Random rGen = new Random();
	public int number = 0;

	public Generation() {
		chromosomes = new Chromosome[POP_SIZE];
		bestRoute = null;
		firstRoute = null;
	}

	public Generation(Chromosome ch) {
		chromosomes = new Chromosome[POP_SIZE];
		chromosomes[0] = ch;
		bestRoute = ch;
		firstRoute = ch;
		for (int i = 1; i < POP_SIZE; i++)
			chromosomes[i] = ch.scramble();
	}

	public void nextGen() {
		number++;
		rank();

		breed(select());

		mutate();

		rank();

		bestRoute = chromosomes[0];
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

	public void breed(Chromosome[] par) {
		int i = 0;
		Chromosome[] temp = new Chromosome[(SELECT_NUM * SELECT_NUM) - SELECT_NUM];
		for (int x = 0; x < par.length; x++)
			for (int y = 0; y < par.length; y++)
				if (x != y)
					temp[i++] = par[x].breed(par[y]);
		Arrays.sort(temp);
		System.arraycopy(temp, 0, chromosomes, SELECT_NUM, SELECT_NUM);
	}

	public void mutate() {
		if (rGen.nextInt(MUTATION_CHANCE) < 10)
			for (int i = 0; i < MUTATION_NUM; i++) {
				int index = rGen.nextInt(chromosomes.length);
				chromosomes[index] = chromosomes[index].scramble();
			}
	}

	public Chromosome[] select() {
		Chromosome[] temp = new Chromosome[SELECT_NUM];
		System.arraycopy(chromosomes, 0, temp, 0, SELECT_NUM);
		return temp;
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < chromosomes.length; i++)
			s += chromosomes[i].fitness() + "\t";
		return s;
	}
}