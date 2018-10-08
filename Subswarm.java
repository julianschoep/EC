import org.vu.contest.ContestEvaluation;
import java.util.Random;

public class Subswarm {
    private Particle[] swarm;
    private double[] gbestPosition;
    private double gbestFitness;
    private ContestEvaluation evaluation;
    private Random rnd;
    private double[] centroid;
    private int d;


    public Subswarm(int d, ContestEvaluation evaluation, Random rnd){
        this.gbestFitness = -Double.MAX_VALUE;
        this.evaluation = evaluation;
        this.rnd = rnd;
        this.d = d;
    }

    public Subswarm(Particle[] particles,ContestEvaluation evaluation, Random rnd){
        this.swarm = particles;
        this.gbestFitness = -Double.MAX_VALUE;
        this.evaluation = evaluation;
        this.rnd = rnd;
    }


    public void calculateCentroid(){
        // calculate the centroid based on the particles in the swarm
        for(int dim = 0; dim < this.d; dim ++){
            double sum = 0;
            for(int i = 0; i < swarm.length; i++){
                sum += swarm[i].getPositionAt(dim);
            }
            this.centroid[dim] = sum / swarm.length;
        }
    }

    public void removeAllParticles(){
        // needed when reassigning particles
        this.swarm = new Particle[];
    }

    public double[] getCentroid(){
        return this.centroid;
    }

    public void addParticle(Particle newParticle){
        old_n = swarm.length;
        Particle[] updatedSwarm = new Particle[old_n+1];
        for(int i = 0; i < old_n;i++){
            updatedSwarm[i] = this.swarm[i];
        }
        updatedSwarm[old_n] = newParticle;
        this.swarm = updatedSwarm;
    }

    public void removeParticle(Particle particle){
        old_n = swarm.length;
        Particle[] updatedSwarm = new Particle[old_n-1];
        for(int i = 0; i < old_n;i++){
            if(this.swarm[i] != particle){
                updatedSwarm[i] = this.swarm[i];
            }
        }
        this.swarm = updatedSwarm;
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