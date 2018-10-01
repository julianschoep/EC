import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;

public class player25 implements ContestSubmission
{
	private int nParticles = 100;
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
		// Run your algorithm here
        int evals = 0;

        // init population
        Swarm swarm = new Swarm(nParticles, nDimensions,  evaluation_, rnd_);

        // Calculate and save fitness per particle
        while(evals < evaluations_limit_) {
        	//System.out.println(evals);
			//System.out.println(evaluations_limit_);
			// update particle's velocity
			swarm.iterate();
			double fitness = swarm.getGbestFitness();
			bestGlobalPosition = swarm.getGbestPosition();

			//double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
			// Check fitness of unknown fuction
			//double fitness = (double) evaluation_.evaluate(child);

			//double fitness = (double) evaluation_.evaluate(bestGlobalPosition);
			//System.out.println(fitness);
			evals++;
		}

        /**
		Initialization
		1. initialize particle xi(0) from swarm (best drawn from uniform distribution with constraints/limits [-5, 5])
		2. particles best position = particles current (= starting) position
		3. Calculate fitness of each particle and if fj(x) > fi(x), initialize fj(x) as best global position as g = xj(0)

		Until stopping criterion is met, do following:
		1. Update the particle's' velocity according to: v(t+1) = intertia(0.7)*v(t) + c1(p - x(t)) * R1 + c2(g - x(t)) * R2 (usually, c1=c2=2)
			On the other hand, R1 and R2 are two dxd diagonal matrices of
			random numbers generated from a uniform distribution in [0,1],
			so that both the social and the cognitive components have a stochastic
			influence on the velocity update rule

			Note that matrices R1t and R2t are generated at each iteration for each particle independently

		2. Update the particle's' position according to: x(t+1) = x(t) + v(t+1)
		3. Evaluate fitness of particle f(xi(t+1))
		4. if f(xi(t+1)) > f(xi(t)): update personal best position
		5. if f(xi(t+1)) > g: update global best position

		At the end, best position is represented by g!





		each particle is 10-dimensional array with real-values, randomly initialized
		fitness calculation: put values of particle (10 values of array) into function and get number

		update particle position based on formula: x(t+1) = x(t) + v(t+1)

        */
		/**
		 * the first one, defined the inertia or momentum prevents the particle
		 * from drastically changing direction, by keeping track of the previous
		 * flow direction; the second term, called the cognitive com- ponent, accounts
		 * for the tendency of particles to return to their own pre- viously found best positions;
		 * the last one, named the social component, identifies the propensity of a particle to
		 * move towards the best position of the whole swarm (or of a local neighborhood of the
		 * particle, depend- ing on whether a global or partial PSO is implemented). Based on these
		 * considerations, the velocity of the ith particle is defined as:
		 *
		 * v(t+1) = v(t) + c1(p - x(t)) * R1 + c2(g - x(t)) * R2
 		 */

		/**
        while(evals<evaluations_limit_){
        	//
            double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
            // Check fitness of unknown fuction
            Double fitness = (double) evaluation_.evaluate(child);
            evals++;
            // Select survivors
        }
		 */
	}
}
