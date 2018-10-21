import os
import numpy as np
import time
import multiprocessing as mp

class qworker(mp.Process):
	def __init__(self, inq, base_dir):
		super(qworker, self).__init__()
		self.inq = inq
		self.base_dir = base_dir

	def run(self):
		while(True):
			qItem = self.inq.get()
			if not qItem:
				break
			else:
				(bash_cmd, bashFile, logfile) = qItem
				s = time.time()
				bash_cmd, bashFile, logfile = next(cmd_gen)
				with open(bashFile, 'w') as of:
					of.write("#!/usr/bin/env python\n")
					of.write('cd {0}\n'.format(baseDir))
					of.write(bash_cmd)
				# run command
				os.system('cd .. && bash {0} > {1}.txt'.format(bashFile, logfile))
				print("took {} mins".format(((time.time()-s)/60)))


def command_generator():
	baseDir = os.getcwd()
	outputDir = os.path.join(baseDir, 'output')
	n_seeds = 5

	if os.path.isdir(outputDir) != True:
	    os.mkdir(outputDir)

	# iterate through every function #BentCigarFunction KatsuuraEvaluation SphereEvaluation
	for func in ['KatsuuraEvaluation']:#'SphereEvaluation', 'BentCigarFunction', 'KatsuuraEvaluation', 'SchaffersEvaluation']:
		# iterate through parameter setting (numbers of particles)
		for C in [100,200,500,1000,2000]:
			
			# iterate through seeds as iterations per setting
			for seedValue in np.linspace(1, n_seeds, n_seeds, dtype = np.int32):
			    s = time.time()
			    print("{}, {}, {}".format(func,C,seedValue))
			    # java command 
			    bash_cmd = "java -Dvar={var} -jar testrun.jar -submission=player25 -evaluation={func} -seed={seed} ".format(var = C, func = func, seed = str(seedValue))
			    
			    # .sh file containing java file
			    bashFile = os.path.join(outputDir, "EA_func_{func}_C_{var}_seed_{seed}.sh".format(func = func, var = C, seed = seedValue))
			    # log file 
			    logfile = os.path.join(outputDir, "logfile_func_{func}_C_{var}_seed_{seed}".format(func = func, var = C, seed = seedValue))
			    
			    yield (bash_cmd, bashFile, logfile)

cmd_gen = command_generator()

inq = mp.Queue()
nworkers = 4
baseDir = os.getcwd()

for i in range(nworkers):
	qworker(inq, baseDir).start()

while(True):
	try:
		(bash_cmd, bashFile, logfile) = next(cmd_gen)
		inq.put((bash_cmd, bashFile, logfile))
	except StopIteration:
		print('DONE FILLING QUEUE')
		break
for i in range(nworkers):
	inq.put(None)






	# save .sh file with java command
	
