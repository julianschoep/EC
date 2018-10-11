import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.Random;
import java.util.Collections;
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
        // select the initial K clusters, assuming particles arent ordered, selecting first three is random enough
        for(int i = 0; i < k; i++){
            Subswarm cluster = new Subswarm(d,evaluation,rnd);
            cluster.addParticle(particles[i]);
            cluster.calculateCentroid();
            clusters[i] = cluster;
        }
        // assign remaining particles
        clearClusterAssignments(); // make sure not to add duplicate particles to clusters, run before assignment!
        assignParticles(); // reassigning the initial particles will not yield different clusters
        updateCentroids();
    }

    public void clearClusterAssignments(){
        for(int i = 0; i < k; i++){
            this.clusters[i].removeAllParticles();

        }
    }

    private Subswarm[] copyClusters(Subswarm[] clusters){
        // copies clusters
        Subswarm[] clusterCopy = new Subswarm[this.k];
        assert clusters.length == this.k;
        for(int i = 0; i < k; i++){
            Particle[] cParticles = clusters[i].getParticles();
            clusterCopy[i] = new Subswarm(cParticles, this.evaluation, this.rnd);
            clusterCopy[i].calculateCentroid();
        }
        return clusterCopy;
    }

    private Subswarm[] assignParticles(){
        Subswarm[] clusters = new Subswarm[this.k];
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
        this.clusters = clusters;
        return clusters;
    }

    public Subswarm[] cluster(){
        // ?
        boolean satisfied = false;
        while(!satisfied){
            System.out.println("CLUSTER STEP!");
            Subswarm[] oldClusters = copyClusters(this.clusters);
            // asuming clusters are initialized,
            // clear the cluster assignments to avoid duplicate assignment
            clearClusterAssignments();
            // assign clusters according to means of previous step
            Subswarm[] clusters = assignParticles();
            updateCentroids();

            satisfied=true;
            for(int i = 0; i < k; i++){ // check if clusters changed
                double[] newCentroid = clusters[i].getCentroid();
                double[] oldCentroid = oldClusters[i].getCentroid();
                boolean isEqual = true;
                for(int dim = 0; dim < this.d; dim++) { // is centoid the same?
                    if (newCentroid[dim] != oldCentroid[dim]) {
                        System.out.println(String.format("%f is not %f", newCentroid[dim],oldCentroid[dim]));
                        isEqual = false;
                    }
                }
                if(!isEqual){
                    satisfied=false; // centroid changed, so assignment changed, so clustering is still in progress.
                }
            }
        }
        return this.clusters;
    }

    public double getSquaredError(){
        double sq_err = 0;
        for(int i = 0; i < this.k; i++){
            sq_err += this.clusters[i].getSquaredError();
        }
        return sq_err;
    }



    private void updateCentroids(){
        for(int i = 0; i < this.k; i++){
            this.clusters[i].calculateCentroid();
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