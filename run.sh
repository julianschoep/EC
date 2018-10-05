echo “compiling..”
javac -cp contest.jar player25.java Swarm.java Particle.java 
echo “creating submission.jar” 
jar cmf MainClass.txt submission.jar player25.class Swarm.class Particle.class
echo “running…” 
java -jar testrun.jar -submission=player25 -evaluation=SphereEvaluation -seed=1 #BentCigarFunction KatsuuraEvaluation SphereEvaluation