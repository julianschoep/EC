echo “compiling..”
javac -cp contest.jar player25.java NeumannNode.java Particle.java Subswarm.java Population.java Kmeans.java 
echo “creating submission.jar” 
jar cmf MainClass.txt submission.jar player25.class NeumannNode.class Subswarm.class Particle.class Population.class Kmeans.class
echo “running…”

java -Dvar=5 -ea -jar testrun.jar -submission=player25 -evaluation=BentCigarFunction -seed=$1

 #BentCigarFunction KatsuuraEvaluation SphereEvaluation SchaffersEvaluation
