import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.Random;
import java.util.Math;

public class Population {
    private Particle[] population;
    private double[] gbestPosition;
    private double gbestFitness;
    private int pSize;
    private ContestEvaluation evaluation;
    private Random rnd;


    public Population(int pSize, int nDimensions, ContestEvaluation evaluation, Random rnd){
        // Initialize the population
        this.swarm = new Particle[pSize];
        this.pSize = pSize;
        this.gbestFitness = -Double.MAX_VALUE;
        this.evaluation = evaluation;
        this.rnd = rnd;
        this.d = nDimensions;

        for(int i = 0; i < pSize; i++){
            this.swarm[i] = new Particle(nDimensions, this.evaluation, this.rnd); // randomly init N particles
        }
        this.updateGlobalFitness();
    }

    public void doNiching(){
        // do clustering
        // for k in {2 -- this.pSize/2}
        //
        //      do 10 cluster iterations, select best on basis of error (implement error for cluster)
        // select best cluster based on BIC (implement BIC calculation)
        // do cutting procedure
        // assign  cutted particles to vonneumann topology as a seperate swarm
        //
    }

    public void iterate() { // not very pretty...

        // update particle positions
        for (int i = 0; i < this.pSize; i++) {
            this.swarm[i].updatePosition(this.gbestPosition);
        }

        // select new global best
        this.updateGlobalFitness();
    }




    public void updateGlobalFitness(){
        for (int i = 0; i < this.pSize; i++) {
            double particleFitness = this.swarm[i].getBestFitness();

            if (particleFitness > this.gbestFitness) {
                this.gbestFitness = particleFitness;
                this.gbestPosition = this.swarm[i].getBestPosition();
            }
        }
    }

    public double[] getGbestPosition() {
        return this.gbestPosition;
    }

    public double getGbestFitness() {
        return this.gbestFitness;
    }




    public double calculateEuclideanDistance(Particle x, Particle y){
        double totalDistance = 0;
        xpos = x.getPosition();
        ypos = y.getPosition();
        for(int i = 0; i < this.d; i++){
            double xi 			=  xpos(i);
            double yi 			=  ypos(i);
            double distance	 	=  Math.pow(xi - yi, 2);
            totalDistance 		+= distance;
        }
        double result = Math.sqrt(totalDistance);
        return result;
    }






}