public class Swarm {
    private Particle[] swarm;
    private Position gbest;
    private double gbestFitness;
    private int pSize;
    private ContestEvaluation evaluation;


    public Swarm(int pSize, int nDimensions, ContestEvaluation evaluation){
        // Initialize the swarm
        this.swarm = Particle[pSize];
        this.psize = pSize;
        this.gbestFitness = -1;
        this.evaluation = evaluation;

        for(int i = 0; i < pSize; i++){
            this.swarm[i] = Particle(nDimensions); // randomly init N particles
        }
    }

    public Position iterate(){ // not very pretty...

        // update particle positions
        Position[psize] currentPositions
        for(int i = 0; i < this.pSize; i++){
            currentPositions[i] = this.swarm[i].update(this.gbest)
        }

        // determine new gbest
        for(int i = 0; i < this.pSize; i++){
            particlePosition = currentPositions[i].getPosition();
            fitness = this.evaluation(particlePosition.getCoordinates())
            if fitness > this.gbestFitness{
                this.gbestFitness = fitness;
                this.gbest = currentPositions[i];
            }
        }
        return this.gbest;
    }




}