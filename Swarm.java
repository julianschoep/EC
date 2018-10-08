import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.Random;

public class Swarm {
    private Particle[] swarm;
    private double[] gbestPosition;
    private double gbestFitness;
    private int pSize;
    private ContestEvaluation evaluation;
    private Random rnd;


    public Swarm(int pSize, int nDimensions, ContestEvaluation evaluation, Random rnd){
        // Initialize the swarm
        this.swarm = new Particle[pSize];
        this.pSize = pSize;
        this.gbestFitness = -Double.MAX_VALUE;
        this.evaluation = evaluation;
        this.rnd = rnd;

        for(int i = 0; i < pSize; i++){
            this.swarm[i] = new Particle(nDimensions, this.evaluation, this.rnd); // randomly init N particles
        }
        this.updateGlobalFitness();
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

}