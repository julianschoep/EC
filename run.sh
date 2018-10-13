echo “compiling..”
javac -cp contest.jar player25.java Swarm.java Particle.java Subswarm.java Population.java Kmeans.java 
echo “creating submission.jar” 
jar cmf MainClass.txt submission.jar player25.class Subswarm.class Particle.class Population.class Kmeans.class
echo “running…”
echo $1 
java -jar testrun.jar -submission=player25 -evaluation=SchaffersEvaluation -seed=$1

 #BentCigarFunction KatsuuraEvaluation SphereEvaluation

