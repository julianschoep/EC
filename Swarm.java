import org.vu.contest.ContestEvaluation;
import java.util.Random;

public class Swarm {
    private Particle[] swarm;
    private Position gbestPosition;
    private double gbestFitness;
    private int pSize;
    private ContestEvaluation evaluation;
    private Random rnd;


    public Swarm(int pSize, int nDimensions, ContestEvaluation evaluation, Random rnd){
        // Initialize the swarm
        this.swarm = Particle[pSize];
        this.pSize = pSize;
        this.gbestFitness = -1;
        this.evaluation = evaluation;
        this.rnd = rnd;

        for(int i = 0; i < pSize; i++){
            this.swarm[i] = new Particle(nDimensions, this.evaluation, this.rnd); // randomly init N particles
        }

        // set initial global best position and fitness
        for(int i = 0; i < pSize; i++){
            double particleFitness = this.swarm[i].getFitness();

            if(particleFitness > this.gbestFitness){
                this.gbestFitness = particleFitness;
                this.gbestPosition = this.swarm[i].getPosition();
            }
        }
    }

    public Position iterate() { // not very pretty...
        // update particle positions
        for (int i = 0; i < pSize; i++) {
            this.swarm[i].updatePosition(this.gbestPosition);
        }

        // select new global best
        for (int i = 0; i < pSize; i++) {
            double particleFitness = this.swarm[i].getFitness();

            if (particleFitness > this.gbestFitness) {
                this.gbestFitness = particleFitness;
                this.gbestPosition = this.swarm[i].getPosition();
            }
        }
    }

    public Position getGbestPosition() {
        return this.gbestPosition;
    }

    public double getGbestFitness() {
        return this.gbestFitness;
    }



    /**
    // update particle positions
    Position[psize] currentPositions
    for(int i = 0; i < this.pSize; i++){
        currentPositions[i] = this.swarm[i].update(this.gbest)
    }

    // determine new gbest
    for(int i = 0; i < this.pSize; i++){
        particlePosition = currentPositions[i].getPosition();
        fitness = this.evaluation(particlePosition.getCoordinates())
        if fitness > this.gbestFitness{
            this.gbestFitness = fitness;
            this.gbest = currentPositions[i];
        }
    }
    return this.gbest;
    */

}