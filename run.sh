echo “compiling..”
javac -cp contest.jar player25.java Swarm.java Particle.java 
echo “creating submission.jar” 
jar cmf MainClass.txt submission.jar player25.class Swarm.class Particle.class
echo “running…” 
java -Dvar=10 -jar testrun.jar -submission=player25 -evaluation=BentCigarFunction -seed=1 #BentCigarFunction KatsuuraEvaluation SphereEvaluation