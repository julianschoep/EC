import java.util.Random;

public class Particle {
    private Position p;
    private Velocity v;
    private double fitness;
    private Position pBest;
    private double pBestFitness;
    private int nDimensions; // java way of var naming is first letter small, next word capital, no '_'

    /**
    public Particle(Position p, Velocity v, double fitness){

        this.p = p;
        this.v = v;
        this.fitness = fitness;
    }
    */
    public Particle(int nDimensions){

        this.nDimensions = nDimensions;
        double[] coordinates = new double[this.nDimensions];
        Random rnd = new Random();

        for (int i = 0; i < this.n_dimensions; i++){
            //double randomCoord = rnd.nextDouble((5.0d - (-5.0d)) + 1.0d) - 5.0d;
            double randomCoord = (-5.0d) + (5.0d - (-5.0d)) * rnd.nextDouble();
            coordinates[i] = randomCoord;
        }

        // initialze position and pbest
        this.p = this.pBest = new Position(coorindates);
    }

    public





    public Velocity getVelocity(){
        return this.v;
    }

    public Position getPosition(){
        return this.p;
    }

    public double[] getBestPosition(){
        return this.bestPosition;
    }

    public double getBestFitness(){
        return this.bestFitness
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

    public void setVelocity(Velocity v){
        this.v = v;
    }

    public void setFitness(double fitness){
        this.fitness = fitness;
    }

    public void update(Position gbest){

        // do this4
        // this.v = this.v + (lr * );
    }



    public void initialize_velocity(){

    }



}