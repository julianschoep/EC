import org.vu.contest.ContestEvaluation;
import java.util.Random;

public class Particle {
    private double fitness;
    private double bestFitness;
    private int nDimensions; // java way of var naming is first letter small, next word capital, no '_'
    private double[] position;
    private double[] velocity;
    private double[] bestPosition;
    private double c1 = 2.0;
    private double c2 = 2.0;
    private double inertia = 0.7;
    private ContestEvaluation evaluation;
    private Random rnd;


    public Particle(int nDimensions, ContestEvaluation evaluation, Random rnd){
        // initialize the particle
        this.nDimensions = nDimensions;
        this.evaluation = evaluation;
        this.rnd = rnd;
        this.position = new double[this.nDimensions];
        this.bestPosition = new double[this.nDimensions];
        this.velocity = new double[this.nDimensions];

        // initialize coordinates with random values between -5 and 5
        for (int i = 0; i < this.nDimensions; i++){
            double randomPos= (-5.0d) + (5.0d - (-5.0d)) * this.rnd.nextDouble();
            this.position[i] = randomPos;
            this.bestPosition[i] = randomPos;
        }
        // initialize velocity double[] with small random values
        for(int i = 0; i < this.nDimensions; i++){
            double smallRandomValue = (rnd.nextDouble() + 0.1d) / 10;
            this.velocity[i] = smallRandomValue;
        }

        // initialze position and pbest / bestFitness
        this.fitness = this.bestFitness = (double) evaluation.evaluate(this.position);
    }


    public void updateVelocity(double[] globalBestPosition){
        // Formula: inertia * v(t) + c1 * (p - x(t)) * R1 + c2 * (g - x(t)) * R2
        // constants: inertia = 0.7, c1 = c2 = 2
        // R1 = dxd diagonal matrix with random numbers from normal distribution [0,1]
        // R2 = dxd diagonal matrix with random numbers from normal distribution [0,1]
        //  p = personal best position
        //  g = global best position
        double[] newVelocity = new double[this.nDimensions];

        //System.out.println(globalBestPosition[0]);
        //System.out.println(this.position[0]);
        //System.out.println(this.bestPosition[0]);

        for (int i = 0; i < this.nDimensions; i++){
            double r1 = this.rnd.nextDouble();
            double r2 = this.rnd.nextDouble();
            newVelocity[i] = (inertia * this.velocity[i]) + (c1 * (this.bestPosition[i] - this.position[i]) * r1) + (c2 * (globalBestPosition[i] - this.position[i]) * r2);
        }
        this.velocity = newVelocity;
    }


    public void updateCoordinates(double[] gBestPosition){
        // update velocities
        this.updateVelocity(gBestPosition);
        double[] newCoordinates = new double[nDimensions];

        // update positions
        //System.out.println("Updating coordinates");
        for(int i = 0; i < this.nDimensions; i++){
            newCoordinates[i] = this.position[i] + this.velocity[i];
            //System.out.print(newCoordinates[i]);
            //System.out.print(',');
        }
        //System.out.println(" ");
        this.position = newCoordinates;
    }


    public void updatePosition(double[] gBestPosition){
        this.updateCoordinates(gBestPosition);
        //System.out.println(this.evaluation);
        //System.out.println(this.position[0]);
        this.fitness =  (double) this.evaluation.evaluate(this.position);

        // check if position update is better than best position
        if(this.fitness > this.bestFitness){
            this.bestPosition = this.position;
        }
    }



    public double[] getPosition(){
        return this.position;
    }

    public double[] getBestPosition(){
        return this.bestPosition;
    }

    public double getFitness(){ return this.fitness; }

    public double getBestFitness(){
        return this.bestFitness;
    }

    public void setBestFitness(double bestFitness){
        this.bestFitness = bestFitness;
    }

    public void setPosition(double[]  p){
        this.position = p;
    }

    public void setBestPosition(double[] bestPosition){
        this.bestPosition = bestPosition;
    }

    public void setFitness(double fitness){
        this.fitness = fitness;
    }

}