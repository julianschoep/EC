import numpy as np 
import pandas as pd 
import os, sys

baseDir = os.getcwd()
outputDir = os.path.join(baseDir, 'output')

# check if output folder exists
if outputDir.split('/')[-1] not in os.listdir(): sys.exit('No output folder found. Python scripts exits.')
else: print('Loading data files...')


#Function, nP, seed, score, runtime
functionList = list()
numberParticles = list()
seedList = list()
scoreList = list()
runTimeList = list()

idx = 0
for file in sorted(os.listdir(outputDir)):
	iterationList = list()
	iterationScore = list()

	if "logfile" in file:
		
		print('file: {0}'.format(file))

		splitted_log = file.split('_')
		functionList.append(splitted_log[2])
		numberParticles.append(splitted_log[4])
		seedList.append(splitted_log[-1].split('.')[0])

		file = open(os.path.join(outputDir, file), 'r').readlines()
		for line in file:
			if 'Iteration' in line:
				iterationList.append(int(line.split(" ")[1]))
				iterationScore.append(float(line.split(" ")[2].split('\n')[0]))

			elif 'Score' in line: 
				#print(line)#print( float(line.split(' ')[1].split('\n')[0]))
				scoreList.append(float(line.split(' ')[1].split('\n')[0]))
			elif 'Runtime' in line:
				runTimeList.append(float(line.split(' ')[1].split('ms')[0]))

		if idx == 0: 
			overall_df = pd.DataFrame(data = {'Iteration': iterationList, 'iterationScore': iterationScore})
			overall_df['Function'] = functionList[idx]
			overall_df['Particles'] = numberParticles[idx]
			overall_df['seed'] = seedList[idx]
			overall_df['Score'] = scoreList[idx]
		elif idx > 0:
			individual_df = pd.DataFrame(data = {'Iteration': iterationList, 'iterationScore': iterationScore})
			individual_df['Function'] = functionList[idx]
			individual_df['Particles'] = numberParticles[idx]
			individual_df['seed'] = seedList[idx]
			individual_df['Score'] = scoreList[idx]
			overall_df = pd.concat([overall_df, individual_df])
		idx += 1


print(overall_df.head())
print(overall_df.shape)
print(overall_df['seed'].value_counts())


