import org.vu.contest.ContestEvaluation;
import java.util.Random;

public class Particle {
    private double fitness;
    private double bestFitness;
    private int nDimensions; // java way of var naming is first letter small, next word capital, no '_'
    private Position p;
    private Position bestPosition;
    private ContestEvaluation evaluation;
    private Random rnd;

    public Particle(int nDimensions, ContestEvaluation evaluation, Random rnd){
        // initialize the particle
        this.nDimensions = nDimensions;
        this.evaluation = evaluation;
        this.rnd = rnd;

        // initialze position and pbest / bestFitness
        this.p = this.bestPosition = new Position(this.nDimensions, this.rnd);
        this.fitness = this.bestFitness = (double) evaluation.evaluate(p.getCoordinates());
    }


    public void updatePosition(Position gBestPosition){
        this.p.updateCoordinates(gBestPosition, this.bestPosition);
        this.fitness = (double) evaluation.evaluate(p.getCoordinates());

        // check if position update is better than best position
        if(this.fitness > this.bestFitness){
            this.bestPosition = this.p;
        }
    }



    public Position getPosition(){
        return this.p;
    }

    public Position getBestPosition(){
        return this.bestPosition;
    }

    public double getFitness(){ return this.fitness};

    public double getBestFitness(){
        return this.bestFitness;
    }

    public void setBestFitness(double bestFitness){
        this.bestFitness = bestFitness;
    }

    public void setPosition(Position p){
        this.p = p;
    }

    public void setBestPosition(double[] bestPosition){
        this.bestPosition = bestPosition;
    }

    public void setFitness(double fitness){
        this.fitness = fitness;
    }

}