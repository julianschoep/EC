import org.vu.contest.ContestEvaluation;
import java.util.Random;
import java.lang.Math;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
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
        this.swarm = new Particle[0];
        this.centroid = new double[d];
    }

    public Subswarm(Particle[] particles,int d,ContestEvaluation evaluation, Random rnd){
        this.swarm = particles;
        this.gbestFitness = -Double.MAX_VALUE;
        this.evaluation = evaluation;
        this.rnd = rnd;
        this.centroid = new double[d];
        this.d = d;
    }

    public void print(String text, double value){
        String thing  = text + "%f";
        System.out.println(String.format(thing,value));

    }

    public double[] calculateCentroidForCopy(){
        print("number of dimensions", this.d);
        // calculate the centroid based on the particles in the swarm
        for(int dim = 0; dim < this.d; dim ++){
            double sum = 0;
            print("Number of particles", swarm.length);
            for(int i = 0; i < swarm.length; i++){
                sum += swarm[i].getPositionAt(dim);
            }
            double avg_val = sum / (double) swarm.length;
            if( dim == 0) {
                print("sum", sum);
                print("avg", avg_val);
                print("swarm lenght", (double) swarm.length);
            }
            this.centroid[dim] = avg_val;
        }
        print("AND SO THE CENTROID IS:",this.centroid[0]);
        return this.centroid;
    }

    public double[] calculateCentroid(){
        // calculate the centroid based on the particles in the swarm
        for(int dim = 0; dim < this.d; dim ++){
            double sum = 0;
            for(int i = 0; i < swarm.length; i++){
                sum += swarm[i].getPositionAt(dim);
            }
            double avg_val = sum / (double) swarm.length;
            this.centroid[dim] = avg_val;
        }
        return this.centroid;
    }

    public void removeAllParticles(){
        // needed when reassigning particles
        this.swarm = new Particle[0];
    }

    public double[] getCentroid(){
        return this.centroid;
    }

    public void addParticle(Particle newParticle){
        int old_n = swarm.length;
        Particle[] updatedSwarm = new Particle[old_n+1];
        for(int i = 0; i < old_n;i++){
            updatedSwarm[i] = this.swarm[i];
        }
        updatedSwarm[old_n] = newParticle;
        this.swarm = updatedSwarm;
    }

    public void removeParticle(Particle particle){
        int old_n = swarm.length;
        Particle[] updatedSwarm = new Particle[old_n-1];
        for(int i = 0; i < old_n;i++){
            if(this.swarm[i] != particle){
                updatedSwarm[i] = this.swarm[i];
            }
        }
        this.swarm = updatedSwarm;
    }

    public Subswarm[] removeBottomNParticles(int N){

        // removes bottom N particles from swarm based on pbest and returns them
        List<Particle> P = new ArrayList<Particle>();

        for(int i = 0; i < this.swarm.length; i++){
            P.add(this.swarm[i]);
            System.out.println(this.swarm[i].getBestFitness());
            print("haa", 0);
        }
        Collections.sort(P);

        for(Particle a: P){
            print("ordered by pbest: ", a.getBestFitness());
        }
        int a=0;
        if(a == 0){
            System.out.println("SWARM IS SORTED BASED ON PBEST");
            System.out.println("Fitness last");
            System.out.println(this.swarm[this.swarm.length -1].getBestFitness());
            System.out.println("Fitness second last (should be higher)");
            System.out.println(this.swarm[this.swarm.length -2].getBestFitness());
            double b = 1/0;
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            System.out.println(b);
            throw new IllegalArgumentException("bla");
        }else{
            return new Subswarm[3];
        }


    }

    public double getSquaredError(){
        double sum_err = 0;
        //System.out.println("Calc sq err for 1 cluster..");
        for(int i = 0; i < this.swarm.length; i++){
            double[] particlePosition = this.swarm[i].getPosition();
            //for(int j = 0; j < this.d; j++){

                //sum_err += Math.pow(particlePosition[j] - this.centroid[j],2);
            //}
            double distance_to_centroid = calculateEuclideanDistance(particlePosition,this.centroid);
            //print("Distance to centroid:", distance_to_centroid);
            sum_err += Math.pow(distance_to_centroid,2);
            //print("Summed error: ",sum_err);
        }

        double average_squared_error = (1.0/(this.swarm.length))*sum_err;
        return average_squared_error;

    }


    public Particle[] getParticles() {
        return this.swarm;
    }

    public void iterate() { // not very pretty...
        // update particle positions
        for (int i = 0; i < this.swarm.length; i++) {
            this.swarm[i].updatePosition(this.gbestPosition);
        }

        // select new global best
        this.updateGlobalFitness();
    }


    public void updateGlobalFitness(){
        for (int i = 0; i < this.swarm.length; i++) {
            double particleFitness = this.swarm[i].getBestFitness();

            if (particleFitness > this.gbestFitness) {
                this.gbestFitness = particleFitness;
                this.gbestPosition = this.swarm[i].getBestPosition();
            }
        }
    }

    public double calculateEuclideanDistance(double[] x, double[] y){
        double totalDistance = 0;
        for(int i = 0; i < this.d; i++){
            double xi 			=  x[i];
            double yi 			=  y[i];
            double distance	 	=  Math.pow(xi - yi, 2);
            totalDistance 		+= distance;
        }
        double result = Math.sqrt(totalDistance);
        return result;
    }

    public double[] getGbestPosition() {
        return this.gbestPosition;
    }

    public double getGbestFitness() {
        return this.gbestFitness;
    }

}