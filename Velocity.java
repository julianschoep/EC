public class Velocity{

    private int n_dimensions = 10;
    private double c1 = 2.0;
    private double c2 = 2.0;
    private double inertia = 0.7;

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

    public double[] update_velocity(double velocity_at_position){
        private double[] oldVelocity = this.velocity;
        private double[] newVelocity = double[this.n_dimensions];

        //newVelocity = inertia * oldVelocity + c1(p - x(t)) * R1 + c2(g - x(t)) * R2
        newVelocity = inertia * velocity_at_position + (c1 * (p - x(t))) * R1 + c2(g - x(t)) * R2
                // R1 = dxd diagonal matrix with random numbers from normal distribution [0,1]
                // R2 = dxd diagonal matrix with random numbers from normal distribution [0,1]
                //  p = personal best position
                //  g = global best position

    }
}