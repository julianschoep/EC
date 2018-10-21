import numpy as np 
import pandas as pd 
import os, sys, re
import seaborn as sns
import matplotlib.pyplot as plt
import scipy as sp


def getDataset(baseDir, outputDir, fileName, loadData = True, saveDataset = True):
    
    ## Load dataset if available:
    if (fileName in os.listdir(baseDir)) and (loadData):
        print('Merged dataset found. Loading it...')
        overall_df = pd.read_csv(os.path.join(baseDir, fileName))

    else:
        # check if output folder exists
        if outputDir.split('/')[-1] not in os.listdir(): sys.exit('No output folder found. Python scripts exits.')
        else: print('No merged dataset found. Loading data files...')


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
                nParticles = int(splitted_log[4])
                particlesColumnList = ['particle_{0}'.format(i) for i in np.arange(nParticles)]

                file = open(os.path.join(outputDir, file), 'r').readlines()
                for index, line in enumerate(file):
                    if 'Iteration' in line:
                        iterationList.append(int(line.split(" ")[1]))
                        iterationScore.append(float(line.split(" ")[2].split('\n')[0]))
                        lastIterationIndex = index
                    elif 'Score' in line: 
                        scoreList.append(float(line.split(' ')[1].split('\n')[0]))
                    elif 'Runtime' in line:
                        runTimeList.append(float(line.split(' ')[1].split('ms')[0]))



                # Getting the particle values for each particle of last iteration
                particleVals = list()
                for particleIndex in range(int(splitted_log[4])):
                    particleLine = file[1 + lastIterationIndex + particleIndex].split(' ')
                    particleVal = float(particleLine[3].split('\n')[0])
                    particleVals.append(particleVal)

                particleDict = dict()
                for dName, dVal in zip(particlesColumnList, particleVals):
                    particleDict.update( {dName : dVal} )

                if idx == 0: 
                    pd.set_option('display.precision',12)
                    overall_df = pd.DataFrame(data = {'Iteration': iterationList, 'iterationScore': iterationScore})
                    overall_df['Function'] = functionList[idx]
                    overall_df['Particles'] = numberParticles[idx]
                    overall_df['seed'] = seedList[idx]
                    overall_df['Score'] = scoreList[idx]
                    for pName, pVal in particleDict.items():
                        overall_df[pName] = pVal
                elif idx > 0:
                    individual_df = pd.DataFrame(data = {'Iteration': iterationList, 'iterationScore': iterationScore})
                    individual_df['Function'] = functionList[idx]
                    individual_df['Particles'] = numberParticles[idx]
                    individual_df['seed'] = seedList[idx]
                    individual_df['Score'] = scoreList[idx]
                    for pName, pVal in particleDict.items():
                        individual_df[pName] = pVal
                    overall_df = pd.concat([overall_df, individual_df])

                idx += 1
        overall_df['Particles'] = pd.to_numeric(overall_df['Particles'])
        overall_df['seed'] = pd.to_numeric(overall_df['seed'])
        overall_df['Score'] = pd.to_numeric(overall_df['Score'])
        if saveDataset: overall_df.to_csv(os.path.join(baseDir, fileName))
    return overall_df

def getMeanArray(overall_df, functionName):

    # Subselect df based on function
    sub_df = overall_df[overall_df['Function'] == functionName]

    # get all particle columns
    cols = sub_df.columns
    particleCols = [col for col in cols if 'particle' in col]

    # calculate mean value per particle (i.e. across seeds)
    meanArray = [ np.mean( sub_df[pCol] ) for pCol in particleCols ]
    meanArray = np.array(meanArray)
    return meanArray

def showKDEPlot(overall_df, functionName, savePlot = False, nameExtension = ''):
    # General sns configurations
    #sns.set_style("white")

    meanArray = getMeanArray(overall_df, functionName)

    # Plot KDE
    sns.distplot(meanArray,  
                 hist=False,
                 rug=True,
                 kde=True, 
                 bins=150, 
                 color = 'darkblue', 
                 hist_kws={'edgecolor':'black', 'bw':0.2},
                 kde_kws={'linewidth': 2})

    # Plot Configurations
    plt.xlim(0, 10)
    plt.xlabel('Fitness')
    plt.ylabel('Density')
    title = " ".join(re.findall('[A-Z][^A-Z]*', functionName))
    plt.title(title)
    sns.despine()

    if savePlot: plt.savefig('{0}{1}.png'.format(functionName, nameExtension))
    plt.show()
    # print('{0}: {1}, size: {2}'.format(functionName, np.mean(meanArray), sub_df.shape[0]))

def permutation_test(overall_df, functionName, toy = True): 

    meanArray = getMeanArray(overall_df, functionName = 'KatsuuraEvaluation')

    if toy: 
        rndVals = np.random.rand((meanArray.shape[0])) + 6
        totalArray = np.concatenate((meanArray, rndVals))

    nSimulations = 100000

    means = np.zeros((nSimulations))

    for i in range(nSimulations):
        vals = np.random.permutation(totalArray)[ : meanArray.shape[0]]
        means[i] = np.mean(vals)

    stat, pVal = sp.stats.mannwhitneyu(meanArray, rndVals)
    print('Mann Whitney U Test:\nStatistic: {0}, p-Val: {1}'.format(stat, pVal))
    return meanArray, rndVals


    # observedMean = overall_df
    # ## Initialize the permutation test
    # np.random.seed(42)

    # diffs = np.zeros((nsims))
    # for i in range(nsims):
    #     beta, sqrt, var = GLM_model(subtracted_df, original = 0, events = events, dict_stimuliStarts = dict_stimuliStarts, irf = irf)

    #     diffs[i] = var

    # ## two-sided p-value
    # numberMoreExtremeValues = len(diffs[abs(diffs) >= var_obs])
    # p_value = numberMoreExtremeValues / float(nsims)
    # return diffs, p_value

def main():
    baseDir = os.getcwd()
    outputDir = os.path.join(baseDir, 'output')
    fileName = 'merged_output.csv'
    loadData = True
    saveDf = True

    overall_df = getDataset(baseDir, outputDir, fileName, loadData = loadData, saveDataset = saveDf)

    # for plotFunction in ['SchaffersEvaluation', 'KatsuuraEvaluation']:#'SphereEvaluation','BentCigarFunction', 'SchaffersEvaluation', 
        # showKDEPlot(overall_df, plotFunction, savePlot = True, nameExtension = '')
    vals, rndVals = permutation_test(overall_df, 'KatsuuraEvaluation')



def plotBothKDE(psoValues, kpsoValues):

    # Plot KDE
    fig, ax = plt.subplots()
    sns.distplot(vals,  
                 hist=False,
                 rug=True,
                 kde=True, 
                 bins=150, 
                 color = 'darkblue',
                 ax=ax, 
                 hist_kws={'edgecolor':'black'},
                 kde_kws={'linewidth': 2, 'bw':0.2})
    sns.distplot(rndVals,  
                 hist=False,
                 rug=True,
                 kde=True, 
                 bins=150, 
                 color = 'green', 
                 ax = ax,
                 hist_kws={'edgecolor':'black'},
                 kde_kws={'linewidth': 2, 'bw':0.2})

    # Plot Configurations
    plt.xlim(0, 10)
    plt.xlabel('Fitness')
    plt.ylabel('Density')
    plt.legend(['PSO', 'kPSO'])


    # title = " ".join(re.findall('[A-Z][^A-Z]*', functionName))
    # plt.title(title)
    sns.despine()

    # if savePlot: plt.savefig('{0}{1}.png'.format(functionName, nameExtension))
    plt.show()
















if __name__ == '__main__':
    main()























