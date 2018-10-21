import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.Random;
import java.util.Collections;
import java.util.Arrays;

public class Kmeans{
    private int k;
    private Particle[] particles;
    private Subswarm[] clusters;
    private int d;
    private ContestEvaluation evaluation;
    private Random rnd;
    public String name;

    public Kmeans(){
        // emptry constructor for dummy variables
        this.k = 2;
        this.clusters = new Subswarm[2];
        this.particles = new Particle[10];
        this.d = 10;
    }


    public Kmeans(int d, int k, Particle[] particles,ContestEvaluation evaluation, Random rnd, int runID){
        // initialize kmeans algorithm
        this.k = k;
        this.clusters = new Subswarm[k];
        this.particles = particles;
        this.evaluation = evaluation;
        this.rnd = rnd;
        this.d = d;
        this.name = String.format("%d-%d",k,runID);

        //System.out.println("Init FOR K: "+Integer.toString(k));
        //System.out.println("KMEANS "+this.name+" CREATED!");


        int[] chosen_particles =  new int[k];
        Particle[] pchosen_particles = new Particle[k];
        // select the initial K clusters, as k random particles
        for(int i = 0; i < k; i++){
            boolean satisfied = false;
            while(!satisfied){
                int random_index = rnd.nextInt(particles.length); // choose random int between 0 and n particles
                boolean index_alreadychosen = contains(chosen_particles,random_index);
                if(!index_alreadychosen) { // index not yet chosen
                    chosen_particles[i] = random_index;
                    pchosen_particles[i] = particles[random_index];
                    Subswarm cluster = new Subswarm(d, evaluation, rnd,i,this.name);
                    cluster.addParticle(particles[random_index]);
                    //cluster.calculateCentroid();
                    this.clusters[i] = cluster;
                    satisfied = true;
                }
            }
        }

        updateCentroids(this.clusters);
        // assign remaining particles
        clearClusterAssignments(this.clusters); // make sure not to add duplicate particles to clusters, run before assignment!
        assignParticles(this.clusters,-1); // reassigning the initial particles will not yield different clusters
        updateCentroids(this.clusters);
    }

    private boolean contains(int[] array, int contain){
        for(int i = 0; i < array.length; i++){
            if(array[i] == contain){
                return true;
            }
        }
        return false;
    }

    public void clearClusterAssignments(Subswarm[] clusters){
        for(int i = 0; i < clusters.length; i++){
            clusters[i].removeAllParticles();
        }
    }

    private Subswarm[] copyClusters(Subswarm[] clusters){
        // copies clusters
        Subswarm[] clusterCopy = new Subswarm[clusters.length];
        assert clusters.length == this.k;
        for(int i = 0; i < clusterCopy.length; i++){
            Particle[] cParticles = clusters[i].getParticles();
            //print("og centroid: ",clusters[i].getCentroid()[0]);
            //print("NUMBEr OF PARTICLES TO COPY",(double) cParticles.length);
            clusterCopy[i] = new Subswarm(cParticles, this.d, this.evaluation, this.rnd);
            //System.out.println("Calculating centroid...");
            //print("copied particles number" ,clusterCopy[i].getParticles().length);
            double[] test = clusterCopy[i].calculateCentroid();
            //print("copy centroid: ",clusterCopy[i].getCentroid()[0]);
            //print("calc centroid: ",test[0]);
            //print("OG centroid: ", clusters[i].getCentroid()[0]);
        }
        return clusterCopy;
    }

    private Subswarm[] assignParticles(Subswarm[] clusters,int idx){

        for(int i = 0; i < this.particles.length; i++){
            // for each particle
            double[] position = this.particles[i].getPosition();

            int best_cluster = -1;
            double smallest_distance = Double.MAX_VALUE;
            for(int cluster_i = 0; cluster_i < k; cluster_i ++){
                // calculate distance distance to cluster centroid
                double dist = Math.pow(calculateEuclideanDistance(clusters[cluster_i].getCentroid(), position),2); // squared distance!
                if(dist < smallest_distance){
                    smallest_distance = dist;
                    best_cluster = cluster_i;
                }
            }
            // add particle to cluster with nearest centroid (aka mean)


            clusters[best_cluster].addParticle(this.particles[i]);
        }

        // if thing turns out to be zero we have a problem... So add a random particle to the 0 cluster.
        for(int i = 0; i < clusters.length; i++){
            Subswarm cluster = clusters[i];
            int Nj = cluster.getSwarm().length;
            if(Nj == 0){
                // cluster is empty.
                cluster.addParticle(bestEmergencyParticle(clusters));
            }
        }

        if(idx > 300){
            System.out.println("PRINTING CLUSTERS");
            for(int i = 0; i < clusters.length; i++){
                System.out.print(clusters[i].name + " | ");
                clusters[i].printCluster();
            }
        }


        return clusters;
    }



    public Particle bestEmergencyParticle(Subswarm[] clusters){
        // from the biggest cluster, find the most outlying particle, remove it
        // and return it.
        int largestN = -1;
        Subswarm largestCluster = clusters[0];
        for(int i = 0; i < clusters.length; i++){
            int Nj = clusters[i].getSwarm().length;
            if(Nj > largestN){
                largestCluster = clusters[i];
                largestN = Nj;
            }
        }
        Particle[] largestSwarm = largestCluster.getSwarm();
        double[] centroid = largestCluster.getCentroid();
        Particle farthestParticle = largestSwarm[0];
        double largestDistance = -1;
        for(int i = 0; i < largestSwarm.length; i++){
            double dist = calculateEuclideanDistance(centroid,largestSwarm[i].getPosition());
            if(dist > largestDistance){
                largestDistance = dist;
                farthestParticle = largestSwarm[i];
            }
        }
        largestCluster.removeParticle(farthestParticle);
        return farthestParticle;

    }



    public void print(String text, double value){
        String thing  = text + "%f";
        System.out.println(String.format(thing,value));

    }

    public int numSameClusters(){
        int sum = 0;
        for(int i = 0; i < this.clusters.length; i++){
            for(int j = i; j < this.clusters.length; j++){
                if(i != j){
                    Subswarm clusterA = clusters[i];
                    Subswarm clusterB = clusters[j];
                    double centrDist = calculateEuclideanDistance(clusterA.getCentroid(),clusterB.getCentroid());
                    if(centrDist < 1e-5){
                        sum += 1;
                    }
                }
            }
        }
        return sum;
    }

    public Subswarm getMatchingCluster(int particleName, Subswarm[] clusters){

        for(int i = 0; i < clusters.length; i++){
            //Subswarm cluster = clusters[i];
            Particle[] cluster = clusters[i].getSwarm();
            for(int j = 0; j < cluster.length; j++){
                if(cluster[j].name == particleName){
                    return clusters[i];
                }
            }
        }
        assert 1 == 2: String.format("PARTICLE %d IS NOT IN CLUSTERS...",particleName);
        return clusters[0];
    }

    public boolean clustersEqual(Subswarm[] clustersA, Subswarm[] clustersB){

        for(int i = 0; i < clustersA.length; i++){
            Particle[] clusterASwarm = clustersA[i].getSwarm();
            Subswarm clusterB = getMatchingCluster(clusterASwarm[0].name, clustersB);
            Particle[] clusterBSwarm = clusterB.getSwarm();
            int Na = clusterASwarm.length;
            int Nb = clusterBSwarm.length;
            if(Na != Nb){
                return false;
            }
            int[] clusterBParticles = new int[Nb];
            int[] clusterAParticles = new int[Na];
            for(int j = 0; j < Na; j++){
                clusterAParticles[j] = clusterASwarm[j].name;
                clusterBParticles[j] = clusterBSwarm[j].name;
            }
            Arrays.sort(clusterBParticles);
            Arrays.sort(clusterAParticles);
            //System.out.println("____________________");
            //System.out.println(Arrays.toString(clusterAParticles));
            //System.out.println(Arrays.toString(clusterBParticles));
            // Linearly compare elements
            for (int k=0; k<Na; k++)
                if (clusterBParticles[k] != clusterAParticles[k])
                    return false;

            // If all elements were same.

        }
        return true;
    }

    public Subswarm[] cluster(){
        // ?
        //System.out.print(String.format("k: %d \n ",this.k));
        int step_ctr =0;
        while(true){
            if((step_ctr%51 == 0)&&(step_ctr > 0)){
                System.out.print(String.format("%d |",step_ctr));
            }
            step_ctr +=1 ;
            Subswarm[] oldClusters = copyClusters(this.clusters);
            //print("old centroid",this.clusters[0].getCentroid()[0]);
            // asuming clusters are initialized,
            // clear the cluster assignments to avoid duplicate assignment
            clearClusterAssignments(this.clusters);

            // assign clusters according to means of previous step
            //System.out.println("EEH "+Integer.toString(step_ctr));
            this.clusters = assignParticles(this.clusters, step_ctr);
            //System.out.println("particles assigned");
            updateCentroids(this.clusters);
            //print("new centroid",this.clusters[0].getCentroid()[0]);

            if(clustersEqual(oldClusters, this.clusters)){
                // no cluster change detected.
                break;
            }
            if((step_ctr > 10) && (numSameClusters() > 0)){
                // stop when its going nowhere..
                break;
            }

        }
        //if(step_ctr > 20){
        //    System.out.println(" ");
        //}

        //print("CLUSTER VARIANCE ",this.getCombinedSquaredError());
        return this.clusters;
    }




    public double getCombinedSquaredError(){

        double sq_err = 0;
        //System.out.println("calculating squared error");
        for(int i = 0; i < this.clusters.length; i++){
            //print("Squared error",sq_err);
            sq_err += Math.pow(this.clusters[i].getSquaredError(),0.5);
        }
        return sq_err;
    }



    private void updateCentroids(Subswarm[] clusters){
        //System.out.println("CENTROIDS UPDATE!");
        for(int i = 0; i < this.k; i++){
            Subswarm cluster = clusters[i];
            int Nj = cluster.getSwarm().length;
            //System.out.println(String.format("Nj: %d",Nj));

            clusters[i].calculateCentroid();
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
}