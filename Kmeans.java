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


    public Kmeans(int d, int k, Particle[] particles,ContestEvaluation evaluation, Random rnd){
        // initialize kmeans algorithm
        this.k = k;
        this.clusters = new Subswarm[k];
        this.particles = particles;
        this.evaluation = evaluation;
        this.rnd = rnd;
        this.d = d;

        int[] chosen_particles =  new int[k];

        // select the initial K clusters, as k random particles
        for(int i = 0; i < k; i++){
            boolean satisfied = false;
            while(!satisfied){
                int random_index = rnd.nextInt(particles.length); // choose random int between 0 and n particles
                boolean index_alreadychosen = contains(chosen_particles,random_index);
                if(!index_alreadychosen) { // index not yet chosen
                    chosen_particles[i] = random_index;
                    Subswarm cluster = new Subswarm(d, evaluation, rnd);
                    cluster.addParticle(particles[random_index]);
                    cluster.calculateCentroid();
                    this.clusters[i] = cluster;
                    satisfied = true;
                }
            }
        }

        // assign remaining particles
        clearClusterAssignments(this.clusters); // make sure not to add duplicate particles to clusters, run before assignment!
        assignParticles(this.clusters); // reassigning the initial particles will not yield different clusters
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

    private Subswarm[] assignParticles(Subswarm[] clusters){

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
            //print("PARTICLE", i);
            //print("ADDED TO CLUSTER:", best_cluster);
            clusters[best_cluster].addParticle(this.particles[i]);
        }

        return clusters;
    }

    public void print(String text, double value){
        String thing  = text + "%f";
        System.out.println(String.format(thing,value));

    }

    public Subswarm[] cluster(){
        // ?
        int step_ctr =0;
        while(true){
            //print("Cluster step", step_ctr);
            step_ctr +=1 ;
            Subswarm[] oldClusters = copyClusters(this.clusters);
            //print("old centroid",this.clusters[0].getCentroid()[0]);
            // asuming clusters are initialized,
            // clear the cluster assignments to avoid duplicate assignment
            clearClusterAssignments(this.clusters);

            // assign clusters according to means of previous step
            this.clusters = assignParticles(this.clusters);
            updateCentroids(this.clusters);
            //print("new centroid",this.clusters[0].getCentroid()[0]);

            boolean satisfied=true;
            for(int i = 0; i < k; i++){ // check if clusters changed
                double[] newCentroid = this.clusters[i].getCentroid();
                double[] oldCentroid = oldClusters[i].getCentroid();
                //print("new centroid 0", (double) newCentroid[0]);
                //print("old centroid 0", (double) oldCentroid[0]);
                boolean isEqual = true;
                for(int dim = 0; dim < this.d; dim++) { // is centoid the same?
                    if (newCentroid[dim] != oldCentroid[dim]) {
                        //System.out.println(String.format("%f is not %f", newCentroid[dim],oldCentroid[dim]));
                        isEqual = false;
                        //print("This dim is different", dim);
                    }
                }
                if(!isEqual){
                    satisfied=false; // centroid changed, so assignment changed, so clustering is still in progress.
                    //System.out.println("Centroid changed!");
                    //print("New one: ", newCentroid[0]);
                    //print("Old one: ", oldCentroid[0]);
                    break;
                }
            }
            if(satisfied){
                //System.out.println("CLUSTERING DONE!");
                break;
            }
        }
        print("CLUSTER VARIANCE ",this.getSquaredError());
        return this.clusters;
    }




    public double getSquaredError(){

        double sq_err = 0;
        //System.out.println("calculating squared error");
        for(int i = 0; i < this.clusters.length; i++){
            //print("Squared error",sq_err);
            sq_err += Math.pow(this.clusters[i].getSquaredError(),0.5);
        }
        return sq_err;
    }



    private void updateCentroids(Subswarm[] clusters){
        for(int i = 0; i < this.k; i++){
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