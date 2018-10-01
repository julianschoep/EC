import java.util.Random;

public class Velocity{

    private int nDimensions;
    private double c1 = 2.0;
    private double c2 = 2.0;
    private double inertia = 0.7;
    private double[] velocity;
    private Random rnd;


    public Velocity(int nDimensions, Random rnd){
        this.nDimensions = nDimensions;
        this.rnd = rnd;
        this.velocity = double[this.nDimensions];

        // initialize velocity double[] with small random values
        for(int i = 0; i < this.nDimensions; i++){
            double smallRandomValue = (rnd.nextDouble() + 0.1d) / 10;
            this.velocity[i] = smallRandomValue;
        }
    }


    public void updateVelocity(Position globalBestPosition, Position particleBestPosition, double[] currentPosition){
        // Formula: inertia * v(t) + c1 * (p - x(t)) * R1 + c2 * (g - x(t)) * R2
        // constants: inertia = 0.7, c1 = c2 = 2
        // R1 = dxd diagonal matrix with random numbers from normal distribution [0,1]
        // R2 = dxd diagonal matrix with random numbers from normal distribution [0,1]
        //  p = personal best position
        //  g = global best position

        double[] gBestPosition = globalBestPosition.getCoordinates();
        double[] pBestPosition = particleBestPosition.getCoordinates();
        double[] newVelocity = new double[this.nDimensions];

        for (int i = 0; i < this.nDimensions; i++){
            private double r1 = rnd.nextDouble();
            private double r2 = rnd.nextDouble();
            newVelocity[i] = (inertia * this.velocity[i]) + (c1 * (pBestPosition[i] - currentPosition[i]) * r1) + (c2 * (gBestPosition[i] - currentPosition[i]) * r2);
        }
        this.velocity = newVelocity;
    }

    public double[] getVelocity(){
        return this.velocity;
    }

    public void setVelocity(double[] velocity){
        this.velocity = velocity;
    }

    public void setVelocityAt(int i, double vi){
        this.velocity[i] = vi;
    }

    public double getVelocityAt(int i){
        return this.velocity[i];
    }


}