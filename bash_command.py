import os
import subprocess

#baseDir = os.getcwd()
baseDir = "/Users/daniellutscher/Drive/--\ Universiteit\ --/Jahr\ 3/evolutionary\ computing/EC"

for nP in [1,2,3]:
	bash_cmd = "java -Dvar={0} -jar testrun.jar -submission=player25 -evaluation=SphereEvaluation -seed=1 #BentCigarFunction KatsuuraEvaluation SphereEvaluation".format(nP)
	logFile =  "&> logfile.txt"

# 	print("Running")
# 	subprocess.call(bash_cmd, shell=True)

baseDir = os.getcwd()
bashFile = os.path.join(baseDir, "EA_nP_1.sh")
test = "bash {0} &> logfile.txt".format(bashFile)
#subprocess.call(["bash", bashFile, logFile])
subprocess.call(test)

