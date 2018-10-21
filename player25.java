import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;

public class player25 implements ContestSubmission
{
	private int nParticles = 500;
	private int c = Integer.parseInt(System.getProperty("var"));
    private int nDimensions = 10;
    private double[] bestGlobalPosition;
    private double bestGlobalFitness;
	private int evaluations_limit_;

	Random rnd_; // NOTE so I think we have to pass this to the swarm init and particle init;
				 // as we are supposed to be able to set seeds for a number of evaluations.
				 // - I think you're right so I gave the rnd_ as an input to the other classes (swarm, particle, velocity)

	ContestEvaluation evaluation_;


	public player25()
	{
		rnd_ = new Random();
	}

	public void setSeed(long seed)
	{
		// Set seed of algortihms' random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;
		
		// Get evaluation properties
		Properties props = evaluation.getProperties();

		// Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		// Property keys depend on specific evaluation
		// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Do sth with property values, e.g. specify relevant settings of your algorithm
        if(isMultimodal){
            // Do sth
        }else{
            // Do sth else
        }
    }
    
	public void run()
	{
		// Run your algorithm heres
        int evals = 0;
		System.out.print("Number of particles: ");
		System.out.println(this.nParticles);
        // init population
        Population population = new Population(nParticles, nDimensions,  evaluation_, rnd_);
        
        // Calculate and save fitness per particle
        //while(evals < evaluations_limit_) {
        while(evals < evaluations_limit_) {

			if((evals % this.c == 0)){ // every C steps do niching operation
                //System.out.println("___________________________________");
				//System.out.println("IDENTIFYING NICHES");
                if(this.c > 0){
				    population.identifyNiches();
                }else{
                    System.out.println("so no niching");
                }
			}
			population.updateGlobalFitness();
			//population.iterate();
        	//System.out.println(evals);
			//System.out.println(evaluations_limit_);
			// update particle's velocity
			population.iterate(); // updates positions
			double fitness = population.getGbestFitness();
			//System.out.println(String.format("Best fitness: %f",fitness));
			bestGlobalPosition = population.getGbestPosition();
			System.out.println(" ");
			System.out.print("Iteration ");
			System.out.print(evals);
			System.out.print(" ");
			System.out.println(fitness);
			//System.out.println(" ");
			if(false){
				for(int i = 0; i < population.population.length; i++){
					String line = String.format("Particle nr. %d: ",
							population.population[i].name);
					System.out.print(line);
					System.out.println(population.population[i].fitness);
				}
			}

			//double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
			// Check fitness of unknown fuction
			//double fitness = (double) evaluation_.evaluate(child);

			//double fitness = (double) evaluation_.evaluate(bestGlobalPosition);
			//System.out.println(fitness);
			evals++;
			//System.out.println(evals);
		}

	}
}
