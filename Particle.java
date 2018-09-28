import java.util.Random;

public class Particle {
    private Position p;
    private Velocity v;
    private double fitness;
    private double[] bestPosition;
    private static int lr; // learning rate? C1 and C2
    private int n_dimensions;

    /**
    public Particle(Position p, Velocity v, double fitness){

        this.p = p;
        this.v = v;
        this.fitness = fitness;
    }
    */

    public Velocity getVelocity(){
        return this.v;
    }

    public Position getPosition(){
        return this.p;
    }

    public double[] getBestPosition(){
        return this.bestPosition;
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

    public void updatePosition(double gbest){
        // do this4
        this.v = this.v + (lr * )
    }

    public void initialize_positions(n_dimensions){
        private double[] coordinates;
        this.n_dimensions = n_dimensions;
        Random rnd = new Random();

        for (int i = 0; n_dimensions; i++){
            randomCoord = rnd.nextDouble((5 - (-5)) + 1) - 5;
            coordinates[i] = randomCoord;
        }

        p = new Position(coordinates);
        this.setPosition(p);
        this.setBestPosition(coordinates);
    }

    public void initialize_velocity(){

    }

}