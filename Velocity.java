import java.util.Random;

public class Velocity{

    private int n_dimensions = 10;
    private double c1 = 2.0;
    private double c2 = 2.0;
    private double inertia = 0.7;
    private double[] velocity;
    Random rnd = new Random();



    public Velocity(double[] velocity){
        this.velocity = velocity;
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

    public double update_velocity(double velocity_at_position, double personalBestPosition, double globalBestPosition, double currentPosition){
        private double newVelocity = 0.0d;
        private double r1 = rnd.nextDouble();
        private double r2 = rnd.nextDouble();

        for (int i = 0; i < n_dimensions; i++){
            private double oldVelocity = getVelocityAt(i);
            private double newVelocity =
            //newVelocity = inertia * velocity_at_position + (c1 * (personalBestPosition - currentPosition)) * r1 + (c2 * (globalBestPosition - currentPosition) * r2);
        }
        newVelocity = inertia * velocity_at_position + (c1 * (personalBestPosition - currentPosition)) * r1 + (c2 * (globalBestPosition - currentPosition) * r2);
        // R1 = dxd diagonal matrix with random numbers from normal distribution [0,1]
        // R2 = dxd diagonal matrix with random numbers from normal distribution [0,1]
        //  p = personal best position
        //  g = global best position
        return newVelocity;
    }


    /**
    public double[][] drawMatrix(int n_dimensions) {
        Random rnd = new Random();
        double[][] randomMatrix = new double[n_dimensions][n_dimensions];

        for(int i = 0; i < n_dimensions; i++) {
            for(int j = 0; j < n_dimensions; j++) {
                if (i == j) {
                    double randomVal = rnd.nextDouble();
                    randomMatrix[i][j] = randomVal;
                }
            }
        }
        return randomMatrix;
    }
    */

}