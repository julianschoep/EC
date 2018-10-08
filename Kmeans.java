import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.Random;
import java.util.Collections;
public class Kmeans{
    private int k;
    private Particle[] particles;
    private Subswarm[] clusters;
    private Subswarm[] old_clusters;
    private int d;


    public Kmeans(int d, int k, Particle[] particles,ContestEvaluation evaluation, Random rnd){
        // initialize kmeans algorithm
        this.k = k;
        this.clusters = new Subswarm[k];
        this.particles = particles;
        // select the initial K clusters, assuming particles arent ordered, selecting first three is random enough
        for(int i = 0; i < k; i++){
            cluster = new Subswarm(d,evaluation,rnd);
            cluster.addParticle(particles[i]);
            cluster.calculateCentroid();
            clusters[i] = cluster;
        }
        // assign remaining particles
        clearClusterAssignments() // make sure not to add duplicate particles to clusters, run before assignment!
        assignParticles(particles) // reassigning the initial particles will not yield different clusters
        updateCentroids()
    }

    public void clearClusterAssignments(){
        for(int i = 0; i < k; i++){
            this.clusters[i].removeAllParticles();

        }
    }

    private void assignParticles(Particles[] particles){
        for(int i = k-1; i < particles.length; i++){
            double[] position = particles[i].getPosition();

            int best_cluster = -1;
            double smallest_distance = Double.MAX_VALUE;
            for(int cluster_i = 0; cluster_i < k; cluster_i ++){
                double dist = Math.pow(calculateEuclideanDistance(clusters[cluster_i].getCentroid(), position),2); // squared distance!
                if(dist < smallest_distance){
                    smallest_distance = dist;
                    best_cluster = cluster_i;
                }
            }
            this.clusters[cluster_i].addParticle(particles[i])
        }
    }

    public Subswarm[] cluster(){
        // ?
        boolean satisfied = false;
        while(!satsified){

        }

    }


    private void updateCentroids(){
        for(int i = 0; i < this.k; i++){
            this.clusters[i].updateCentroid();
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