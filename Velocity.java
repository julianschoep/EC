public class Velocity{

    private double c1 = 2.0;
    private double c2 = 2.0;

    private double[] velocity;

    public Velocity(double[] velocity){
        this.velocity = velocity;
    }
    public double[] getVelocity(){
        return this.velocity;
    }

    public void setVelocity(double[] velocity){
        this.velocity = velocity;
    }

    public void setVelocityAt(int i, double vi){
        this.velocity[i] = vi;
    }

    public double getVelocityAt(int i){
        return this.velocity[i];
    }
}