import java.util.Random;

public class Position {
    private double[] coordinates;
    private int nDimensions;
    private Velocity velocity;
    private Random rnd;


    public Position(int nDimensions, Random rnd){
        this.nDimensions = nDimensions;
        this.rnd = rnd;
        this.coordinates = new double[this.nDimensions];

        // initialize coordinates with random values between -5 and 5
        for (int i = 0; i < this.nDimensions; i++){
            double randomCoord = (-5.0d) + (5.0d - (-5.0d)) * this.rnd.nextDouble();
            this.coordinates[i] = randomCoord;
        }
        // initialize velocity
        this.velocity = new Velocity(this.nDimensions, this.rnd);
    }

    public double[] getCoordinates(){
        return this.coordinates;
    }

    public double getCoordAt(int i){
        return this.coordinates[i];
    }

    public void setCoordAt(int i, double coord){
        this.coordinates[i] = coord;
    }

    public void updateCoordinates(Position gBestPosition, Position particleBestPosition){
        // update velocities
        this.velocity.updateVelocity(gBestPosition, particleBestPosition, this.coordinates);
        double[] velocities = this.velocity.getVelocity();
        double[] newCoordinates = new double[nDimensions];

        // update positions
        for(int i = 0; i < this.nDimensions; i++){
            newCoordinates[i] = this.coordinates[i] + velocities[i];
        }
        this.coordinates = newCoordinates;
    }

}