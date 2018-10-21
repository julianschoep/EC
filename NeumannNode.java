public class NeumannNode extends Particle{
    private NeumannNode[] neighbours;
    private int neighbourPointer;
    private NeumannNode thisParticle;
    private double GlobalBestFitness;
    private double[] GlobalBestPosition;

    public NeumannNode(Particle particle){
        super(particle.getNDims(), particle.getEvaluation(), particle.getRnd(), particle.name);
        String line = String.format("Position should be random init, thus same as best: %f vs %f", this.bestPosition[0],this.position[0]);
        assert this.bestPosition[0] == this.position[0]:  line;

        this.neighbours = new NeumannNode[2];
        this.neighbourPointer = 0;
        this.GlobalBestFitness = this.bestFitness;
        this.GlobalBestPosition = this.bestPosition;
    }

    public void reportNeighbours(){
        assert this.neighbourPointer == 2: "Neigbourhood isn't filled yet.. Can't report.";
        int[] names = new int[3];
        names[0] = this.name;
        //System.out.println(String.format("this name: %d",this.name));
        for(int i = 0; i < this.neighbours.length; i++){
            names[i+1] = this.neighbours[i].name;
            //System.out.println(String.format("neighbour name: %d",neighbours[i].name));
        }
        String line = String.format("left %d, this: %d, right %d",names[1], names[0],names[2]);
        System.out.println(line);
    }
    public boolean addNeighbour(NeumannNode neighbour){

        if(neighbour.name == this.name){
            System.out.println("CANT BE NEIGHBOURS WITH YOURSELF YANNY");
            //System.out.println('1');
            return false;
        }

        if(this.neighbourPointer == 2){
            System.out.println(Integer.toString(this.name)+": Neighbours topology is full! ("+Integer.toString(neighbour.name)+")");
            //System.out.println('2');
            return false;
        }

        if(this.neighbourPointer == 0){ // no neighbours assigned yet
            //System.out.println('3');
            if(neighbour.name != this.name){
                this.neighbours[0] = neighbour;
                this.neighbourPointer += 1;
            }else{
                return false;
            }
        }else{
            //System.out.println("ADDING NEIGHBOUR TO NEIHBOUR HOOD YAY");
            //System.out.println(String.format("Neighbourpointer: %d",this.neighbourPointer));
            for(int i = 0; i < this.neighbourPointer; i++){
                //System.out.println((i));
                //String line = String.format("%d is in neighbourhood!",this.neighbours[i].name);
                //System.out.println(line);
                int h = 0;
            }
            //System.out.println('4');
            // check that neighbour isn't already in neighbourhood
            for(int i = 0; i < this.neighbourPointer; i++){
                //System.out.println('5');
                //System.out.println(i);
                //System.out.println(this.neighbours[i].name);

                if(this.neighbours[i].name == neighbour.name){
                    return false;
                }
                //System.out.println('6');
            }
            // neighbour not yet in neighbourhood
            this.neighbours[this.neighbourPointer] = neighbour;
            this.neighbourPointer ++;

        }
        return true;
    }

    public double updateGlobalFitness(){
        if(this.bestFitness > this.GlobalBestFitness) {
            this.GlobalBestFitness = this.bestFitness;
            this.GlobalBestPosition = this.bestPosition;
        }
        for(int i = 0; i < this.neighbourPointer; i++){
            NeumannNode neighbour = this.neighbours[i];
            if(neighbour.bestFitness > this.GlobalBestFitness){
                this.GlobalBestFitness = neighbour.bestFitness;
                this.GlobalBestPosition = neighbour.bestPosition;
            }
        }
        return this.GlobalBestFitness;
    }
    public double[] getGlobalBestPosition(){
        return this.GlobalBestPosition;
    }
}