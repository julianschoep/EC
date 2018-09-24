public class Particle {
    private Position p;
    private Velocity v;
    private double fitness;
    private double[] pbest;

    public Particle(Position p, Velocity v, double fitness){
        this.p = p;
        this.v = v;
        this.fitness = fitness;
    }
    public void setFitness(double fitness){
        this.fitness = fitness;
    }

    public Velocity getVelocity(){
        return this.v;
    }

    public Position getPosition(){
        return this.p;
    }

    public void setPosition(Position p){
        this.p = p;
    }
    public void setVelocity(Velocity v){
        this.v = v;
    }
    // velocity is defined as
    // v = v + c1 * rand()? * (pbest - current) + c2 * rand()? * (gbest - current)
    // position is updated as
    // current += v

    public void updatePosition(double gbest){
        this.v = this.v + (C1 * )
    }




}