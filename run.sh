echo “compiling..”
javac -cp contest.jar player25.java NeumannNode.java Particle.java Subswarm.java Population.java Kmeans.java 
echo “creating submission.jar” 
jar cmf MainClass.txt submission.jar player25.class NeumannNode.class Subswarm.class Particle.class Population.class Kmeans.class
echo “running…”
sleep 1
echo "3"
sleep 1
echo "2"
sleep 1
echo "1"
sleep 1
echo "go"
java -Dvar=$2 -ea -jar testrun.jar -submission=player25 -evaluation=KatsuuraEvaluation -seed=$1

 #BentCigarFunction KatsuuraEvaluation SphereEvaluation SchaffersEvaluation
