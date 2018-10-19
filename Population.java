import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.Random;
import java.lang.Math;

public class Population {
    public Particle[] population;
    private double[] gbestPosition;
    private double gbestFitness;
    private int pSize;
    private ContestEvaluation evaluation;
    private Random rnd;
    private Subswarm[] subswarms;
    private NeumannNode[] neumannTopology;
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
            this.population[i] = new Particle(nDimensions, this.evaluation, this.rnd, i); // randomly init N particles, calls eval!
        }
        // all particles have a pbest now, but no gbest yet.
    }

    public void identifyNiches(){
        // do clustering
        // for k in {2 -- this.pSize/2}
        //      do 10 cluster iterations, select best on basis of error (implement error for cluster)
        // select best cluster based on BIC (implement BIC calculation)

        Subswarm[] bestClusters = new Subswarm[0];
        Kmeans bestKmeans = new Kmeans();
        int bestK = -1;
        double bestBIC = Double.MAX_VALUE;
        //System.out.println("Clustering...");
        for(int k = 2; k < this.pSize/2; k++){
            //System.out.println(String.format("k: %di",k));
            double bestError = Double.MAX_VALUE;
            Subswarm[] bestClustersAtK = new Subswarm[0];
            Kmeans bestKmeansAtK = new Kmeans();
            //print("k: ",k);
            for(int i = 0; i < 10; i++){

                //print("i: ", i);
                Kmeans kmeans = new Kmeans(this.d, k, this.population,this.evaluation, this.rnd, i);
                Subswarm[] clusters = kmeans.cluster();
                double squaredError = kmeans.getCombinedSquaredError();
                if(squaredError < bestError){
                    bestError = squaredError;
                    bestClustersAtK = clusters;
                    bestKmeansAtK  = kmeans;
                }
            }

            double BIC = calculateBIC2(bestClustersAtK);
            if(BIC < bestBIC){
                bestBIC = BIC;
                bestK = k;
                bestClusters = bestClustersAtK;
                bestKmeans = bestKmeansAtK;
            }
        }
        //print("BEST CLUSTER VARIANCE",getCombinedSquaredError(bestClusters));
        //System.out.println("BEST CLUSTER NAME: "+bestKmeans.name);
        //print("Best cluster kmeans var", bestKmeans.getCombinedSquaredError());
        //print("BEST BIC", bestBIC);
        //print("BEST K", bestK);


        // Do cutting procedure

        double Navg = 0;
        for(int i = 0; i < bestClusters.length; i++){
            Navg += bestClusters[i].getParticles().length;
        }
        Navg = Navg/bestClusters.length;
        // identify particles to cut; cut clusters with Nj > Navg
        Particle[] removedParticles = new Particle[0];
        for(int i = 0; i < bestClusters.length; i++){
            int Nj = bestClusters[i].getParticles().length;
            int dN = Nj  - (int) Navg;
            if(dN > 0){ // there are more particles than Navg in cluster
                // remove bottom dN particles from cluster based on pbest.
                //System.out.println("Removing " + Integer.toString(dN)+" particles from "+bestClusters[i].name);
                //System.out.println(String.format("avg: %d, dN: %d, Nj: %d",(int)Navg,dN,Nj));
                Particle[] unniched = bestClusters[i].removeBottomNParticles(dN);
                removedParticles = merge(removedParticles, unniched);
            }
        }
        //print("NUMBER OF UNNICHED PARTICLES ",removedParticles.length);
        this.neumannTopology = createVonNeumannTopology(removedParticles);
        this.subswarms = bestClusters;
        propagateVelocityClamp(bestClusters);
        int nInClusters = 0;
        for(int i = 0; i < this.subswarms.length; i++){
            nInClusters += this.subswarms[i].getLength();
        }
        //print("NUMBER OF CLUSTERED PARTICLES ",nInClusters);
        //print("NUMBER OF NICHED PARTICLES ",neumannTopology.length);
        assert neumannTopology.length + nInClusters == this.pSize: "number of particles should stay the same...";


        //System.out.println("DONE");
        // assert 1 == 2: "1 is not 2 dude";
        // assign  cutted particles to vonneumann topology as a seperate swarm
        //
    }
    public void propagateVelocityClamp(Subswarm[] clusters){
        for(int i = 0; i < clusters.length; i++){
            clusters[i].propagateVClamp();
        }
    }


    private Particle[] merge(Particle[] a, Particle[] b){
        // merges two particle arrays together

        int new_n = a.length + b.length;
        Particle[] result = new Particle[new_n];
        int i;
        for(i = 0; i < a.length; i++){
            result[i] = a[i];
        }
        int j;
        for(j= 0; j < b.length; j++){
            int idx= (i+j);
            result[idx] = b[j];
        }

        return result;
    }

    private double calculateBIC(Subswarm[] clusters){
        //System.out.println(Math.log(0));
        int k = clusters.length;
        int p = (k-1)+(this.d*k)+k;
        int N = this.pSize;

        double likelihoodSum = 0;
        for(int i = 0; i < k; i ++){

            Subswarm cluster = clusters[i];
            int Nj = clusters[i].getParticles().length;
            double sigmaSquared = clusters[i].getSquaredError();
            if(sigmaSquared == 0){ // protect against extremely small values
                sigmaSquared = 1e-4;
            }
            likelihoodSum += ((-Nj/2)*Math.log(2*Math.PI))
                            -(((Nj*this.d)/2)*Math.log(sigmaSquared))
                            - ((Nj-1)/2)
                            + (Nj*Math.log(Nj));
            if(Math.abs(likelihoodSum) > Double.MAX_VALUE){
                print("ERROR",0);
                System.out.println(Nj);
                System.out.println(sigmaSquared);
                assert 1 == 2: "thing";
            }
        }

        double likelihoodClusters = likelihoodSum - (N * Math.log(N));
        double BIC = likelihoodClusters - (p/2)*Math.log(N);

        return Math.abs(BIC);
    }

    private double calculateBIC2(Subswarm[] clusters){

        //System.out.println(Math.log(0));
        int k = clusters.length;
        int p = (k-1)+(this.d*k)+k;
        int N = this.pSize;

        double clVarSum = 0;

        for(int i = 0; i < k; i++){
            clVarSum += clusters[i].getSquaredError();
        }
        double clVar = (1/(N-k)/this.d);

        double BICSum = 0;
        for(int i = 0; i < k; i++){
            double Nj = clusters[i].getSwarm().length;
            BICSum += Nj * Math.log(Nj) -
                    Nj * Math.log(N) -
                    ((Nj*this.d)/2) * Math.log(2*Math.PI*clVar) -
                    ((Nj -1)*this.d*0.5);
        }
        double constTerm = 0.5*k*Math.log(N) * (this.d+1);
        double BIC = BICSum = constTerm;
        //System.out.println(String.format("BIC THING! %f",BIC));

        return BIC;
    }

    public void iterate() { // updates position of all particles
        // first update the nodes in the clusters
        for(int i = 0; i < this.subswarms.length; i++){
            Subswarm cluster = this.subswarms[i];
            cluster.updatePosition();
        }

        for(int i = 0; i < this.neumannTopology.length; i++){
            NeumannNode node = this.neumannTopology[i];
            node.updatePosition(node.getGlobalBestPosition());
        }

    }

    public void updateGlobalFitness(){ // this will update the gbests of all the subswarms and neumann topologies

        // first update the nodes in the clusters
        for(int i = 0; i < this.subswarms.length; i++){
            Subswarm cluster = this.subswarms[i];
            double bestFitness = cluster.updateGlobalFitness();
            if(bestFitness > this.gbestFitness){
                this.gbestFitness = bestFitness;
                this.gbestPosition = cluster.getGbestPosition();
            }
        }

        for(int i = 0; i < this.neumannTopology.length; i++){
            NeumannNode node = this.neumannTopology[i];
            double bestFitness = node.updateGlobalFitness();
            if(bestFitness > this.gbestFitness){
                this.gbestFitness = bestFitness;
                this.gbestPosition = node.getGlobalBestPosition();
            }
        }
    }

    public void print(String text, double value){
        String thing  = text + "%f";
        System.out.println(String.format(thing,value));

    }

    public double getCombinedSquaredError(Subswarm[] clusters){

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


    private NeumannNode[] createVonNeumannTopology(Particle[] particles){
        //System.out.println("PARTICLES TO BE PUT IN RING");
        //for(int i = 0; i < particles.length; i++){
        //    String line = String.format("%d: %d",i,particles[i].name);
        //    System.out.println(line);
        //}

        // just doing ring topology for now, we can see if we want to do von neumann
        NeumannNode[] topology = new NeumannNode[particles.length];
        // populate topology
        int M = particles.length;
        for(int i = 0; i < M; i++){
            //System.out.println(String.format("part name: %d",particles[i].name));
            topology[i] = new NeumannNode(particles[i]);
            //System.out.println(String.format("neuman name: %d",topology[i].name));
        }

        if(M == 1){
            return topology;
        }else if(M < 4){ // equivalent to fully connected graph
            for(int i = 0; i < M; i++){
                for(int j = 0; j < M; j++){
                    if(j != i){
                        topology[i].addNeighbour(topology[j]);
                    }
                }
            }
        }else{
            for(int i = 0; i < M; i++){
                int leftNeighbour = i -1;
                if(leftNeighbour < 0){
                    leftNeighbour = M-1;
                }
                int rightNeighbour = i+1;
                if(rightNeighbour > M-1){
                    rightNeighbour = 0;
                }
                //String line = String.format("left: %d, this: %d, right %d",leftNeighbour,i,rightNeighbour);
                //System.out.println(line);
                topology[i].addNeighbour(topology[leftNeighbour]);
                topology[i].addNeighbour(topology[rightNeighbour]);
                //topology[i].reportNeighbours();
            }
        }
        return topology;
    }









}