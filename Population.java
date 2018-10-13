import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.Random;
import java.lang.Math;

public class Population {
    private Particle[] population;
    private double[] gbestPosition;
    private double gbestFitness;
    private int pSize;
    private ContestEvaluation evaluation;
    private Random rnd;
    private Subswarm[] subswarms;
    private int d;


    public Population(int pSize, int nDimensions, ContestEvaluation evaluation, Random rnd){
        // Initialize the population
        this.population = new Particle[pSize];
        this.pSize = pSize;
        this.gbestFitness = -Double.MAX_VALUE;
        this.evaluation = evaluation;
        this.rnd = rnd;
        this.d = nDimensions;

        for(int i = 0; i < pSize; i++){
            this.population[i] = new Particle(nDimensions, this.evaluation, this.rnd, Integer.toString(i)); // randomly init N particles, calls eval!
        }
        this.updateGlobalFitness();
    }

    public void identifyNiches(){
        // do clustering
        // for k in {2 -- this.pSize/2}
        //      do 10 cluster iterations, select best on basis of error (implement error for cluster)
        // select best cluster based on BIC (implement BIC calculation)

        Subswarm[] bestClusters = new Subswarm[0];
        int bestK = -1;
        double bestBIC = Double.MAX_VALUE;
        for(int k = 2; k < this.pSize/2; k++){

            double bestError = Double.MAX_VALUE;
            Subswarm[] bestClustersAtK = new Subswarm[0];
            print("k: ",k);
            for(int i = 0; i < 10; i++){

                print("i: ", i);
                Kmeans kmeans = new Kmeans(this.d, k, this.population,this.evaluation, this.rnd);
                Subswarm[] clusters = kmeans.cluster();
                double squaredError = kmeans.getSquaredError();
                if(squaredError < bestError){
                    bestError = squaredError;
                    bestClustersAtK = clusters;
                }
            }

            double BIC = calculateBIC(bestClustersAtK);
            if(BIC < bestBIC){
                bestBIC = BIC;
                bestK = k;
                bestClusters = bestClustersAtK;
            }
        }
        print("BEST CLUSTER VARIANCE",getSquaredError(bestClusters));
        print("BEST BIC", bestBIC);
        print("BEST K", bestK);

        // do cutting procedure
        // calculate average number of particles in the clusters
        double Navg = 0;
        for(int i = 0; i < bestClusters.length; i++){
            Navg += bestClusters[i].getParticles().length;
        }
        Navg = Navg/bestClusters.length;
        // identify particles to cut; cut clusters with Nj > Navg
        for(int i = 0; i < bestClusters.length; i++){
            int dN = bestClusters[i].getParticles().length - (int) Navg;
            if(dN > 0){ // there are more particles than Navg in cluster
                // remove bottom dN particles from cluster based on pbest.
                Subswarm[] unniched = bestClusters[i].removeBottomNParticles(dN);
            }
        }

        System.out.println("DONE");

        // assign  cutted particles to vonneumann topology as a seperate swarm
        //
    }

    private double calculateBIC(Subswarm[] clusters){
        int k = clusters.length;
        int p = (k-1)+(this.d*k)+k;
        int N = this.pSize;

        double likelihoodSum = 0;
        for(int i = 0; i < k; i ++){
            Subswarm cluster = clusters[i];
            int Nj = clusters[i].getParticles().length;
            double sigmaSquared = clusters[i].getSquaredError();
            likelihoodSum += ((-Nj/2)*Math.log(2*Math.PI))
                            -(((Nj*this.d)/2)*Math.log(sigmaSquared))
                            - ((Nj-1)/2)
                            + (Nj*Math.log(Nj));
        }

        double likelihoodClusters = likelihoodSum - (N * Math.log(N));
        double BIC = likelihoodClusters - (p/2)*Math.log(N);
        return Math.abs(BIC);
    }

    public void iterate() { // not very pretty...
        // update particle positions
        //for (int i = 0; i < this.pSize; i++) {
        //this.swarm[i].updatePosition(this.gbestPosition);
        //}

        // select new global best
        //this.updateGlobalFitness();
    }

    public void print(String text, double value){
        String thing  = text + "%f";
        System.out.println(String.format(thing,value));

    }


    public void updateGlobalFitness(){
        // not implemented
    }

    public double getSquaredError(Subswarm[] clusters){

        double sq_err = 0;
        //System.out.println("calculating squared error");
        for(int i = 0; i < clusters.length; i++){
            //print("Squared error",sq_err);
            sq_err += clusters[i].getSquaredError();
        }
        return sq_err;
    }

    public double[] getGbestPosition() {
        return this.gbestPosition;
    }

    public double getGbestFitness() {
        return this.gbestFitness;
    }










}