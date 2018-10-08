import os

baseDir = os.getcwd()
outputDir = os.path.join(baseDir, 'output')

if os.path.isdir(outputDir) != True:
    os.mkdir(outputDir)

for nP in [10,15,20,25,30,35,40,45, 50]:
    bash_cmd = "java -Dvar={0} -jar testrun.jar -submission=player25 -evaluation=SphereEvaluation -seed=1 #BentCigarFunction KatsuuraEvaluation SphereEvaluation".format(nP)
    
    bashFile = os.path.join(outputDir, "EA_nP_{0}.sh".format(nP))
    logfile = os.path.join(outputDir, "logfile_nP_{0}.txt".format(nP))
    
    with open(bashFile, 'w') as of:
        of.write("#!/usr/bin/env python\n")
        of.write('cd {0}\n'.format(baseDir))
        of.write(bash_cmd)
    os.system('cd .. && bash {0} &> {1}.txt'.format(bashFile, logfile))