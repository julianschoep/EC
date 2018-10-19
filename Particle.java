import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.Random;
import java.util.Comparator;
import java.lang.Double;
import java.lang.Math;

public class Particle implements Comparator<Particle>, Comparable<Particle>{
    public double fitness;
    public double bestFitness;
    public int nDimensions; // java way of var naming is first letter small, next word capital, no '_'
    public double[] position;
    public double[] velocity;
    public double[] bestPosition;
    public double c1 = 2;
    public double c2 = 2;
    public double inertia = 0.7;
    public ContestEvaluation evaluation;
    public Random rnd;
    public int name;
    public double velocityClamp;


    public Particle(int nDimensions, ContestEvaluation evaluation, Random rnd, int name){
        // initialize the particle
        this.nDimensions = nDimensions;
        this.evaluation = evaluation;
        this.rnd = rnd;
        this.position = new double[this.nDimensions];
        this.bestPosition = new double[this.nDimensions];
        this.velocity = new double[this.nDimensions];
        this.name = name;
        this.velocityClamp = -1.0;


        // initialize coordinates with random values between -5 and 5
        for (int i = 0; i < this.nDimensions; i++){
            double randomPos= (-5.0d) + (5.0d - (-5.0d)) * this.rnd.nextDouble();
            this.position[i] = randomPos;
            this.bestPosition[i] = randomPos;
        }
        // initialize velocity double[] with small random values
        for(int i = 0; i < this.nDimensions; i++){
            double smallRandomValue = (rnd.nextDouble() + 0.1d) / 10;
            //this.velocity[i] = smallRandomValue;
            this.velocity[i] = 0;
        }

        // initialze position and pbest / bestFitness
        this.fitness = this.bestFitness = (double) evaluation.evaluate(this.position);
    }

    public void setVelocityClamp(double clamp){
        //System.out.println("RECEIVED VELOCITY CLAMP!");
        this.velocityClamp = clamp;
    }


    public void updatePosition(double[] gBestPosition){
        double[] oldPosition = this.position;
        double[] oldVelocity = this.velocity;
        double[] newPosition = new double[nDimensions];
        double[] newVelocity = new double[this.nDimensions];

        //System.out.println("VELOCITY before - after");
        //printArray(oldVelocity);
        for (int i = 0; i < this.nDimensions; i++){
            double r1 = this.rnd.nextDouble()/2;
            double r2 = this.rnd.nextDouble()/2;
            double newV = (inertia * oldVelocity[i])
                           + (c1 * (this.bestPosition[i] - oldPosition[i]) * r1)
                           + (c2 * (gBestPosition[i] - oldPosition[i]) * r2);
            if(this.velocityClamp > 0.01){
                //System.out.println("Velocity clamp applied");
                //System.out.println(this.velocityClamp);
                newVelocity[i] = Math.min(newV, this.velocityClamp);
            }else{
                newVelocity[i] = newV;
            }

        }
        //printArray(newVelocity);

        //System.out.println("POSITION before - after");
        //this.printArray(oldPosition);
        for(int i = 0; i < this.nDimensions; i++){
            newPosition[i] = oldPosition[i] + newVelocity[i];
        }
        //this.printArray(newPosition);

        double fitness =  (double) this.evaluation.evaluate(newPosition);
        //System.out.println(fitness);

        // check if position update is better than best position
        if(fitness > this.bestFitness){
            this.bestFitness = fitness;
            this.bestPosition = newPosition;
        }
        this.position = newPosition;
        this.velocity = newVelocity;
        this.fitness = fitness;
    }


    public void printArray(double[] arrayBefore){
        for(int i = 0; i < this.nDimensions; i++){
            System.out.print(arrayBefore[i]);
            System.out.print(' ');
        }
        System.out.println(" ");
    }

    public void dummyFunction(){
        this.bestFitness = this.name;
    }


    public double[] getPosition(){
        return this.position;
    }

    public double getPositionAt(int i){ return this.position[i];}

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

    // COMPARATOR FUNCTIONS
    public int compare(Particle a, Particle b){
        if((double) a.getBestFitness() > (double) b.getBestFitness()){
            System.out.println("A larger than B");
            return 1;
        }else if( (double) a.getBestFitness() < (double) b.getBestFitness()){
            System.out.println("A smaller than B");
            return -1;
        }else{

            return 0;
        }
    }

    public int compareTo(Particle b){
        return Double.valueOf(this.bestFitness).compareTo(Double.valueOf(b.getBestFitness()));
    }

    public int getNDims(){
        return this.nDimensions;
    }

    public ContestEvaluation getEvaluation() {
        return this.evaluation;
    }

    public Random getRnd() {
        return this.rnd;
    }


}

/*


    public double[] updateVelocity(double[] globalBestPosition, double[] position){
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
            newVelocity[i] = (inertia * this.velocity[i]) + (c1 * (this.bestPosition[i] - position[i]) * r1) + (c2 * (globalBestPosition[i] - position[i]) * r2);
        }
        return newVelocity;
    }


    public double[] updateCoordinates(double[] gBestPosition, double[] oldPosition){
        // update velocities
        velocity = this.updateVelocity(gBestPosition, position);
        double[] newCoordinates = new double[nDimensions];

        // update positions
        //System.out.println("Updating coordinates");
        for(int i = 0; i < this.nDimensions; i++){
            newCoordinates[i] = oldPosition[i] + velocity[i];
            //System.out.print(newCoordinates[i]);
            //System.out.print(',');
        }
        //System.out.println(" ");
        return newCoordinates;
    }


    public void updatePosition(double[] gBestPosition){
        double[] oldPosition = this.position;
        double[] position = this.updateCoordinates(gBestPosition, oldPosition);
        //System.out.println(this.evaluation);
        //System.out.println(this.position[0]);
        this.fitness =  (double) this.evaluation.evaluate(position);

        // check if position update is better than best position
        if(this.fitness > this.bestFitness){
            this.bestPosition = position;
        }
        this.position = position;
    }

 */