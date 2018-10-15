import os
import numpy as np

baseDir = os.getcwd()
outputDir = os.path.join(baseDir, 'output')
n_seeds = 100

if os.path.isdir(outputDir) != True:
    os.mkdir(outputDir)

# iterate through every function #BentCigarFunction KatsuuraEvaluation SphereEvaluation
for func in ['SphereEvaluation', 'BentCigarFunction', 'SchaffersEvaluation']:#'SphereEvaluation', 'BentCigarFunction', 'KatsuuraEvaluation']:
	# iterate through parameter setting (numbers of particles)
	for nP in [100]:
		# iterate through seeds as iterations per setting
		for seedValue in np.linspace(1, n_seeds, n_seeds, dtype = np.int32):
		    # java command 
		    bash_cmd = "java -Dvar={var} -jar testrun.jar -submission=player25 -evaluation={func} -seed={seed} ".format(var = nP, func = func, seed = str(seedValue))
		    
		    # .sh file containing java file
		    bashFile = os.path.join(outputDir, "EA_func_{func}_nP_{var}_seed_{seed}.sh".format(func = func, var = nP, seed = seedValue))
		    # log file 
		    logfile = os.path.join(outputDir, "logfile_func_{func}_nP_{var}_seed_{seed}".format(func = func, var = nP, seed = seedValue))
		    
		    # save .sh file with java command
		    with open(bashFile, 'w') as of:
		        of.write("#!/usr/bin/env python\n")
		        of.write('cd {0}\n'.format(baseDir))
		        of.write(bash_cmd)
		    # run command
		    os.system('cd .. && bash {0} &> {1}.txt'.format(bashFile, logfile))